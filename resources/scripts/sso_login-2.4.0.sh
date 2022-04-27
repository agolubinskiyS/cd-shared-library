#!/usr/bin/env bash 

[[ $DEBUG == true ]] && set -x || set +x

# set -o nounset
# set -o errexit

############## Dependency checking
DEPENDENCIES_CHECK() { 
    for program in "$@" ; do
        type "${program}" >/dev/null 2>&1 || {
            echo >&2 "I require ${program} but it's not installed. Aborting."
            exit 1
        }
    done
}
DEPENDENCIES_CHECK "curl" "getopt" "awk"

if [[ ! $(declare -f B_LOG) ]] ; then
    : "${LOG_LEVEL:=ERROR}"
    define(){ IFS='\n' read -r -d '' ${1} || true ; }
    emptyBody="{[[:space:]]*:[[:space:]]*}"
    [[ $(declare -f TRACE) =~ $emptyBody ]] || TRACE() { if [[ "${TERM}" =~ xterm ]] ; then printf >&2 "\033[36;2;1m[TRACE]\033[0m \033[36;2m%s\033[0m\n" "${1:-}" ; else printf >&2 "[TRACE] %s\n" "${1:-}" ; fi ; }
    [[ "${LOG_LEVEL}" =~ ^(ERROR|INFO|DEBUG)$ ]] && { TRACE(){ :; } } ; [[ $(declare -f DEBUG) =~ $emptyBody ]] || DEBUG() { if [[ "${TERM}" =~ xterm ]] ; then printf >&2 "\033[34;2;1m[DEBUG]\033[0m \033[34;2m%s\033[0m\n" "${1:-}" ; else printf >&2 "[DEBUG] %s\n" "${1:-}" ; fi ; }
    [[ "${LOG_LEVEL}" =~ ^(ERROR|INFO)$ ]] && { DEBUG(){ :; } } ; [[ $(declare -f INFO) =~ $emptyBody ]] || INFO() { if [[ "${TERM}" =~ xterm ]] ; then printf >&2 "\033[34;2;1m[INFO]\033[0m \033[34;2m%s\033[0m\n" "${1:-}" ; else printf >&2 "[INFO] %s\n" "${1:-}" ; fi ; }
    [[ "${LOG_LEVEL}" =~ ^(ERROR)$ ]] && { INFO(){ :; } } ; declare -F ERROR >/dev/null || ERROR() { if [[ "${TERM}" =~ xterm ]] ; then printf >&2 "\033[31;1m[ERROR]\033[0m \033[31m%s\033[0m\n" "${1:-}" ; else printf >&2 "[ERROR] %s\n" "${1:-}" ; fi ; }
fi
msg() { if [[ "${TERM}" =~ xterm ]] ; then printf >&2 "\033[1m%s\033[0m\n" "${1:-}" ; else printf >&2 "%s\n" "${1:-}" ; fi ; }
die() { ERROR "${1:-An error has occured.Exiting.} (${2:-1})" ; return "${2:-1}" ; }

__curl() {
    local defaultCurlOptions=( --connect-timeout 5 --retry 5 --retry-delay 2 --tlsv1.2 --insecure --silent --write-out "\n%{response_code} %{url_effective}" )
    local curlArguments=("${defaultCurlOptions[@]}" "$@")
    DEBUG "curl ${curlArguments[*]}"
    response=$(curl "${curlArguments[@]}") || returnCode=$?
    TRACE "response: ${response}"
    data=$(echo "${response}" | head -1 )
    status_code=$(echo "${response}" | tail -1 )
    status_code="${status_code%% *}"
    DEBUG "__curl status_code: ${status_code}; return code: ${returnCode}"
    echo "${response}"
    case "${status_code:-$returnCode}" in
        20*|30*) : ;;
        4*|5*)      die "${data} http status: ${status_code}" ;;
        *)          die "Fail to execute the curl command; return code: ${returnCode}" "${returnCode}"
    esac
    return $?
}

