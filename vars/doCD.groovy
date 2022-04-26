// def call(baseUrl, serviceDescriptor, serviceModel, serviceVersion, tenantID){
//     echo "deploying service on: ${baseUrl} with: ${serviceDescriptor} model: ${serviceModel} version: ${serviceVersion} tenant: ${tenantID}"

//     def cct = new cosas.Utilities()
//     print(cct.get('myscript'))

// }

def call() {
    def utilities = new cosas.Utilities()
    node {
        stage("Build Docker Image") {
            script {
            utilities.get('Mensaje')
            }
        }
    }
}