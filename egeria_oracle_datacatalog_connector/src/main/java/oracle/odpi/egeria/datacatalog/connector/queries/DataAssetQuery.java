package oracle.odpi.egeria.datacatalog.connector.queries;

import com.oracle.bmc.datacatalog.model.DataAssetSummary;
import java.util.List;


import com.oracle.bmc.datacatalog.requests.ListDataAssetsRequest;
import com.oracle.bmc.datacatalog.responses.ListDataAssetsResponse;

import java.util.stream.Collectors;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

/**
 *
 */
public class DataAssetQuery implements OracleDataCatalogQuery {

    @Override
    public List<EntityDetail> queryCatalog(final OracleDataCatalogQueryContext context) {
        
        ListDataAssetsRequest request = ListDataAssetsRequest.builder()
                .catalogId(context.getCatalogId())
                .build();
        ListDataAssetsResponse response = context.getDataCatalogClient().listDataAssets(request);
        
        List<EntityDetail> result = response.getDataAssetCollection().getItems().stream()
                .map(dataAssetSummary -> map(dataAssetSummary))
                .collect(Collectors.toList());
        
        return result;
    }

    private EntityDetail map(final DataAssetSummary dataAssetSummary) {
        EntityDetail result = new EntityDetail();
        
        result.setGUID(dataAssetSummary.getKey());
        
        return result;
    }
    
}
