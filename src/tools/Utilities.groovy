package tools

class Utilities implements Serializable {

    def script
    String cookie
    String command
    Map params = [:]

    Utilities(script, params) {
      this.script = script
      command = """
      CICDCD_SSO_URL="$params.url" \
      CICDCD_SSO_TENANT="$params.tenant"
      """
      this.params = params
    }
    void prueba() {
      script.echo(command)
    }

    void deploy(Map params = [:]) {
      String cookie = login(params)
      publishApplication()
    }
    public login(String Password, String Username) {
      def operation = String.format("./login.sh %s %s %s %s", params.url, Username, Password, params.tenant)
      def result = script.sh(returnStdout: true, script: operation).trim().substring(21)
      cookie = result.substring(0, result.indexOf(';')) 
      return cookie
    }
    void publishApplication() {
      script.echo(cookie)
      // script.sh(returnStdout: true, script: operation+"publishApplication")
      // operation = """. ./cct-api.sh && \
      // CICDCD_SSO_URL="$params.url" \
      // CICDCD_SSO_USER_ID="$params.user" \
      // CICDCD_SSO_USER_PASSWORD="$params.password" \
      // CICDCD_SSO_TENANT="$params.tenant" \
      // deploymentDescriptor="$params.deploymentDescriptor" \
      // model="$params.model" \
      // version="$params.version" \
      // service="$params.service" \
      // cookie="$cookie" 
      // """
    }
}