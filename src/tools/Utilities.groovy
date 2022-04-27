package tools

class Utilities implements Serializable {

    def script
    
    Utilities(script) {
        this.script = script
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
    void publishApplication(Map params = [:], String cookie) {
      script.sh(""". ./publish.sh && \
      CICDCD_SSO_URL="$params.url" \
      CICDCD_SSO_USER_ID="$params.user" \
      CICDCD_SSO_USER_PASSWORD="$params.password" \
      CICDCD_SSO_TENANT="$params.tenant" \
      deploymentDescriptor="$params.deploymentDescriptor" \
      model="$params.model" \
      version="$params.version" \
      service="$params.service" \
      cookie="$cookie"
      publishApplication
      """)
    }
}