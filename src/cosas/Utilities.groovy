package cosas

class Utilities implements Serializable {

    // private static final long serialVersionUID
    def script
    

    Utilities(script) {
        this.script = script
    }

    void prueba(String user, String password) {
      // script.echo user password
      script.sh("./script.sh $user $password")
    }
    void post(Map params = [:]) {
      // script.sh("echo ${params.name}")
      // script.echo params.name
      script.sh("""echo \
      CICDCD_SSO_URL=$params.url \
      CICDCD_SSO_USER_ID=$params.user \
      CICDCD_SSO_USER_PASSWORD=$params.password \
      CICDCD_SSO_TENANT=$params.password \
      publishApplication \
      deploymentDescriptor $params.deploymentDescriptor \
      model $params.model \
      version $params.version \
      service $params.service""")
    }
    // void publishApplication(Map params = [:]) {
    //   script.sh(""". ./cct_deploy_utils_fat-2.4.0.sh && \
    //   CICDCD_SSO_URL="${params.url}" \ 
    //   CICDCD_SSO_USER_ID="${params.user}" \
    //   CICDCD_SSO_USER_PASSWORD="${params.password}" \
    //   CICDCD_SSO_TENANT="${params.password}" \
    //   publishApplication \
    //   --deploymentDescriptor "${params.deploymentDescriptor}" \ 
    //   --model "${params.model}" \
    //   --version "${params.version}" \
    //   --service "${params.service}" """)
    // }
}