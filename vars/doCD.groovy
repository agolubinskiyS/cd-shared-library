def call(baseUrl, serviceDescriptor, serviceModel, serviceVersion, tenantID){
    def utilities = new cosas.Utilities(this)

    node {
        stage("Deploy Service on EOS") {
            loadScript(name: 'script.sh')
            loadScript(name: 'cct_deploy_utils_fat-2.4.0.sh')
            // utilities.prueba('Mensaje')
            // utilities.post(name: 'Nombre')
            withCredentials([usernamePassword(credentialsId:'c5295ce1-59e7-4849-b18a-9f64a05e4ef2', passwordVariable: 'Password', usernameVariable: 'Username')]) {
                utilities.prueba("${Username}", "${Password}")
                //utilities.publishApplication(name: 'Nombre')
            }    
        }
    }
}