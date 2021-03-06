package oracle.odpi.egeria.datacatalog.connector;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.bmc.datacatalog.DataCatalogClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import oracle.odpi.egeria.datacatalog.connector.queries.DataAssetQuery;
import oracle.odpi.egeria.datacatalog.connector.queries.OracleDataCatalogQuery;
import oracle.odpi.egeria.datacatalog.connector.queries.OracleDataCatalogContext;
import oracle.odpi.egeria.datacatalog.connector.queries.GenericDataCatalogClient;
import oracle.odpi.egeria.datacatalog.connector.queries.GenericClientContext;



import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import oracle.odpi.egeria.datacatalog.connector.mapper.ODCEgeriaMapper;
import oracle.odpi.egeria.datacatalog.connector.mapper.ODCEgeriaMapperRegistry;
import oracle.odpi.egeria.datacatalog.connector.mapper.ODCEntityGuid;
import oracle.odpi.egeria.datacatalog.connector.mapper.ODCTypeRegistry;

/**
 *
 */
public final class OracleDataCatalogHelper {

    /**
     * Used for logging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            OracleDataCatalogHelper.class);
    
    private static final Map<String, OracleDataCatalogQuery> typeDefName2QueryMap;

    private static final Set<String> supportedEntityDefNames;
    
    private static final Map<String, String> guidTypeTemplateMap;
    private final ODCEgeriaMapperRegistry odcEgeriaMapperRegistry;
    private final ODCTypeRegistry odcTypeRegistry;

    static {
        supportedEntityDefNames = new HashSet<>();
        supportedEntityDefNames.add("Database");
        supportedEntityDefNames.add("RelationalDBSchemaType");
        supportedEntityDefNames.add("RelationalTableType");
        supportedEntityDefNames.add("RelationalColumnType");
        
        typeDefName2QueryMap = new HashMap<>();
        typeDefName2QueryMap.put("Database", new DataAssetQuery());
        
        guidTypeTemplateMap = new HashMap<>();
        guidTypeTemplateMap.put("data_asset", "dataAssets/%s");
    }

    private final String metadataCollectionId;
    private final DataCatalogClient dataCatalogClient;
    private final GenericDataCatalogClient genericDataCatalogClient;
    private final String catalogId;

    private final OMRSRepositoryHelper repositoryHelper;

    private final Map<String, TypeDef> registeredTypeDefs = new HashMap<>();

    public OracleDataCatalogHelper(
            final String metadataCollectionId,
            final DataCatalogClient dataCatalogClient,
            final GenericDataCatalogClient genericDataCatalogClient,
            final String catalogId,
            final OMRSRepositoryHelper repositoryHelper) {
        this.metadataCollectionId = metadataCollectionId;
        this.dataCatalogClient = dataCatalogClient;
        this.genericDataCatalogClient = genericDataCatalogClient;
        this.catalogId = catalogId;
        this.repositoryHelper = repositoryHelper;
        
        LOGGER.debug("OracleDataCatalogHelper() initializing odcTypeRegistry ...");
        odcTypeRegistry = ODCTypeRegistry.fromCatalog(
                genericDataCatalogClient,
                catalogId);
        LOGGER.debug("OracleDataCatalogHelper() initializing odcTypeRegistry ... done.");

        
        odcEgeriaMapperRegistry = new ODCEgeriaMapperRegistry(
                odcTypeRegistry,
                repositoryHelper,
                metadataCollectionId);
    }

    public boolean isSupportedEntityDef(final String typeName) {
        return supportedEntityDefNames.contains(typeName);
    }

    public void registerTypeDef(final TypeDef typeDef) {
        LOGGER.debug(
                "registerTypeDef([{} {}])",
                typeDef.getCategory().getName(),
                typeDef.getName());
        registeredTypeDefs.put(typeDef.getGUID(), typeDef);
    }

    public boolean isRegisteredType(final TypeDef typeDef) {
        return registeredTypeDefs.containsKey(typeDef.getGUID());
    }
    
    /**
     * This is a helper method to implement {@code OracleDataCatalogOMRSMetadataCollection#findEntities}.
     * After calculating the result from the given parameters, the result is further limited to those
     * {@code EntityDef}s which have been registered before.
     * @param entityTypeGUID the <b>GUID</b> of the root of the type hierarchy, for which to search entities
     * @param entitySubtypeGUIDs if provided: used to limit the subtypes to the given ones; if {@code null} or empty, it will be ignored.
     * @return the list of supported {@code EntityDef} instances
     * @throws TypeErrorException 
     */
    public List<TypeDef> getSupportedEntityTypeDefsFor(
            final String                    entityTypeGUID,
            final List<String>              entitySubtypeGUIDs) throws TypeErrorException {
        final List<TypeDef> result = new ArrayList<>();
        
        final TypeDef searchEntityTypeDef = repositoryHelper.getTypeDef("", "entityTypeGUID", entityTypeGUID, "getSupportedEntityTypeDefsFor");
        result.add(searchEntityTypeDef);
        
        final List<String> searchEntitySubTypeNames = repositoryHelper.getSubTypesOf("", searchEntityTypeDef.getName());
        final List<TypeDef> searchEntitySubTypes = searchEntitySubTypeNames.stream()
                .map(searchEntitySubTypeName -> repositoryHelper.getTypeDefByName("", searchEntitySubTypeName))
                .collect(Collectors.toList());
        
        if (entitySubtypeGUIDs == null || entitySubtypeGUIDs.isEmpty()) {
            result.addAll(searchEntitySubTypes);
        } else {
            result.addAll(searchEntitySubTypes.stream()
                    .filter(t -> entitySubtypeGUIDs.contains(t.getGUID()))
                    .collect(Collectors.toList()));
        }
    
        return result.stream()
                .filter(t -> registeredTypeDefs.containsKey(t.getGUID()))
                .collect(Collectors.toList());
    }

    public OracleDataCatalogQuery getQueryForEntityDef(final TypeDef typeDef) {
        return typeDefName2QueryMap.get(typeDef.getName());
    }
    
    public OracleDataCatalogContext createQueryContext() {
        return new OracleDataCatalogContext(dataCatalogClient, catalogId);
    }
    
    private GenericClientContext clientContextFromGUID(final String guid) {
        LOGGER.debug("clientContextFromGUID({})", guid);
        
        ODCEntityGuid odcEntityGuid = ODCEntityGuid.fromOMRSGuid(guid);
        
        if ((odcEntityGuid != null) && (metadataCollectionId.equals(odcEntityGuid.getMetadataCollectionId()))) {
            final String template = guidTypeTemplateMap.get(odcEntityGuid.getOdcTypeKey());
            final ODCEgeriaMapper mapper = odcEgeriaMapperRegistry.getMapperFor(odcEntityGuid.getOdcTypeKey());

            return new GenericClientContext(template, new Object[] {odcEntityGuid.getOdcKey()}, mapper);
        }
        return null;
    }
    
    public EntityDetail loadEntityByGUID(final String guid) {
        GenericClientContext clientContext = clientContextFromGUID(guid);
        if (clientContext != null) {
            JsonNode jsonNode = genericDataCatalogClient.queryCatalog(
                    clientContext.getQueryTemplate(),
                    clientContext.getQueryArgs());

            return clientContext.getMapper().map(jsonNode);
        }
        return null;
    }

}
