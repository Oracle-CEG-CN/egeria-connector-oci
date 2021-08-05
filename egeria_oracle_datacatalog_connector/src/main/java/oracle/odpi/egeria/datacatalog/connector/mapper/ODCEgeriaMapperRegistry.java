package oracle.odpi.egeria.datacatalog.connector.mapper;

import java.util.HashMap;
import java.util.Map;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 *
 */
public final class ODCEgeriaMapperRegistry {
    
    private final Map<String, ODCEgeriaMapper> odcTypeEgeriaMapperMap;
    
    public ODCEgeriaMapperRegistry(
            final ODCTypeRegistry odcTypeRegistry,
            final OMRSRepositoryHelper omrsRepositoryHelper,
            final String metadataCollectionId) {
        odcTypeEgeriaMapperMap = new HashMap<>();
        odcTypeEgeriaMapperMap.put(
                "data_asset",
                new DataAssetMapper(odcTypeRegistry, omrsRepositoryHelper, metadataCollectionId));
    }
    
    public ODCEgeriaMapper getMapperFor(final String type) {
        return odcTypeEgeriaMapperMap.get(type);
    }
}
