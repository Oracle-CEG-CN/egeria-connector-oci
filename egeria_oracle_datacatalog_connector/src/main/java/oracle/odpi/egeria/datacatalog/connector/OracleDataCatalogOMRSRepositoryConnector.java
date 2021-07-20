package oracle.odpi.egeria.datacatalog.connector;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.StringPrivateKeySupplier;

import com.oracle.bmc.datacatalog.DataCatalogClient;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.Map;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleDataCatalogOMRSRepositoryConnector
        extends OMRSRepositoryConnector {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(
		    OracleDataCatalogOMRSRepositoryConnector.class);

    private DataCatalogClient dataCatalogClient = null;
    private String catalogId = null;
    
    private static DataCatalogClient createDataCatalogClient(
		final Map<String, Object> configurationProperties) {
	if (configurationProperties == null) {
		LOGGER.warn("ConfigurationProperties to create DataCatalogClient are missing.");
		return null;
	}
	final String apiKey = (String) configurationProperties.get("api_key");
	final String tenancy = (String) configurationProperties.get("tenancy");
	final String user = (String) configurationProperties.get("user");
	final String fingerprint = (String) configurationProperties.get("fingerprint");
	
	if ((apiKey == null) || (tenancy == null) || (user == null) || (fingerprint == null)) {
		StringBuilder sb = new StringBuilder();
		boolean addComma = false;
		sb.append("ConfigurationProperties: ");
		if (apiKey == null) {
			sb.append("api_key");
			addComma = true;
		}
		if (tenancy == null) {
			if (addComma) {
				sb.append(", ");
			}
			sb.append("tenancy");
			addComma = true;
		}
		if (user == null) {
			if (addComma) {
				sb.append(", ");
			}
			sb.append("user");
			addComma = true;
		}
		if (fingerprint == null) {
			if (addComma) {
				sb.append(", ");
			}
			sb.append("fingerprint");
		}
		sb.append(" to create DataCatalogClient are missing.");
		LOGGER.warn(sb.toString());
		return null;
	}
	StringPrivateKeySupplier privateKeySupplier = new StringPrivateKeySupplier(apiKey);

	AuthenticationDetailsProvider provider
		= SimpleAuthenticationDetailsProvider.builder()
		    .tenantId(tenancy)
		    .userId(user)
		    .fingerprint(fingerprint)
		    .privateKeySupplier(privateKeySupplier)
		    .build();

        return new DataCatalogClient(provider);
    }

    @Override
    public void initialize(
            final String connectorInstanceId,
            final ConnectionProperties connectionProperties) {
	LOGGER.debug("initialize({}, {})", connectorInstanceId, connectionProperties);
	super.initialize(connectorInstanceId, connectionProperties);
	
	dataCatalogClient = createDataCatalogClient(
			connectionProperties.getConfigurationProperties());
	if (connectionProperties.getConfigurationProperties() != null) {
		catalogId = (String) connectionProperties.getConfigurationProperties().get("catalog_id");
	}
    }
    
    private boolean isInitialized() {
	return (dataCatalogClient != null) && (catalogId != null);
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
		        metadataCollectionId,
			new OracleDataCatalogHelper(dataCatalogClient, catalogId, repositoryHelper));
	}
    }

}

