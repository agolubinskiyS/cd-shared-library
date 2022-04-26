#! /bin/bash

# curl -k -X POST https://bootstrap.yankee.labs.stratio.com/service/cct-deploy-api/deploy/grafana/adminrouter/11.0.1/schema?tenantId=s000004 \
# -H 'Cookie: dcos-acs-auth-cookie=eyJhbGciOiJIUzI1NiIsImtpZCI6InNlY3JldCIsInR5cCI6IkpXVCJ9.eyJjbiI6ImFkbWluIiwiZXhwIjoxNjUwOTA3NzA2LCJncm91cHMiOlsiYWRtaW4iLCJzdHJhdGlvIiwibWFuYWdlcl9pbnN0YWxsZXIiLCJtYW5hZ2VyX2FkbWluIiwiczAwMDAwMi1TdXBlckFkbWluLWdvdmVybmFuY2UiLCJzMDAwMDAyLWdvdmVybmFuY2Utc3VwZXItYWRtaW5zIiwiczAwMDAwMy1TdXBlckFkbWluLWdvdmVybmFuY2UiLCJzMDAwMDAzLWdvdmVybmFuY2Utc3VwZXItYWRtaW5zIiwibWRtX2FkbWluIiwiczAwMDAwMy1kaXNjb3ZlcnktYWRtaW4iXSwibWFpbCI6ImFkbWluQHlhbmtlZS5sYWJzLnN0cmF0aW8uY29tIiwidGVuYW50IjoiTk9ORSIsInVpZCI6ImFkbWluIn0.IosEt1H7-68zIh8gIXInoE7JruELNx8gqDGAcnbTrlY' \
# -H 'Content-Type: application/json' \
# -H 'accept: */*' \
# -d '{"general":{"serviceId":"s000004/s000004-grafana-prueba","appname":"grafana-prueba","grafanaAdminUser":"admin","identity":{"approlename":"s000004"},"network":{"networkName":"s000004-core"},"resources":{"INSTANCES":1,"CPUs":1,"MEM":1024}},"settings":{"logs":{"nginxLogLevel":"error"}},"placement":{"marathonConstraintSection":{"marathonConstraintName":"","marathonConstraintOperator":"","marathonConstraintValue":""}},"environment":{"grafanaConsulDomain":"${eos.internalDomain}","grafanaSSOURI":"${globals.sso.ssoUri}","vault":{"vaultHosts":"vault.service.eos.yankee.labs.stratio.com","vaultPort":8200}}}'


print "USER IS ${1} PASSWORD is ${2}"