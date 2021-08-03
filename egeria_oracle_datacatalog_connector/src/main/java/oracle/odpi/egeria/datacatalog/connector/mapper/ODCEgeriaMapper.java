package oracle.odpi.egeria.datacatalog.connector.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

/**
 * Implementations of this interface are used to map data retrieved from an
 * <b>Oracle Data Catalog</b> to an <b>Egeria</b> {@code EntityDetail}.
 */
public interface ODCEgeriaMapper {

    /**
     * Map the given {@code odcObject} retrieved from an <b>Oracle Data
     * Catalog</b> instance to an <b>Egeria</b> {@code EntityDetail} instance.
     *
     * @param odcObject a {@code JsonNode} retrieved from an <b>Oracle Data
     * Catalog</b> instance.
     * @return an <b>Egeria</b> {@code EntityDetail} instance.
     */
    EntityDetail map(JsonNode odcObject);
}
