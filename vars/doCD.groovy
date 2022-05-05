def call(Map params = [:]){
    def utilities = new tools.Utilities(this, params)
    def toJsonConverter = new tools.ToJsonConverter(this)
    def scripts = ['cct-api.sh', 'sso_login-2.4.0.sh', 'login_mock.sh', 'login.sh']
    String descriptor
    String serviceStatus
    String serviceId
    // podTemplate(containers: [containerTemplate(name: "curl", image: "dwdraju/alpine-curl-jq", command: "sleep", args: "9999999")]) {
        timeout(time: 1, unit: 'MINUTES') {
        node('cloner') {
            dir('build') {
                stage("Deploy Service on EOS") {
                    // timeout(time: 1, unit: 'MINUTES') {
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
                        descriptor = groovy.json.JsonOutput.toJson(descriptor.replace("\n", "").replace(" ", "").trim())

                        println(toJsonConverter.parseJsonText(descriptor))

                        assert params.url ==~ $/http(s)?://.+?/$ : 'unexpected CCT url format'

                        withCredentials([usernamePassword(credentialsId:'cct-api', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                            utilities.login(Username, Password)
                        }
                                            
                        if (utilities.getDeployByServiceDeployId(serviceId) == '200') {
                            utilities.updateService(descriptor, serviceId)
                        } else { 
                            utilities.publishApplication(descriptor) 
                        }
                    // }
                }
           }
        }
        }
    // }
}
