

# Step 0: kill current server

CURRENT_SERVER_CHASSIS_PROCESS_ID=$(ps -ef | grep 'java -jar server-chassis-spring-2.11.jar' | grep -v 'grep' | awk '{ print $2 }')
echo "Current server-chassis process-id: ${CURRENT_SERVER_CHASSIS_PROCESS_ID}"

kill ${CURRENT_SERVER_CHASSIS_PROCESS_ID}


# Step 1: build connector

cd ../egeria_oracle_datacatalog_connector

mvn clean install


# Step 2: build connector package

cd ../egeria_oracle_datacatalog_connector_package

mvn clean install


# Step 3: remove server configuration

cd ../egeria_oracle_datacatalog_connector_run

rm -Rf data

# Step 4: start server

. run_server_chassis.sh  > odc-proxy.log 2>&1 &

# Step 5: configure connector

# cat re_register.commands | ./omag_server_config.py

