package oracle.odpi.egeria.datacatalog.connector;

import java.util.Map;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import com.oracle.bmc.Region;

import com.oracle.bmc.datacatalog.DataCatalogClient;

import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;

public class OracleDataCatalogOMRSRepositoryConnector extends OMRSRepositoryConnector {

    private static final String OCI_CONFIG_USER_ID = "oci-config-user-id";
    private static final String OCI_CONFIG_TENANT_ID = "oci-config-tenant-id";
    private static final String OCI_CONFIG_FINGERPRINT = "oci-config-fingerprint";
    private static final String OCI_CONFIG_REGION = "oci-config-region";

    private DataCatalogClient dataCatalogClient;


    private static final DataCatalogClient createFromConnectionProperties(
            final ConnectionProperties connectionProperties) {
        Map<String, Object> connectionConfigurationProperties = connectionProperties.getConfigurationProperties();
        SimpleAuthenticationDetailsProvider authenticationDetailsProvider;

        authenticationDetailsProvider = SimpleAuthenticationDetailsProvider.builder()
            .userId((String) connectionConfigurationProperties.get(OCI_CONFIG_USER_ID))
            .tenantId((String) connectionConfigurationProperties.get(OCI_CONFIG_TENANT_ID))
            .region(Region.fromRegionCodeOrId((String) connectionConfigurationProperties.get(OCI_CONFIG_REGION)))
            .fingerprint((String) connectionConfigurationProperties.get(OCI_CONFIG_FINGERPRINT))
            .build();

        return null;
    }


    @Override
    public void initialize(
            final String connectorInstanceId,
            final ConnectionProperties connectionProperties) {
    }

    @Override
    public void setMetadataCollectionId(final String metadataCollectionId) {
        super.setMetadataCollectionId(metadataCollectionId);
    }
}

