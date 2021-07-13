
. server_chassis_version.sh


export LOADER_PATH=../egeria_oracle_datacatalog_connector_package/target/egeria-datacatalog-connector-package-1.0-SNAPSHOT.jar
export LOGGING_LEVEL_ROOT=DEBUG

java -jar server-chassis-spring-${SERVER_CHASSIS_VERSION}.jar

