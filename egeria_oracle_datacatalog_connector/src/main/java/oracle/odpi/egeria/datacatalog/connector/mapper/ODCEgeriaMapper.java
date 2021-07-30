package oracle.odpi.egeria.datacatalog.connector.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

/**
 *
 */
public interface ODCEgeriaMapper {
    EntityDetail map(JsonNode odcObject);
}
