package oracle.odpi.egeria.datacatalog.connector;

import java.util.Map;

import java.io.IOException;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;

import com.oracle.bmc.datacatalog.DataCatalogClient;

import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;

public class OracleDataCatalogOMRSRepositoryConnector
        extends OMRSRepositoryConnector {

    private DataCatalogClient dataCatalogClient;

    private static final DataCatalogClient createFromDefaultConfig()
            throws IOException {
        ConfigFileReader.ConfigFile config = ConfigFileReader.parseDefault();

        SimplePrivateKeySupplier privateKeySupplier
                = new SimplePrivateKeySupplier(config.get("key_file"));

        AuthenticationDetailsProvider provider
                = SimpleAuthenticationDetailsProvider.builder()
                    .tenantId(config.get("tenancy"))
                    .userId(config.get("user"))
                    .fingerprint(config.get("fingerprint"))
                    .privateKeySupplier(privateKeySupplier)
                    .build();

        return new DataCatalogClient(provider);
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

