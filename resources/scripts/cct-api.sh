#!/bin/bash

for PARAMETER in "$@"
do
   KEY=$(echo $PARAMETER | cut -f1 -d=)

   KEY_LENGTH=${#KEY}
   VALUE="${PARAMETER:$KEY_LENGTH+1}"

   export "$KEY"="$VALUE"
done

curlAction="curl --write-out %{http_code} --silent --output /dev/null -k"
cctUrlResources="service/cct-deploy-api"
cctUrlUniverse="service/cct-universe/v1"

statusValidate(){  
   if [ "$1" -ne 202 ] ; then
      echo "Error: status code: $status_code"
      exit 2
   else
      echo "Done"
      exit 0
   fi
}

publishApplication() {
   status_code=$($curlAction \
   -X POST $CICDCD_SSO_URL/$cctUrlResources/deploy/$service/$model/$version/schema?tenantId=$CICDCD_SSO_TENANT \
   -H "Cookie: dcos-acs-auth-cookie=$cookie" \
   -H 'Content-Type: application/json' \
   -H 'accept: */*' \
   -d "$deploymentDescriptor")
   statusValidate $status_code
}

updateService() {
   status_code=$($curlAction \
   -X PUT $CICDCD_SSO_URL/$cctUrlResources/update/$serviceId?version=$version \
   -H "Cookie: dcos-acs-auth-cookie=$cookie" \
   -H 'Content-Type: application/json' \
   -H 'accept: */*' \
   -d "$deploymentDescriptor")
   statusValidate $status_code
}

getDeployByServiceDeployId() {
  status_code=$($curlAction \
   -X GET $CICDCD_SSO_URL/$cctUrlResources/update/$serviceId?version=$version \
   -H "Cookie: dcos-acs-auth-cookie=$cookie" \
   -H 'accept: */*')
   echo "$status_code"
}

# updateServiceDescriptor() {
#    status_code=$($curlAction \
#    -X PUT $CICDCD_SSO_URL/$cctUrlUniverse/descriptors/$service/$model/$version \
#    -H "Cookie: dcos-acs-auth-cookie=$cookie" \
#    -H 'Content-Type: application/json' \
#    -H 'accept: */*' \
#    -d $(cat service-descriptor.json))
#    statusValidate $status_code
# }

updateServiceDescriptor() {
   echo $(cat saas-universe/maintenance-core-default.json | tr -d '\n' | tr -d ' ' ) > /tmp/output2
}

prueba() {
   status_code=$($curlAction \
   -X PUT url \
   -H 'Content-Type: application/json' \
   -H 'accept: */*' \
   -d """$serviceDescriptor""")
   echo $serviceDescriptor
}

parametrizeImage() {
   tmp=$(mktemp)
   jq --arg i '${eos.dockerRegistry}/'$image '.data.container.runners[0].image = $i' \
   saas-universe/maintenance-core-default.json > "$tmp" && mv "$tmp" saas-universe/maintenance-core-default.json
}

