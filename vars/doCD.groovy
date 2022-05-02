def call(Map params = [:]){
    def utilities = new tools.Utilities(this, params)
    def scripts = ['cct-api.sh', 'sso_login-2.4.0.sh', 'login_mock.sh', 'login.sh']
    def descriptor = ''
    podTemplate(containers: [containerTemplate(name: "curl", image: "dwdraju/alpine-curl-jq", command: "sleep", args: "9999999")]) {
        node(POD_LABEL) {
            // dir('build') {
                stage("Deploy Service on EOS") {
                
                    loadScripts(scripts)
                    sh("pwd")
                    sh("ls -lha")
                    def exists = fileExists 'deploymentDescriptor.json'
                    if (exists) {
                        descriptor = readFile(file:'deploymentDescriptor.json')
                    } else if (params.deploymentDescriptor != null) {
                        descriptor = params.deploymentDescriptor
                    }
                    else {
                        error 'Deployment Descriptor not found'    
                    }

                    assert params.url ==~ $/http(s)?://.+?/$ : 'unexpected CCT url format'

                    withCredentials([usernamePassword(credentialsId:'cct-api', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                        utilities.login(Username, Password)
                    }
                    def escaped_json = groovy.json.JsonOutput.toJson(params.descriptor)
                    utilities.publishApplication(deploymentDescriptor: escaped_json, model: params.model, version: params.version, service: params.service)    

                }
        //    }
        }
    }
}
