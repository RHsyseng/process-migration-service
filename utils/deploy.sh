export KIE_CONTEXT_ROOT=kie-server/
export KIESERVER_SERVICE_PROTOCOL=http
export KIESERVER_SERVICE_PORT=8080
export KIESERVER_SERVICE_HOST=localhost
export KIE_SERVER_USER=krisv
export KIE_SERVER_PWD=krisv
mvn clean package -Popenshift
java -jar ./target/process-migration-thorntail.jar -Dswarm.port.offset=200

