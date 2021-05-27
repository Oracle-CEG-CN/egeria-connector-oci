package oracle.odpi.egeria.datacatalog.connector;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


public class OracleDataCatalogOMRSRepositoryConnector extends OMRSRepositoryConnector {

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

