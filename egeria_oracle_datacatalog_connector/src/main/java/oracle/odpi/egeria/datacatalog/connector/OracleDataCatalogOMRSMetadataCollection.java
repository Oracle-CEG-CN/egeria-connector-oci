package oracle.odpi.egeria.datacatalog.connector;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidTypeDefException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class OracleDataCatalogOMRSMetadataCollection extends OMRSMetadataCollectionBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			OracleDataCatalogOMRSMetadataCollection.class);
	
	public OracleDataCatalogOMRSMetadataCollection(
			OMRSRepositoryConnector parentConnector,
			String repositoryName,
			OMRSRepositoryHelper repositoryHelper,
			OMRSRepositoryValidator repositoryValidator,
			String metadataCollectionId) {
		super(parentConnector, repositoryName, repositoryHelper,
				repositoryValidator, metadataCollectionId);
	}
	
	@Override
	public boolean verifyTypeDef(final String userId, final TypeDef typeDef)
			throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException {
		LOGGER.debug("verifyTypeDef({}, {})", userId, typeDef.toString());
		return false;
	}
	
	@Override
	public void addTypeDef(final String userId, final TypeDef typeDef)
			throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefKnownException, TypeDefConflictException, InvalidTypeDefException, FunctionNotSupportedException, UserNotAuthorizedException {
		LOGGER.debug("addTypeDef({}, {} - {})", userId, typeDef.getCategory().getName(), typeDef.getName());
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

	
}
