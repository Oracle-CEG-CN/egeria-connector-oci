package oracle.odpi.egeria.datacatalog.connector;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

public class OracleDataCatalogOMRSRepositoryConnectorProvider
        extends OMRSRepositoryConnectorProviderBase {

    static final String  CONNECTOR_TYPE_GUID = "ffb2a47e-7503-436c-85f1-6dd7bff49607";
    static final String  CONNECTOR_TYPE_NAME = "OMRS Oracle Data Catalog Repository Connector";
    static final String  CONNECTOR_TYPE_DESCRIPTION = "OMRS Oracle Data Catalog Repository Connector that processes events from the Oracle Data Catalog repository store.";

    public OracleDataCatalogOMRSRepositoryConnectorProvider() {
        Class connectorClass = OracleDataCatalogOMRSRepositoryConnector.class;
        super.setConnectorClassName(connectorClass.getName());
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(CONNECTOR_TYPE_GUID);
        connectorType.setQualifiedName(CONNECTOR_TYPE_NAME);
        connectorType.setDisplayName(CONNECTOR_TYPE_NAME);
        connectorType.setDescription(CONNECTOR_TYPE_DESCRIPTION);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        super.connectorTypeBean = connectorType;
    }
}


