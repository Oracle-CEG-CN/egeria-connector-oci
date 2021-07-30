package oracle.odpi.egeria.datacatalog.connector.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class ODCEgeriaMapperRegistry {
    
    private final Map<String, ODCEgeriaMapper> odcTypeEgeriaMapperMap;
    
    public ODCEgeriaMapperRegistry(final ODCTypeRegistry odcTypeRegistry) {
        odcTypeEgeriaMapperMap = new HashMap<>();
        odcTypeEgeriaMapperMap.put("data_asset", new DataAssetMapper(odcTypeRegistry));
    }
    
    public ODCEgeriaMapper getMapperFor(final String type) {
        return odcTypeEgeriaMapperMap.get(type);
    }
}
