def call(baseUrl, serviceDescriptor, serviceModel, serviceVersion, tenantID){
    def utilities = new cosas.Utilities(this)

    node {
        stage("Build Docker Image") {
            loadScript(name: 'script.sh')
            utilities.get('Mensaje')
        }
    }
}