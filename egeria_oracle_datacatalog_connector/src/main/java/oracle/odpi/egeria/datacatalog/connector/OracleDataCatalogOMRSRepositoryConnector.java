package oracle.odpi.egeria.datacatalog.connector;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

//import com.oracle.bmc.ConfigFileReader;
//
//import com.oracle.bmc.auth.AuthenticationDetailsProvider;
//import com.oracle.bmc.auth.SimplePrivateKeySupplier;
//
//import com.oracle.bmc.datacatalog.DataCatalogClient;
//
//import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
//
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleDataCatalogOMRSRepositoryConnector
        extends OMRSRepositoryConnector {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleDataCatalogOMRSRepositoryConnector.class);

//    private DataCatalogClient dataCatalogClient;
//
//    private static final DataCatalogClient createFromDefaultConfig()
//            throws IOException {
//        ConfigFileReader.ConfigFile config = ConfigFileReader.parseDefault();
//
//        SimplePrivateKeySupplier privateKeySupplier
//                = new SimplePrivateKeySupplier(config.get("key_file"));
//
//        AuthenticationDetailsProvider provider
//                = SimpleAuthenticationDetailsProvider.builder()
//                    .tenantId(config.get("tenancy"))
//                    .userId(config.get("user"))
//                    .fingerprint(config.get("fingerprint"))
//                    .privateKeySupplier(privateKeySupplier)
//                    .build();
//
//        return new DataCatalogClient(provider);
//    }

    @Override
    public void initialize(
            final String connectorInstanceId,
            final ConnectionProperties connectionProperties) {
	LOGGER.debug("initialize({}, {})", connectorInstanceId, connectionProperties);
	super.initialize(connectorInstanceId, connectionProperties);
    }
    
    private boolean isInitialized() {
	return true;
    }
    
    @Override
    public void setMetadataCollectionId(final String metadataCollectionId) {
	LOGGER.debug("setMetadataCollectionId({})", metadataCollectionId);
	super.setMetadataCollectionId(metadataCollectionId);
	if (isInitialized()) {
		metadataCollection = new OracleDataCatalogOMRSMetadataCollection(
			this,
		        serverName,
		        repositoryHelper,
		        repositoryValidator,
		        metadataCollectionId);
	}
    }

}