_login() {
    local url_dcos="${CICDCD_SSO_URL:-}"
    local user_id="${CICDCD_SSO_USER_ID:-}"
    local user_password="${CICDCD_SSO_USER_PASSWORD:-}"
    local tenant="${CICDCD_SSO_TENANT:-NONE}"
    local authCookieLabel="${AUTH_COOKIE_LABEL:-^.*-auth-cookie}"
    local infoCookieLabel="${INFO_COOKIE_LABEL:-^.*-info-cookie}"
    local cookiesFile="${COOKIE_FILE:-}"
    
    local returnCode=0
    local response
    local status_code
    local url_login_submit
    local cookie_auth
    local cookie_info
    local input_lt
    local input_execution
    local old_ifs
    local cookiesFirstLogin="$(mktemp -p /dev/shm)"

    INFO "Starting the login to ${url_dcos} with user ${user_id}"
    TRACE "login with ${url_dcos} ${user_id} ${user_password} ${tenant} ${raw_cookies}"

    old_ifs=$IFS
    ############## Recuperación de los datos de conexión
    local loginPageOptions=(
        --location
        --cookie-jar "${cookiesFirstLogin}"
        --url "${url_dcos}"
    )

    response=$( __curl "${loginPageOptions[@]}" ) || returnCode="$?"
    TRACE "returnCode after the __curl invocation: ${returnCode}"
    TRACE "tail: $( echo "${response}" | tail -1)"
    [[ ${returnCode} -eq 0 ]] && IFS=' ' read -r status_code url_login_submit <<< "$( echo "${response}" | tail -1)"
    DEBUG "Status Code ${status_code:-$returnCode} url_login_submit: ${url_login_submit}"
    case "${status_code:-$returnCode}" in
        6)          die "Could not resolve ${url_dcos}. Verify the endpoint." ;;
        20*|30*)    : ;;
        401)        die "User is unauthorized to access ${url_dcos}. Verify the user credentials." ;;
        *)          die "Invalid login to endpoint ${url_dcos}. The endpoint is invalid or is unavailable." "${returnCode}"
    esac || return $?

    IFS=' ' read -r input_lt input_execution <<< $(
        echo "${response}" | 
            awk -v RS='<input ' -v FS='"' -v ORS=' ' '
                /"execution"/ { for(i=1;i<=NF;i++) if($i ~ /value/) execution=$(i+1) }
                /"lt"/ { for(i=1;i<=NF;i++) if($i ~ /value/) lt=$(i+1) }
                END { print lt, execution }
            ')
    [[ -z "${input_lt}" || -z "${input_execution}" ]] && { die "Fail to get SSO information from ${url_dcos}. Review the endpoint." ; return $? ; }

    ############## Login
    local loginvalidationPageOptions=(
        --location
        --cookie "${cookiesFirstLogin}"
        --data -
        --output /dev/null
        --data _eventId=submit
        --data tenant="$tenant"
        --data username="$user_id"
        --data password="$user_password"
        --data lt="$input_lt"
        --data-urlencode execution="${input_execution}"
        --compressed
        --url "${url_login_submit}"
    )
    [[ -n $cookiesFile ]] && loginvalidationPageOptions=( "${loginvalidationPageOptions[@]}" --cookie-jar "${cookiesFile}" )

    response=$( __curl "${loginvalidationPageOptions[@]}" ) || returnCode="$?"
    TRACE "returnCode ${returnCode}"

    [[ "${returnCode}" ]] && IFS=' ' read -r status_code url_login_submit <<< "$( echo "${response}" | tail -1)"
    TRACE "Status Code ${status_code:-$returnCode}"
    case "${status_code:-$returnCode}" in
        6)          die "Could not resolve ${url_dcos}. Verify the endpoint." ;;
        200|201|202) : ;;
        401)        die "User is unauthorized to access ${url_login_submit}. Verify the user credentials." ;;
        *)          die "Invalid login endpoint ${url_login_submit}. The endpoint is invalid or is unavailable."
    esac || return $?

    if [[ -z $cookiesFile ]] ; then 
        IFS=' ' read -r cookie_auth cookie_info <<< "$(
                echo "${response}" | 
                awk -v infoLabel="${infoCookieLabel}=" -v authLabel="${authCookieLabel}=" '
                    tolower($1) ~ /^set-cookie:/ && $0 ~ authLabel { auth=$2; next}
                    tolower($1) ~ /^set-cookie:/ && $0 ~ infoLabel { info=$2; next}
                    END{print auth, info} 
                ')"
        if [[ -z $cookie_auth ]] || [[ -z $cookie_info ]] ; then
            IFS=$old_ifs
            die "Login failed for user ${user_id}. Verify the user credential."
        fi
        export RAW_COOKIES="${cookie_auth}${cookie_info}"

        cookie_auth="${cookie_auth##${authCookieLabel}=}"
        cookie_info="${cookie_info##${infoCookieLabel}=}"

        export COOKIE_AUTH="${cookie_auth%?}"
        export COOKIE_INFO="${cookie_info%?}"
    fi

    IFS="${old_ifs}"
    rm "${cookiesFirstLogin}" 2> /dev/null

    return 0

}

