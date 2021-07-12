This directory is used to run our Oracle Data-Catalog proxy
within an OMAG server instance.

From within different scripts we source the file "server_chassis_version.sh" to
specify the version of the server-chassis jar file.

We need to download the appropriate server-chassis jar file 
from maven using script "download_server_chassis.sh"

The script "run_server_chassis.sh" starts an OMAG server with an appropriate
setting of the "LOADER_PATH" environment variable to include our connector in
classpath.

