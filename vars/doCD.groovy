def call(Map params = [:]){
    def utilities = new cosas.Utilities(this)
    def descriptor = ''
    node {
        stage("Deploy Service on EOS") {
            loadScript(name: 'script.sh')
            loadScript(name: 'sso_login-2.4.0.sh')
            loadScript(name: 'login_mock.sh')
            loadScript(name: 'login.sh')
            // loadScript(name: 'cct_deploy_utils_fat-2.4.0.sh')
            
            def exists = fileExists 'deploymentDescriptor.json'
            if (exists) {
                descriptor = readFile(file:'deploymentDescriptor.json')
            } else if (params.deploymentDescriptor != null) {
                descriptor = params.deploymentDescriptor
            }
            else {
                error 'Deployment Descriptor not found'    
            }

            withCredentials([usernamePassword(credentialsId:'cct-api', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                utilities.deploy(url: params.url, user: Username, password: Password, tenant: params.tenant, deploymentDescriptor: descriptor, model: params.model, version: params.version, service: params.service)
                //utilities.publishApplication(url: params.url, user: Username, password: Password, tenant: params.tenant, deploymentDescriptor: params.descriptor, model: params.model, version: params.version, service: params.service)
            }    
        }
    }
}