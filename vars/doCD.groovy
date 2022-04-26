def call(baseUrl, serviceDescriptor, serviceModel, serviceVersion, tenantID){
    def utilities = new cosas.Utilities(this)

    node {
        stage("Build Docker Image") {
            loadScript(name: 'script.sh')
            utilities.prueba('Mensaje')
            utilities.post(name: 'Nombre')
            utilities.publishApplication(name: 'Nombre')
        }
    }
}