package tools

class Utilities implements Serializable {

    def script
    String operation

    Utilities(script, operation) {
      this.script = script
      // String operation = """. ./cct-api.sh && \
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
      this.operation = operation
    }
    void prueba() {
      script.echo(operation)
    }

    void deploy(Map params = [:]) {
      String cookie = login(params)
      publishApplication(params, cookie)
    }
    public login(Map params = [:]) {
      def operation = String.format("./login.sh %s %s %s %s", params.url, params.user, params.password, params.tenant)
      def result = script.sh(returnStdout: true, script: operation).trim().substring(21)
      result = result.substring(0, result.indexOf(';')) 
      return result
    }
    void publishApplication(String cookie) {
      script.sh(returnStdout: true, script: operation+"publishApplication")
    }
}