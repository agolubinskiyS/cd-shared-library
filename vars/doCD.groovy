// def call(baseUrl, serviceDescriptor, serviceModel, serviceVersion, tenantID){
//     echo "deploying service on: ${baseUrl} with: ${serviceDescriptor} model: ${serviceModel} version: ${serviceVersion} tenant: ${tenantID}"

//     def cct = new cosas.Utilities()
//     print(cct.get('myscript'))

// }

def call() {
    node {
        def cct = new cosas.Utilities()
        stage("Build Docker Image") {
            cct.get('myscript')
        }
    }
}