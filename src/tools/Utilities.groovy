package tools
// package src.tools

class Utilities implements Serializable {

    def script
    String cookie
    String command
    Map params = [:]

    Utilities(script, params) {
      this.script = script
      command = """
      . ./cct-api.sh &&
      CICDCD_SSO_URL="$params.url"
      CICDCD_SSO_TENANT="$params.tenant"
      deploymentDescriptor="$params.deploymentDescriptor"
      model="$params.model"
      version="$params.version"
      service="$params.service"
      cookie="$cookie"  
      """
      this.params = params
    }
    void prueba(String Username, String Password) {
      script.echo(Username, Password)
    }
    void login(String Username, String Password) {
      def operation = String.format("./login.sh %s %s %s %s", params.url, Username, Password, params.tenant)
      def result = script.sh(returnStdout: true, script: operation).trim().substring(21)
      cookie = result.substring(0, result.indexOf(';')) 
    }
    void publishApplication(Map params = [:]) {
      script.echo(command)
      // script.sh(returnStdout: true, script: command + " publishApplication")
    }
    void upgradeApplication(Map params = [:]) {
      command = command + """serviceId="$params.serviceId" """
      script.sh(returnStdout: true, script: command + " upgradeApplication")
    }
}