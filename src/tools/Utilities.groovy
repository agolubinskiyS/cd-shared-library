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
      model="$params.model"
      version="$params.version"
      service="$params.service"
      """
      this.params = params
    }
    boolean isNullOrEmpty(String str) { return (str == null || str.allWhitespace) }

    void login(String Username, String Password) {
      def operation = String.format("./login.sh %s %s %s %s", params.url, Username, Password, params.tenant)
      def result = script.sh(returnStdout: true, script: operation).trim().substring(21)
      cookie = result.substring(0, result.indexOf(';')) 
      if (isNullOrEmpty(cookie)) { throw new RuntimeException("Login failed") }
      command = command + """cookie="$cookie" """
    }
    
    void publishApplication(String deploymentDescriptor) {
      command = command + """deploymentDescriptor=$deploymentDescriptor """
      def output = script.sh(returnStdout: true, script: command + " publishApplication")
      script.echo(params.deploymentDescriptor)
      script.echo(command)
    }
    
    void upgradeApplication(Map params = [:]) {
      command = command + """serviceId="$params.serviceId" """
      script.sh(returnStdout: true, script: command + " upgradeApplication")
    }
}