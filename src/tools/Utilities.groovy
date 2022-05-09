package tools
// package src.tools

class Utilities implements Serializable {

    def script
    String command

    Utilities(script) {
      this.script = script
      command = ""
    }

    boolean isNullOrEmpty(String str) { return (str == null || str.allWhitespace) }


    def getServiceId(String deploymentDescriptor) {
      return script.sh(returnStdout: true, script: "echo $deploymentDescriptor | jq -r .general.serviceId" ).trim().toString()
    }

    void parametrizeImage(String image) {
      command = command + """image=$image """
      script.sh(returnStdout: true, script: "#!/bin/bash " + command + " parametrizeImage")
    }

    def getLatestJson(String saasPath) {
      return saasPath + '/' + script.sh(returnStdout: true, script: "ls " +  saasPath + " -t1 | egrep .json  | head -n 1" ).trim().toString()
    }
    
}