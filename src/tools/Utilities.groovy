package tools

class Utilities implements Serializable {

    def script
    
    Utilities(script) {
        this.script = script
    }

    void deploy(Map params = [:]) {
      login(params)
      publishApplication(params)
    }
    public login(Map params = [:]) {
      def operation = String.format("./login.sh %s %s %s %s", params.url, params.user, params.password, params.tenant)
      def cookie = script.sh(returnStdout: true, script: operation).trim().substring(21)
      return cookie.substring(0, cookie.indexOf(';'))
    }
    void publishApplication(Map params = [:]) {
      script.echo this.cookie
      // script.sh("""./script.sh \
      // CICDCD_SSO_URL="$params.url" \
      // CICDCD_SSO_USER_ID="$params.user" \
      // CICDCD_SSO_USER_PASSWORD="$params.password" \
      // CICDCD_SSO_TENANT="$params.tenant" \
      // deploymentDescriptor="$params.deploymentDescriptor" \
      // model="$params.model" \
      // version="$params.version" \
      // service="$params.service" """)
    }
}