curl \
    --insecure \
    --request POST \
    --header "Content-Type: application/json" \
    --data @register_connector.json \
    https://localhost:9443/open-metadata/admin-services/users/odc-proxy-admin/servers/oracle-datacatalog-proxy/local-repository/mode/repository-proxy/connection



