package oracle.odpi.egeria.datacatalog.connector.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

/**
 *
 */
public class DataAssetMapper implements ODCEgeriaMapper {
    
    private final ODCTypeRegistry odcTypeRegistry;
    
    public DataAssetMapper(final ODCTypeRegistry odcTypeRegistry) {
        this.odcTypeRegistry = odcTypeRegistry;
    }

    @Override
    public EntityDetail map(JsonNode odcObject) {
        EntityDetail result = new EntityDetail();
        
        result.setCreateTime(MapperUtil.dateFromJson(odcObject.get("timeCreated").asText()));
        
        return result;
    }
    
}
