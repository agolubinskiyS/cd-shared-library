#! /bin/bash

for PARAMETER in "$@"
do
   KEY=$(echo $PARAMETER | cut -f1 -d=)

   KEY_LENGTH=${#KEY}
   VALUE="${PARAMETER:$KEY_LENGTH+1}"

   export "$KEY"="$VALUE"
done

curlAction="curl --write-out %{http_code} --silent --output /dev/null -k"
cctUrlResources="service/cct-deploy-api"

statusValidate(){  
   if [ "$1" -ne 202 ] ; then
      echo "Error: Deployment status code: $status_code"
      exit 2
   else
      echo "OK! Deployment started"
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

# getDeployByServiceDeployId() {
#   status_code=$($curlAction \
#    -X GET $CICDCD_SSO_URL/$cctUrlResources/update/$serviceId?version=$version \
#    -H "Cookie: dcos-acs-auth-cookie=$cookie" \
#    -H 'accept: */*')
#    echo "Status code: $status_code"
# }

getDeployByServiceDeployId() {
   echo "200"
}