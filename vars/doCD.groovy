def call(Map params = [:], timeoutMinutes = 1){
    def api = new tools.CCTapi(this, params)
    def utils = new tools.Utilities(this)
    def p = new tools.RecursiveJson()
    def scripts = ['cct-api.sh', 'sso_login-2.4.0.sh', 'login_mock.sh', 'login.sh']



    Integer retries = 3
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
                    serviceDescriptorPath = params.serviceDescriptorPath ?: utils.getLatestJson(saasPath)
                    deploymentDescriptorPath = params.deploymentDescriptorPath ?: deploymentDescriptorPath
                    

                    def descriptorExists = fileExists "$deploymentDescriptorPath"

              
                    if (params.deploymentDescriptor != null) {
                        deploymentDescriptor = params.deploymentDescriptor     
                    }
                    else if (descriptorExists) {
                        deploymentDescriptor = readFile(file:"$deploymentDescriptorPath")
                    }
                    else {
                        serviceId = params.serviceId 
                        deploymentDescriptor = getDeploymentDescriptor(serviceId)
                    } 
                   
                    deploymentDescriptor =  deploymentDescriptor.replace("\n", "").replace(" ", "").trim() 

                    writeFile(file: 'nuevo-descriptor.json', text: deploymentDescriptor)
                    // serviceId = serviceId ?: utils.getServiceId(deploymentDescriptor)
                    serviceId = 's00s'
                    // deploymentDescriptor = groovy.json.JsonOutput.toJson(deploymentDescriptor.replace("\n", "").replace(" ", "").trim())
                    // print(p.runParseJson(serviceDescriptorPath, deploymentDescriptorPath))
                    println(serviceId)
                    serviceDescriptorPath = '/var/jenkins_home/workspace/CCT-prueba/build/saas-universe/maintenance-core-default.json'

                    // def serviceDescriptor = readJSON file: "$serviceDescriptorPath"
                    print(p.runParseJson(serviceDescriptorPath, '/var/jenkins_home/workspace/CCT-prueba/build/nuevo-descriptor.json'))
                    // String MODULE = 'MODULO'
                    // String INTERNAL_VERSION = 'Version'    

                    // String image = MODULE + ":" + INTERNAL_VERSION


                    // serviceDescriptorPath = 'car'


                    // api.parametrizeImage(MODULE + ":" + INTERNAL_VERSION)    

                    // def exists = fileExists 'deploymentDescriptor.json'
                    // if (exists) {
                    //     descriptor = readFile(file:'deploymentDescriptor.json')
                    // } else if (params.deploymentDescriptor != null) {
                    //     descriptor = params.deploymentDescriptor
                    // }
                    // else {
                    //     error 'Deployment Descriptor not found'    
                    // }
                    // api.updateServiceDescriptor(serviceDescriptor)
                    // descriptor = groovy.json.JsonOutput.toJson(descriptor.replace("\n", "").replace(" ", "").trim())
                    // serviceId = api.getServiceId(descriptor)
                    
                    
                    // withCredentials([usernamePassword(credentialsId:'CREDENTIALS_SOLUTIONS_YANKEE_CCT-API', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                    //     api.login(Username, Password)
                    // }

                    // api.updateServiceDescriptor(serviceDescriptor)

                    // if (api.getDeployByServiceDeployId(serviceId) == '200') {
                    //     api.updateService(descriptor, serviceId)
                    // } else { 
                    //     api.publishApplication(descriptor) 
                    // }
                }
                // stage("get status") {
                //     api.getServiceStatus(serviceId, 4, 1)
                // }
           }
        }
        }
    // }
}
