package oracle.odpi.egeria.datacatalog.connector;

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

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

/**
 *
 */
public final class OracleDataCatalogHelper {

    /**
     * Used for logging.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            OracleDataCatalogHelper.class);

    private static final Set<String> supportedEntityDefNames;

    static {
        supportedEntityDefNames = new HashSet<>();
        supportedEntityDefNames.add("Database");
        supportedEntityDefNames.add("RelationalDBSchemaType");
        supportedEntityDefNames.add("RelationalTableType");
        supportedEntityDefNames.add("RelationalColumnType");
    }

    private final DataCatalogClient dataCatalogClient;
    private final String catalogId;

    private final OMRSRepositoryHelper repositoryHelper;

    private final Map<String, TypeDef> registeredTypeDefs = new HashMap<>();

    public OracleDataCatalogHelper(
            final DataCatalogClient dataCatalogClient,
            final String catalogId,
            final OMRSRepositoryHelper repositoryHelper) {
        this.dataCatalogClient = dataCatalogClient;
        this.catalogId = catalogId;
        this.repositoryHelper = repositoryHelper;
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

}
