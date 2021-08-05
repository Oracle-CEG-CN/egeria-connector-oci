package oracle.odpi.egeria.datacatalog.connector.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;
import oracle.odpi.egeria.datacatalog.connector.queries.GenericDataCatalogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a mapping between <b>ODC</b> type-keys and the type information from
 * <b>ODC</b> represented as {@code JsonNode} instances.
 */
public final class ODCTypeRegistry {
    
    /**
     * Used for logging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ODCTypeRegistry.class);

    /**
     * Maps type keys to {@code JsonNode} instances.
     */
    private final Map<String, JsonNode> odcTypeMap;

    /**
     * Creates the instance.
     *
     * @param odcTypeMap initialization value for the {@code odcTypeMap}
     * instance.
     */
    public ODCTypeRegistry(final Map<String, JsonNode> odcTypeMap) {
        this.odcTypeMap = odcTypeMap;
    }

    /**
     * Creates an {@code ODCTypeRegistry} instance using the given
     * {@code GenericDataCatalogClient} to read all types from the catalog with
     * the given {@code catalogId}.
     *
     * @param client used to access the <b>Oracle Data Catalog</b> instance.
     * @param catalogId the <b>id</b> of the catalog, for which the type
     * registry should be retrieved.
     * @return an initialized {@code ODCTypeRegistry} instance.
     */
    public static ODCTypeRegistry fromCatalog(
            final GenericDataCatalogClient client, final String catalogId) {
        Map<String, JsonNode> resultMap = new HashMap<>();
        LOGGER.debug("ODCTypeRegistry.fromCatalog(...) retrieving types ...");
        JsonNode types = client.queryCatalog("types", new Object[]{});
        LOGGER.debug("ODCTypeRegistry.fromCatalog(...) retrieving types ... done.");
        types.get("items").elements().forEachRemaining((JsonNode type) -> {
            resultMap.put(type.get("key").asText(), type);
        });
        return new ODCTypeRegistry(resultMap);
    }

    public JsonNode odcTypeFor(final String key) {
        return odcTypeMap.get(key);
    }
}
