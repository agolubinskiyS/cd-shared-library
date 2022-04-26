package cosas

class Utilities implements Serializable {

    // private static final long serialVersionUID
    def script
    

    Utilities(script) {
        this.script = script
    }

    void prueba(String user, password) {
      script.echo user password
      script.sh("./script.sh $user $password")
    }
    void post(Map params = [:]) {
      script.sh("echo ${params.name}")
      script.echo params.name
    }
    void publishApplication(Map params = [:]) {
      script.sh('. ./cct_deploy_utils_fat-2.4.0.sh && CICDCD_SSO_URL="https://bootstrap.yankee.labs.stratio.com" CICDCD_SSO_USER_ID="admin" CICDCD_SSO_USER_PASSWORD="1234" CICDCD_SSO_TENANT="s000004" publishApplication --deploymentDescriptor ./request.json --model basic --version 11.0.1 --service grafana-eos')
    }
}