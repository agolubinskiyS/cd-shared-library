package cosas

class Utilities implements Serializable {

    def script
    
    Utilities(script) {
        this.script = script
    }

    void deploy(Map params = [:]) {
      login(params)
      publishApplication(params)
    }
    def login(Map params = [:]) {
      def result = script.sh(""". ./login_mock.sh && \
      CICDCD_SSO_URL="$params.url" \
      CICDCD_SSO_USER_ID="$params.user" \
      CICDCD_SSO_USER_PASSWORD="$params.password" \
      CICDCD_SSO_TENANT="$params.tenant" \    
      """)     
    }
    void publishApplication(Map params = [:]) {
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
}