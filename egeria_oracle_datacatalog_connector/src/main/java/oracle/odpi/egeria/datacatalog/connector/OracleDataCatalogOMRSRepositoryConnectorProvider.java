package oracle.odpi.egeria.datacatalog.connector;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

public class OracleDataCatalogOMRSRepositoryConnectorProvider
        extends OMRSRepositoryConnectorProviderBase {

    static final String  connectorTypeGUID = "ffb2a47e-7503-436c-85f1-6dd7bff49607";
    static final String  connectorTypeName = "OMRS Oracle Data Catalog Repository Connector";
    static final String  connectorTypeDescription = "OMRS Oracle Data Catalog Repository Connector that processes events from the Oracle Data Catalog repository store.";

    public OracleDataCatalogOMRSRepositoryConnectorProvider() {
        Class connectorClass = OracleDataCatalogOMRSRepositoryConnector.class;
        super.setConnectorClassName(connectorClass.getName());
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        super.connectorTypeBean = connectorType;
    }
}


