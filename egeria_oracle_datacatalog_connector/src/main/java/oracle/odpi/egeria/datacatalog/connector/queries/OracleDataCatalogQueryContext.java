package oracle.odpi.egeria.datacatalog.connector.queries;

import com.oracle.bmc.datacatalog.DataCatalogClient;

/**
 *
 */
public class OracleDataCatalogQueryContext {
    
    private final DataCatalogClient dataCatalogClient;
    
    private final String catalogId;
    
    public OracleDataCatalogQueryContext(
            final DataCatalogClient dataCatalogClient,
            final String catalogId) {
        this.dataCatalogClient = dataCatalogClient;
        this.catalogId = catalogId;
    }
    
    public final DataCatalogClient getDataCatalogClient() {
        return dataCatalogClient;
    }
    
    public final String getCatalogId() {
        return catalogId;
    }
    
}
