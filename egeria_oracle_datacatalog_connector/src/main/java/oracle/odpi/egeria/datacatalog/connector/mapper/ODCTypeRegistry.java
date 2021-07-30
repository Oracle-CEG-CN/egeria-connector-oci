package oracle.odpi.egeria.datacatalog.connector.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;
import oracle.odpi.egeria.datacatalog.connector.queries.GenericDataCatalogClient;

/**
 *  Provides a mapping between <b>ODC</b> type-keys and the type information
 *  from <b>ODC</b> represented as {@code JsonNode} instances.
 */
public final class ODCTypeRegistry {
    
    private final Map<String, JsonNode> odcTypeMap;
    public ODCTypeRegistry(final Map<String, JsonNode> odcTypeMap) {
        this.odcTypeMap = odcTypeMap;
    }
    
    public static ODCTypeRegistry fromCatalog(
            final GenericDataCatalogClient client, final String catalogId) {
        Map<String, JsonNode> resultMap = new HashMap<>();
        JsonNode types = client.queryCatalog("catalog/%s/types", new Object[]{ catalogId });
        types.get("items").elements().forEachRemaining((JsonNode type) -> {
            resultMap.put(type.get("key").asText(), type);
        });
        return new ODCTypeRegistry(resultMap);
    }
    
    public JsonNode odcTypeFor(final String key) {
        return odcTypeMap.get(key);
    }
}
