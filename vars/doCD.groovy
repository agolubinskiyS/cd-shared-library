def call(baseUrl, serviceDescriptor, serviceModel, serviceVersion, tenantID){
    def utilities = new cosas.Utilities(this)

    node {
        stage("Build Docker Image") {
            environment {
                CREDS = credentials('c5295ce1-59e7-4849-b18a-9f64a05e4ef2')
            }
            echo CREDS
            loadScript(name: 'script.sh')
            loadScript(name: 'cct_deploy_utils_fat-2.4.0.sh')
            // utilities.prueba('Mensaje')
            // utilities.post(name: 'Nombre')
            utilities.publishApplication(name: 'Nombre')
        }
    }
}