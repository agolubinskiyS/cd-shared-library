#! /bin/bash

for PARAMETER in "$@"
do
   KEY=$(echo $PARAMETER | cut -f1 -d=)

   KEY_LENGTH=${#KEY}
   VALUE="${PARAMETER:$KEY_LENGTH+1}"

   export "$KEY"="$VALUE"
done

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
   status_code=$(curl --write-out %{http_code} --silent --output /dev/null -k \
   -X POST $CICDCD_SSO_URL/service/cct-deploy-api/deploy/$service/$model/$version/schema?tenantId=$CICDCD_SSO_TENANT \
   -H "Cookie: dcos-acs-auth-cookie=$cookie" \
   -H 'Content-Type: application/json' \
   -H 'accept: */*' \
   -d """$deploymentDescriptor""")
   echo """$deploymentDescriptor""" > /tmp/output
   statusValidate $status_code
}

upgradeApplication() {
   status_code=$(curl --write-out %{http_code} --silent --output /dev/null -k \
   -X PUT $CICDCD_SSO_URL/service/cct-deploy-api/deploy/$service/$model/$version/$serviceId \
   -H "Cookie: dcos-acs-auth-cookie=$cookie" \
   -H 'Content-Type: application/json' \
   -H 'accept: */*' \
   -d "$deploymentDescriptor")
   statusValidate $status_code
}