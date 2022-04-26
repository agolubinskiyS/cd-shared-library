package cosas

class Utilities implements Serializable {

    def script
    
    Utilities(script) {
        this.script = script
    }

    void deploy(Map params = [:]) {
      script.sh("""./script.sh \
      CICDCD_SSO_URL="$params.url" \
      CICDCD_SSO_USER_ID="$params.user" \
      CICDCD_SSO_USER_PASSWORD="$params.password" \
      CICDCD_SSO_TENANT="$params.tenant" \
      deploymentDescriptor="$params.deploymentDescriptor" \
      model="$params.model" \
      version="$params.version" \
      service="$params.service" """)
    }
    void publishApplication(Map params = [:]) {
      script.sh(""". ./cct_deploy_utils_fat-2.4.0.sh && \
      CICDCD_SSO_URL="$params.url" \ 
      CICDCD_SSO_USER_ID="$params.user" \
      CICDCD_SSO_USER_PASSWORD="$params.password" \
      CICDCD_SSO_TENANT="$params.tenant" \
      publishApplication \
      --deploymentDescriptor "$params.deploymentDescriptor" \ 
      --model "$params.model" \
      --version "$params.version" \
      --service "$params.service" """)
    }
}