export KIE_CONTEXT_ROOT=
export KIESERVER_SERVICE_PROTOCOL=https
export KIESERVER_SERVICE_PORT=443
export KIESERVER_SERVICE_HOST=rhpam-authoring-kieserver-rhpam-ruben.rhdp.ocp.cloud.lab.eng.bos.redhat.com
export KIE_SERVER_USER=adminUser
export KIE_SERVER_PWD=UJI87Awy
mvn clean package -Popenshift
java -jar ./target/process-migration-service-1.0.0-SNAPSHOT-thorntail.jar -Dswarm.port.offset=200

