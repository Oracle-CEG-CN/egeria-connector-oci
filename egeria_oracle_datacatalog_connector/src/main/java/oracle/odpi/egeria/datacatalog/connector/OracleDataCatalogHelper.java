package oracle.odpi.egeria.datacatalog.connector;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.bmc.datacatalog.DataCatalogClient;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

/**
 *
 */
public final class OracleDataCatalogHelper {
	
	/**
	 * Used for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			OracleDataCatalogHelper.class);
	
	/**
	 * The implementation using a static Set of supported EntityDef names
	 * is just for testing purposes.
	 */
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
	
	private final Set<TypeDef> registeredTypeDefs = new HashSet<>();
	
	public OracleDataCatalogHelper(
			final DataCatalogClient dataCatalogClient,
			final String catalogId) {
		this.dataCatalogClient = dataCatalogClient;
		this.catalogId = catalogId;
	}
	
	public boolean isSupportedEntityDef(final String typeName) {
		return supportedEntityDefNames.contains(typeName);
	}
	
	public void registerTypeDef(final TypeDef typeDef) {
		LOGGER.debug("registerTypeDef([{} {}])", typeDef.getCategory().getName(), typeDef.getName());
		registeredTypeDefs.add(typeDef);
	}
	
	
	public boolean isRegisteredType(final TypeDef typeDef) {
		return registeredTypeDefs.contains(typeDef);
	}
	
}
