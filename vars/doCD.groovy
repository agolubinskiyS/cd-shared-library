import groovy.json.JsonOutput

def call(Map params = [:], timeoutMinutes = 1){
    def utilities = new tools.Utilities(this, params)
    def j = new groovy.json.JsonOutput

    def scripts = ['cct-api.sh', 'sso_login-2.4.0.sh', 'login_mock.sh', 'login.sh']



    String deploymentDescriptor
    String serviceStatus
    String serviceId
    String serviceDescriptorPath
    String saasPath = 'saas-universe'
    String deploymentDescriptorPath = 'deploymentDescriptor.json'
    // podTemplate(containers: [containerTemplate(name: "curl", image: "dwdraju/alpine-curl-jq", command: "sleep", args: "9999999")]) {
        timeout(time: timeoutMinutes, unit: 'MINUTES') {
        node('cloner') {
            dir('build') {
                stage("Deploy Service on EOS") {
                    loadScripts(scripts)
                    assertParams(params)
                    sh("ls -lha")
                    serviceDescriptorPath = params.serviceDescriptorPath ?: utilities.getLatestJson(saasPath)
                    deploymentDescriptorPath = params.deploymentDescriptorPath ?: deploymentDescriptorPath
                    serviceId = params.serviceId ?: ''

                    def descriptorExists = fileExists "$deploymentDescriptorPath"

                    try {
                        if (params.deploymentDescriptor != null) {
                            deploymentDescriptor = params.deploymentDescriptor
                        }
                        else if (descriptorExists) {
                            deploymentDescriptor = readFile(file:"$deploymentDescriptorPath")
                        }
                        else {
                            if (utilities.isNullOrEmpty(serviceId)) { error "serviceID not provided." }

                        } 
                    }
                    catch (error) {
                        error "Deployment Descriptor not found. Error code: ${err}"
                    }
                    
                    deploymentDescriptor = j.toJson(deploymentDescriptor.replace("\n", "").replace(" ", "").trim())
                    serviceId = utilities.getServiceId(descriptor)

                    String MODULE = 'MODULO'
                    String INTERNAL_VERSION = 'Version'    

                    String image = MODULE + ":" + INTERNAL_VERSION


                    // serviceDescriptorPath = 'car'


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
