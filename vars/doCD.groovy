def call(Map params = [:]){
    def utilities = new cosas.Utilities(this)

    node {
        stage("Deploy Service on EOS") {
            loadScript(name: 'script.sh')
            loadScript(name: 'cct_deploy_utils_fat-2.4.0.sh')
            withCredentials([usernamePassword(credentialsId:'cct-api', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                utilities.deploy(url: params.url, user: Username, password: Password, deploymentDescriptor: params.descriptor, model: params.model, version: params.version, service: params.service)
                //utilities.publishApplication(name: 'Nombre')
            }    
        }
    }
}