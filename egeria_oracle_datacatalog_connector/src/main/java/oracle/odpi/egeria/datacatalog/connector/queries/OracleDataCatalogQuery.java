package oracle.odpi.egeria.datacatalog.connector.queries;

import java.util.List;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

/**
 *
 */
public interface OracleDataCatalogQuery {
    List<EntityDetail> queryCatalog(OracleDataCatalogQueryContext context);
}
