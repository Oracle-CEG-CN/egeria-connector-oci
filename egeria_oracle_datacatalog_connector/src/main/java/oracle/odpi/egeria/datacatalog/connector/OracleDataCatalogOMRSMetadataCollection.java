package oracle.odpi.egeria.datacatalog.connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import oracle.odpi.egeria.datacatalog.connector.queries.OracleDataCatalogQuery;
import oracle.odpi.egeria.datacatalog.connector.queries.OracleDataCatalogContext;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidTypeDefException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class OracleDataCatalogOMRSMetadataCollection extends OMRSMetadataCollectionBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			OracleDataCatalogOMRSMetadataCollection.class);
	
	private final OracleDataCatalogHelper oracleDataCatalogHelper;
	
	public OracleDataCatalogOMRSMetadataCollection(
			OMRSRepositoryConnector parentConnector,
			String repositoryName,
			OMRSRepositoryHelper repositoryHelper,
			OMRSRepositoryValidator repositoryValidator,
			String metadataCollectionId,
			final OracleDataCatalogHelper oracleDataCatalogHelper) {
		super(parentConnector, repositoryName, repositoryHelper,
				repositoryValidator, metadataCollectionId);
		this.oracleDataCatalogHelper = oracleDataCatalogHelper;
	}
	
	@Override
	public boolean verifyTypeDef(final String userId, final TypeDef typeDef)
			throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException {
		LOGGER.debug("verifyTypeDef({}, {})", userId, typeDef.getName());
		
		return oracleDataCatalogHelper.isRegisteredType(typeDef);
	}
	
	@Override
	public void addTypeDef(final String userId, final TypeDef typeDef)
			throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefKnownException, TypeDefConflictException, InvalidTypeDefException, FunctionNotSupportedException, UserNotAuthorizedException {
		LOGGER.debug("addTypeDef({}, {} - {})", userId, typeDef.getCategory().getName(), typeDef.getName());
		
		switch (typeDef.getCategory()) {
			case ENTITY_DEF:
				if (oracleDataCatalogHelper.isSupportedEntityDef(typeDef.getName())) {
					oracleDataCatalogHelper.registerTypeDef(typeDef);
					return;
				}
				break;
		}
		throw new TypeDefNotSupportedException(
			new ExceptionMessageDefinition(400, "typenotsupported", "template", "ignore me", "ignore me"),
			OracleDataCatalogOMRSMetadataCollection.class.getName(),
			"ignore me");
	}
	
	@Override
	public boolean verifyAttributeTypeDef(
			final String userId,
			final AttributeTypeDef attributeTypeDef)
			throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException {
		LOGGER.debug("verifyAttributeTypeDef({}, {} - {})", userId, attributeTypeDef.getCategory().getName(), attributeTypeDef.getName());
		return false;
	}

	@Override
	public void addAttributeTypeDef(String userId, AttributeTypeDef newAttributeTypeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefKnownException, TypeDefConflictException, InvalidTypeDefException, FunctionNotSupportedException, UserNotAuthorizedException {
		LOGGER.debug("addAttributeTypeDef({}, {} - {})",
				userId,
				newAttributeTypeDef.getCategory().getName(),
				newAttributeTypeDef.getName());
		throw new TypeDefNotSupportedException(
			new ExceptionMessageDefinition(400, "attributetypedefnotsupported", "template", "ignore me", "ignore me"),
			OracleDataCatalogOMRSMetadataCollection.class.getName(),
			"ignore me");
	}

    @Override
    public List<EntityDetail> findEntities(
            final String                    userId,
            final String                    entityTypeGUID,
            final List<String>              entitySubtypeGUIDs,
            final SearchProperties          matchProperties,
            final int                       fromEntityElement,
            final List<InstanceStatus>      limitResultsByStatus,
            final SearchClassifications     matchClassifications,
            final Date                      asOfTime,
            final String                    sequencingProperty,
            final SequencingOrder           sequencingOrder,
            final int                       pageSize) throws InvalidParameterException,
                    RepositoryErrorException,
                    TypeErrorException,
                    PropertyErrorException,
                    PagingErrorException,
                    FunctionNotSupportedException,
                    UserNotAuthorizedException
    {
        final String  methodName                   = "findEntities";

        /*
         * Validate parameters
         */
        this.findEntitiesParameterValidation(userId,
                                             entityTypeGUID,
                                             entitySubtypeGUIDs,
                                             matchProperties,
                                             fromEntityElement,
                                             limitResultsByStatus,
                                             matchClassifications,
                                             asOfTime,
                                             sequencingProperty,
                                             sequencingOrder,
                                             pageSize);
        
        List<TypeDef> supportedEntityDefs = oracleDataCatalogHelper
                .getSupportedEntityTypeDefsFor(entityTypeGUID,
                        entitySubtypeGUIDs);
        
        List<EntityDetail> result = new ArrayList<>();
        
        supportedEntityDefs.stream()
                .map(typeDef -> oracleDataCatalogHelper.getQueryForEntityDef(typeDef))
                .filter(Objects::nonNull)
                .map(query -> {
                    OracleDataCatalogContext context = oracleDataCatalogHelper.createQueryContext();
                    List<EntityDetail> queryResult = query.queryCatalog(context);
                    return queryResult;
                })
                .forEachOrdered(queryResult -> {
                    result.addAll(queryResult);
                });
        
        return result;
    }
    
    @Override
    public EntityDetail getEntityDetail(
            final String userId,
            final String guid)
            throws InvalidParameterException, RepositoryErrorException,
                    EntityNotKnownException, EntityProxyOnlyException,
                    UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";

        /*
         * Validate parameters
         */
        this.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */
        return null;
    }
}
