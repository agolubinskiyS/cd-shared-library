def call(Map params = [:]){
    def utilities = new cosas.Utilities(this)

    node {
        stage("Deploy Service on EOS") {
            loadScript(name: 'script.sh')
            // loadScript(name: 'cct_deploy_utils_fat-2.4.0.sh')
            
            def exists = fileExists 'deploymentDescriptor.json'
            if (exists) {
                echo 'Yes'
            } else {
                if (params.descriptor != null) {
                    echo 'HAY VARIABLE'
                }
                else {
                    echo 'NO HAY VARIABLE'    
                }
            }


            withCredentials([usernamePassword(credentialsId:'cct-api', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                utilities.deploy(url: params.url, user: Username, password: Password, tenant: params.tenant, deploymentDescriptor: params.descriptor, model: params.model, version: params.version, service: params.service)
                //utilities.publishApplication(url: params.url, user: Username, password: Password, tenant: params.tenant, deploymentDescriptor: params.descriptor, model: params.model, version: params.version, service: params.service)
            }    
        }
    }
}