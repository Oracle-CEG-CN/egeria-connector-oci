ps -ef | grep 'java -jar server-chassis-spring-2.11.jar' | grep -v 'grep'


CURRENT_PROCESS=$(ps -ef | grep 'java -jar server-chassis-spring-2.11.jar' | grep -v 'grep')
echo "Killing current server-chassis process: ${CURRENT_PROCESS}"

CURRENT_PID=$(ps -ef | grep 'java -jar server-chassis-spring-2.11.jar' | awk '{ print $2 }')
echo "Current server-chassis process-id: ${CURRENT_PID}"

kill ${CURRENT_PID}


