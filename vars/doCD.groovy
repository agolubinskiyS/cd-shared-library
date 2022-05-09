def call(Map params = [:]){
    def utilities = new tools.Utilities(this, params)
    
    def scripts = ['cct-api.sh', 'sso_login-2.4.0.sh', 'login_mock.sh', 'login.sh']
    String descriptor
    String serviceStatus
    String serviceId
    String serviceDescriptorPath = 'cra'
    // podTemplate(containers: [containerTemplate(name: "curl", image: "dwdraju/alpine-curl-jq", command: "sleep", args: "9999999")]) {
        timeout(time: 1, unit: 'MINUTES') {
        node('cloner') {
            dir('build') {
                stage("Deploy Service on EOS") {
                    loadScripts(scripts)
                    assertParams(params)
                    println(params)
                    // for (entry in params) {
                    //     println "KEY: $entry.key = Value: $entry.value"
                    // }
                    serviceDescriptor = readFile(file: 'saas-universe/maintenance-core-default.json')
                    serviceDescriptor = groovy.json.JsonOutput.toJson(serviceDescriptor.replace("\n", "").replace(" ", "").trim())

                    String MODULE = 'MODULO'
                    String INTERNAL_VERSION = 'Version'    

                    String image = MODULE + ":" + INTERNAL_VERSION



                    serviceDescriptorPath = serviceDescriptorPath ? serviceDescriptorPath : 'algo'

                    println(serviceDescriptorPath)
                    // utilities.parametrizeImage(MODULE + ":" + INTERNAL_VERSION)    

                    // def exists = fileExists 'deploymentDescriptor.json'
                    // if (exists) {
                    //     descriptor = readFile(file:'deploymentDescriptor.json')
                    // } else if (params.deploymentDescriptor != null) {
                    //     descriptor = params.deploymentDescriptor
                    // }
                    // else {
                    //     error 'Deployment Descriptor not found'    
                    // }
                    // utilities.updateServiceDescriptor(serviceDescriptor)
                    // descriptor = groovy.json.JsonOutput.toJson(descriptor.replace("\n", "").replace(" ", "").trim())
                    // serviceId = utilities.getServiceId(descriptor)
                    
                    
                    // withCredentials([usernamePassword(credentialsId:'CREDENTIALS_SOLUTIONS_YANKEE_CCT-API', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                    //     utilities.login(Username, Password)
                    // }

                    // utilities.updateServiceDescriptor(serviceDescriptor)

                    // if (utilities.getDeployByServiceDeployId(serviceId) == '200') {
                    //     utilities.updateService(descriptor, serviceId)
                    // } else { 
                    //     utilities.publishApplication(descriptor) 
                    // }
                }
           }
        }
        }
    // }
}
