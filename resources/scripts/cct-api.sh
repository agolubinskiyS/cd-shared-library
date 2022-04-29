#! /bin/bash

for PARAMETER in "$@"
do
   KEY=$(echo $PARAMETER | cut -f1 -d=)

   KEY_LENGTH=${#KEY}
   VALUE="${PARAMETER:$KEY_LENGTH+1}"

   export "$KEY"="$VALUE"
done

publishApplication() {
   echo "CICDCD_SSO_URL=$CICDCD_SSO_URL"
   echo "CICDCD_SSO_TENANT=$CICDCD_SSO_TENANT"
   echo "deploymentDescriptor=$deploymentDescriptor" 
   echo "model=$model"
   echo "version=$version"
   echo "service=$service"
   echo "cookie=$cookie"
   status_code=$(curl --write-out %{http_code} --silent --output /dev/null -k \
   -X POST $CICDCD_SSO_URL/service/cct-deploy-api/deploy/$service/$model/$version/schema?tenantId=$CICDCD_SSO_TENANT \
   -H "Cookie: dcos-acs-auth-cookie=$cookie" \
   -H 'Content-Type: application/json' \
   -H 'accept: */*' \
   -d "$deploymentDescriptor")

   if [[ "$status_code" -ne 202 ]] ; then
   echo "Error: Deployment status code: $status_code"
   exit 2
   else
   echo "OK! Deployment started"
   exit 0
   fi
}

upgradeApplication() {
   status_code=$(curl --write-out %{http_code} --silent --output /dev/null -k \
   -X PUT $CICDCD_SSO_URL/service/cct-deploy-api/deploy/$service/$model/$version/$serviceId \
   -H "Cookie: dcos-acs-auth-cookie=$cookie" \
   -H 'Content-Type: application/json' \
   -H 'accept: */*' \
   -d "$deploymentDescriptor")

   if [[ "$status_code" -ne 202 ]] ; then
   echo "Error: Deployment status code: $status_code"
   exit 2
   else
   echo "OK! Deployment started"
   exit 0
   fi
}