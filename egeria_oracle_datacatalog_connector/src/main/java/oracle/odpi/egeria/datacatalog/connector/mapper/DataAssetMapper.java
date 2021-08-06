package oracle.odpi.egeria.datacatalog.connector.mapper;

import com.fasterxml.jackson.databind.JsonNode;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

/**
 *
 */
public class DataAssetMapper implements ODCEgeriaMapper {
    
    private final ODCTypeRegistry odcTypeRegistry;
    private final OMRSRepositoryHelper omrsRepositoryHelper;
    private final String metadataCollectionId;
    
    public DataAssetMapper(
            final ODCTypeRegistry odcTypeRegistry,
            final OMRSRepositoryHelper omrsRepositoryHelper,
            final String metadataCollectionId) {
        this.odcTypeRegistry = odcTypeRegistry;
        this.omrsRepositoryHelper = omrsRepositoryHelper;
        this.metadataCollectionId = metadataCollectionId;
    }

    @Override
    public EntityDetail map(JsonNode odcObject) {
        try {
            EntityDetail result = omrsRepositoryHelper.getSkeletonEntity(
                    "DataAssetMapper",
                    metadataCollectionId,
                    omrsRepositoryHelper.getMetadataCollectionName(metadataCollectionId),
                    InstanceProvenanceType.EXTERNAL_SOURCE,
                    "",
                    "Database");
            
            result.setCreateTime(MapperUtil.dateFromJson(odcObject.get("timeCreated").asText()));
            
            return result;
        } catch (TypeErrorException tee) {
            throw new RuntimeException(tee);
        }
    }
    
}
