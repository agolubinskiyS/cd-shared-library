#!/usr/bin/env bash


. ./sso_login-2.4.0.sh && CICDCD_SSO_URL="$1" CICDCD_SSO_USER_ID="$2" CICDCD_SSO_USER_PASSWORD="$3" CICDCD_SSO_TENANT="$4" login_CCT --rawcookies