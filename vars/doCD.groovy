def call(baseUrl, serviceDescriptor, serviceModel, serviceVersion, tenantID){
    echo "deploying service on: ${baseUrl} with: ${serviceDescriptor} model: ${serviceModel} version: ${serviceVersion} tenant: ${tenantID}"

    def cct = new Utilities()

    cct.get('myscript')
}