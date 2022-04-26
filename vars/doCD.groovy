def call(baseUrl, serviceDescriptor, serviceModel, serviceVersion, tenantID){
    echo "deploying service on: ${baseUrl} with: ${serviceDescriptor} model: ${serviceModel} version: ${serviceVersion} tenant: ${tenantID}"

    script {
        def cct = new cosas.Utilities()

        cct.get('myscript')
    }
}