############## Login
# 
# login to DCOS
#     Positional form
#       INPUTS:
#           1: URL DCOS with /login path
#           2: User_id with permission to execute CCT API
#           3: User Password
#           4: tenant (optional)
#       OUTPUTS:
#          populate COOKIE_DCOS_ACS_AUTH COOKIE_DCOS_ACS_INFO
#       EXAMPLE
#           . ./sso_login.sh &&  login_CCT <SSO_login_URL> <USER> <PASS> && echo "dcos-acs-auth-cookie=${COOKIE_DCOS_ACS_AUTH};dcos-acs-info-cookie=${COOKIE_DCOS_ACS_INFO};"
#
#     Named parameter form
#       INPUTS:
#        USAGE:
#            CICDCD_SSO_URL              Envirenment variable, the url of login screen
#            CICDCD_SSO_USER_ID          Envirenment variable, sso User
#            CICDCD_SSO_USER_PASSWORD    Envirenment variable, sso User password
#            CICDCD_SSO_TENANT           Envirenment variable, ID of tenant, NONE by default
#            --rawcookies                Flag, if set, write raw cookies to console
#            --discovery                 Flag, if set, fill METABASE_SESSION variable with ticket
#            --rocket                    Flag, if set, show Ticket on sdtout
#       OUTPUTS:
#          populate COOKIE_DCOS_ACS_AUTH COOKIE_DCOS_ACS_INFO
#       EXAMPLE
#           . ./sso_login.sh && CICDCD_SSO_URL="<SSO_login_URL>" CICDCD_SSO_USER_ID="<USER>" CICDCD_SSO_USER_PASSWORD="<PASS>" CICDCD_SSO_TENANT="<TENANT>" login_CCT --rawcookies
#
login_CCT() {

    # parameter passed by environement variable, legacy positional
    local url_dcos=${CICDCD_SSO_URL:-}
    local user_id=${CICDCD_SSO_USER_ID:-}
    local user_password=${CICDCD_SSO_USER_PASSWORD:-}
    local tenant="${CICDCD_SSO_TENANT:-NONE}"

    local discovery_user_id=${CICDCD_DISCOVERY_USER_ID:-aa@gmail.com}
    local discovery_user_password=${CICDCD_DISCOVERY_USER_PASSWORD:-bbbbb}

    # Flag
    local raw_cookies=false
    local rocket=false
    local discovery=false

    local authCookieLabel="${AUTH_COOKIE_LABEL:-}"
    local infoCookieLabel="${INFO_COOKIE_LABEL:-}"
    local cookiesFile="${COOKIE_FILE:-}"

    # cookies
    local cookie_auth cookie_info cookiesText

    local data=""
    local connexionInfo=""
    local response=""
    local metabaseTicket=""

    local HELP_TEXT="  Retreive SSO cookies from platform
        USAGE:
            CICDCD_SSO_URL              Envirenment variable with the url of login screen
            CICDCD_SSO_USER_ID          Envirenment variable with sso User
            CICDCD_SSO_USER_PASSWORD    Envirenment variable with sso User plataform
            CICDCD_SSO_TENANT           Envirenment variable with ID of tenant, NONE by defect
            --rawcookies                to write raw cookies to console

    "

    local options=$(getopt \
        --options h\? \
        --long rawcookies,discovery,rocket,help \
        -- "$@")

    if [[ ${#@} -ne 0 ]] && [[ ${@#"--help"} = "" ]]; then
        printf -- "%s" "${HELP_TEXT}"
        exit 0
    fi

    set -- ${options}

    local position_index_argument=0

    while [[ $# -gt 0 ]] ; do
        case $1 in
            --rawcookies) raw_cookies=true ;;
            --rocket) rocket=true ;;
            --discovery) discovery=true ;;
            -h|--help|-\?)  printf -- "%s" "${HELP_TEXT}" ; exit 0 ;;
            (--)            ;;
            (-*)            printf "%s: error - unrecognized option %s" "$0" "$1" 1>&2 ; exit 1 ;;
            (*) 
                position_index_argument=$((position_index_argument+1)) 
                case ${position_index_argument} in
                    1)      url_dcos=${1:1:-1} ;;
                    2)      user_id=${1:1:-1} ;;
                    3)      user_password=${1:1:-1} ;;
                    4)      tenant=${1:1:-1} ;;
                    (*)     printf "%s: error - unrecognized option %s" "$0" "$1" 1>&2 ; exit 1 ;;
                esac
            ;;
        esac
        shift
    done
    
    [[ -z ${url_dcos} ]] && die "The URL is mandatory."
    [[ -z ${user_id} ]] && die "The User ID is mandatory."
    [[ -z ${user_password} ]] && die "The User Password is mandatory."
    if [[ -z "${cookiesFile}" ]] ; then cookiesFile="$(mktemp -p /dev/shm)" || cookiesFile="" ; fi

    CICDCD_SSO_URL="${url_dcos}" \
    CICDCD_SSO_USER_ID="${user_id}" \
    CICDCD_SSO_USER_PASSWORD="${user_password}" \
    CICDCD_SSO_TENANT="${tenant}" \
    AUTH_COOKIE_LABEL="${authCookieLabel}" \
    INFO_COOKIE_LABEL="${infoCookieLabel}" \
    COOKIE_FILE="${cookiesFile}" \
        _login || return $?
    
    TRACE "cookies file ${cookiesFile}"
    if [[ -n ${cookiesFile} ]] ; then
        IFS=' ' read cookie_auth cookie_info cookiesText <<< "$(
            awk '
                $6 ~ "^.*-auth-cookie$" { authLabel=$6 }
                $6 ~ authLabel { auth=$7; next}
                $6 ~ "^.*-info-cookie$" { infoLabel=$6 }
                $6 ~ infoLabel { info=$7; next}
                END{ printf("%s %s %s=%s;%s=%s;", auth, info, authLabel, auth, infoLabel, info) }
            ' "${cookiesFile}")"
        if [[ -n "${cookie_auth}" && -n "${cookie_info}" ]] ; then
            RAW_COOKIES="${cookiesText};"
            TRACE "Auth Cookie: ${cookie_auth}"
            TRACE "Info Cookie: ${cookie_info}"
            TRACE "raw_cookie: ${RAW_COOKIES}"
        else
            RAW_COOKIES=""
        fi
    fi
    if [[ "${rocket}" == true ]] ; then
        IFS=' ' read -r cookie_user <<< "$(
            awk -v userLabel="user" '
                $6 ~ userLabel { user=$7; next}
                END{print user} 
            ' "${cookiesFile}")"
        if [[ -z "${cookie_user}" ]] ; then
            die "Fail to log in Rocket ${url_dcos} for user ${user_id}. Verify the user credential."
        fi
        TRACE "Ticket: ${cookie_user}"
        INFO "Login successful, the rocket ticket has been retrieved."
        echo "${cookie_user}"
    elif [[ "${discovery}" == true ]] ; then
        local jsonCredential=""
        INFO "Starting the process to obtain the Metabase session for user ${user_id}"
        printf -v jsonCredential '{"username": "%s", "password": "%s"}' "${discovery_user_id}" "${discovery_user_password}"
        TRACE "jsonCredential: ${jsonCredential}"
        TRACE "RAW_COOKIES: ${RAW_COOKIES}"
        local loginMetabaseOptions=(
            --cookie "${RAW_COOKIES}"
            --request POST
            --header "Content-Type: application/json"
            --data-binary "${jsonCredential}"
            --url "${url_dcos}/api/session"
        )
        response=$( __curl "${loginMetabaseOptions[@]}") || returnCode="$?"
        TRACE "response: ${response}"
        data=$(echo "${response}" | head -1 )
        connexionInfo=$(echo "${response}" | tail -1)
        status_code="${connexionInfo%% *}"
        case "${status_code:-${returnCode}}" in
            6)          die "Could not resolve ${url_dcos}. Verify the endpoint." ;;
            200|201|202) : ;;
            302)        die "Fail to login ${connexionInfo#* }" ;;
            *)          die "Endpoint ${connexionInfo#* }"
        esac || return $?
        metabaseTicket=$(echo "${data}" | jq -r '.id' )
        if [[ -z "${RAW_COOKIES}" || -z "${metabaseTicket}" ]] ; then
            DISCOVERY_RAW_COOKIES=""
            DISCOVERY_METABASE_SESSION=""
            die "Fail to log in Discovery ${url_dcos} for user ${user_id}. Verify the user credential."
        fi || return "$?"
        export DISCOVERY_RAW_COOKIES="${RAW_COOKIES};"
        export DISCOVERY_METABASE_SESSION="${metabaseTicket}"
        TRACE "Metabase Ticket: ${DISCOVERY_METABASE_SESSION}"
        INFO "Login successful, the Metabse session is available in the environment variable \$DISCOVERY_METABASE_SESSION"
        [[ "${raw_cookies}" == true ]] && echo "${DISCOVERY_RAW_COOKIES}"
    else
        TRACE "raw cookie: ${RAW_COOKIES}"
        if [[ -z "${RAW_COOKIES}" ]] ; then
            die "Failed to log in ${url_dcos} for user ${user_id}. Verify the user credential."
        fi || return "$?"
        export COOKIE_DCOS_ACS_AUTH="${COOKIE_AUTH:-${cookie_auth}}"
        export COOKIE_DCOS_ACS_INFO="${COOKIE_INFO:-${cookie_info}}"
        INFO "Login successful, the auth and info cookies have been retrieved."
        [[ "${raw_cookies}" == true ]] && echo "${RAW_COOKIES}"
    fi
    return 0
}
