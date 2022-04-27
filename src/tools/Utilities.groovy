package tools

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
      """
      this.params = params
    }
    void login(String Password, String Username) {
      def operation = String.format("./login.sh %s %s %s %s", params.url, Username, Password, params.tenant)
      def result = script.sh(returnStdout: true, script: operation).trim().substring(21)
      cookie = result.substring(0, result.indexOf(';')) 
    }
    void publishApplication(Map params = [:]) {
      command = command + 
      """
      deploymentDescriptor="$params.deploymentDescriptor"
      model="$params.model"
      version="$params.version"
      service="$params.service"
      cookie="$cookie"         
      """
      command = command.replaceAll("(\\t|\\r?\\n)+", " ")
      script.echo(command)
      script.sh(returnStdout: true, script: command + " publishApplication")
    }
}