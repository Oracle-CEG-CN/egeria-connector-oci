
. server_chassis_version.sh



curl \
        --location \
        --output server-chassis-spring-${SERVER_CHASSIS_VERSION}.jar \
	https://search.maven.org/remotecontent?filepath=org/odpi/egeria/server-chassis-spring/2.11/server-chassis-spring-${SERVER_CHASSIS_VERSION}.jar


curl \
	--location \
	--output truststore.p12 \
	https://github.com/odpi/egeria/blob/master/open-metadata-implementation/server-chassis/server-chassis-spring/src/main/resources/truststore.p12

