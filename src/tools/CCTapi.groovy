package tools
// package src.tools

class CCTapi implements Serializable {

    def script
    String cookie
    String command
    Map params = [:]

    CCTapi(script, params) {
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
      def operation = String.format("./login.sh %s %s %s %s", "$params.url/login", Username, Password, params.apiTenant)
      def result = script.sh(returnStdout: true, script: operation).trim().substring(21)
      cookie = result.substring(0, result.indexOf(';')) 
      if (isNullOrEmpty(cookie)) { throw new RuntimeException("Login failed") }
      command = command + """cookie="$cookie" """
    }
    
    void publishApplication(String deploymentDescriptor) {
      if (isNullOrEmpty(deploymentDescriptor)) { throw new RuntimeException("deploymentDescriptor error") }
      command = command + """deploymentDescriptor=$deploymentDescriptor """
      script.sh(returnStdout: true, script: command + " publishApplication")
    }
    
    void updateService(String deploymentDescriptor, String serviceId) {
      command = command + """serviceId="$serviceId" """ + """deploymentDescriptor=$deploymentDescriptor """
      script.sh(returnStdout: true, script: command + " updateService")
    }

    def getServiceId(String deploymentDescriptor) {
      return script.sh(returnStdout: true, script: "echo $deploymentDescriptor | jq -r .general.serviceId" ).trim().toString()
    }

    def getDeployByServiceDeployId(String serviceId) {
      command = command + """serviceId="$serviceId" """
      return script.sh(returnStdout: true, script: command + " getDeployByServiceDeployId").trim().toString()
    }

    void updateServiceDescriptor(String serviceDescriptor) {
      if (isNullOrEmpty(serviceDescriptor)) { throw new RuntimeException("serviceDescriptor error") }
      // script.sh("""#!/bin/bash  echo $serviceDescriptor""")
      // command = command + """serviceDescriptor=\'${serviceDescriptor}\' """
      script.sh(returnStdout: true, script: "#!/bin/bash " + command + " updateServiceDescriptor")
    }

    void parametrizeImage(String image) {
      command = command + """image=$image """
      script.sh(returnStdout: true, script: "#!/bin/bash " + command + " parametrizeImage")
    }

    def getLatestJson(String saasPath) {
      return saasPath + '/' + script.sh(returnStdout: true, script: "ls " +  saasPath + " -t1 | egrep .json  | head -n 1" ).trim().toString()
    }
    
    // def loadDeploymentDescriptor(String deploymentDescriptorPath) {
    //   def file = new File(deploymentDescriptorPath)
    //   if (params.deploymentDescriptor != null) {
    //     descriptor = params.deploymentDescriptor
    //   }
    //   else if (file.exists()) {
    //     descriptor = file.readFileString(deploymentDescriptorPath)
    //   } else { 
    //     throw new RuntimeException("Deployment Descriptor not found") 
    //   }
    //   return descriptor
    // }
}