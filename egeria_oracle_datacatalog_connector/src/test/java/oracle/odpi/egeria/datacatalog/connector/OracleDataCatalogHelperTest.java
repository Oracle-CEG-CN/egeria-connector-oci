package oracle.odpi.egeria.datacatalog.connector;

import com.oracle.bmc.datacatalog.DataCatalogClient;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Arrays;
import java.util.List;

import mockit.Expectations;
import mockit.Injectable;
import oracle.odpi.egeria.datacatalog.connector.queries.GenericDataCatalogClient;

import org.junit.Test;
import org.junit.Before;

import static org.assertj.core.api.Assertions.assertThat;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;

/**
 *
 */
public class OracleDataCatalogHelperTest {
    
    @Injectable
    private DataCatalogClient dataCatalogClient;
    
    @Injectable
    private GenericDataCatalogClient genericDataCatalogClient;
    
    @Injectable
    private OMRSRepositoryHelper omrsRepositoryHelper;
    
    private OracleDataCatalogHelper oracleDataCatalogHelper;
    
    private TypeDef createTypeDef(
            final TypeDefCategory typeDefCategory,
            final String guid,
            final String name) {
        return new EntityDef(typeDefCategory, guid, name, 0L, null);
    }
    
    @Before
    public void setUp() throws Exception {
        oracleDataCatalogHelper = new OracleDataCatalogHelper(
                "eine-metadata-collection-id",
                dataCatalogClient,
                genericDataCatalogClient,
                "OracleDataCatalogHelperTest",
                omrsRepositoryHelper);
        oracleDataCatalogHelper.registerTypeDef(createTypeDef(TypeDefCategory.ENTITY_DEF, "guid-Database", "Database"));
        oracleDataCatalogHelper.registerTypeDef(createTypeDef(TypeDefCategory.ENTITY_DEF, "guid-RelationalTableType", "RelationalTableType"));
        
        new Expectations() {{
            omrsRepositoryHelper.getTypeDef(anyString, "entityTypeGUID", "a", "getSupportedEntityTypeDefsFor");
            result = createTypeDef(TypeDefCategory.ENTITY_DEF, "a", "a_type");
            
            omrsRepositoryHelper.getSubTypesOf(anyString, "a_type");
            result = Arrays.asList(new String[] {"aa_type", "ab_type", "ac_type", "ad_type", "DataBase", "RelationalTableType"});
            
            omrsRepositoryHelper.getTypeDefByName("", "aa_type");
            result = createTypeDef(TypeDefCategory.ENTITY_DEF, "aa", "aa_type");
            omrsRepositoryHelper.getTypeDefByName("", "ab_type");
            result = createTypeDef(TypeDefCategory.ENTITY_DEF, "ab", "ab_type");
            omrsRepositoryHelper.getTypeDefByName("", "ac_type");
            result = createTypeDef(TypeDefCategory.ENTITY_DEF, "ac", "ac_type");
            omrsRepositoryHelper.getTypeDefByName("", "ad_type");
            result = createTypeDef(TypeDefCategory.ENTITY_DEF, "ad", "ad_type");
            omrsRepositoryHelper.getTypeDefByName("", "DataBase");
            result = createTypeDef(TypeDefCategory.ENTITY_DEF, "guid-Database", "Database");
            omrsRepositoryHelper.getTypeDefByName("", "RelationalTableType");
            result = createTypeDef(TypeDefCategory.ENTITY_DEF, "guid-RelationalTableType", "RelationalTableType");
            
        }};
    }
    
    
    @Test
    public void testGetSupportedEntityTypeDefsFor1() throws Exception {
        List<TypeDef> result = oracleDataCatalogHelper.getSupportedEntityTypeDefsFor(
                "a", null);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        TypeDef resultTypeDef;
        resultTypeDef = result.get(0);
        assertThat(resultTypeDef).isNotNull();
        assertThat(resultTypeDef.getName()).isEqualTo("Database");
        resultTypeDef = result.get(1);
        assertThat(resultTypeDef).isNotNull();
        assertThat(resultTypeDef.getName()).isEqualTo("RelationalTableType");
    }
    
    @Test
    public void testGetSupportedEntityTypeDefsFor2() throws Exception {
        List<TypeDef> result = oracleDataCatalogHelper.getSupportedEntityTypeDefsFor(
                "a", Arrays.asList(new String[]{ "ab", "ac", "guid-RelationalTableType"}));
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        TypeDef resultTypeDef = result.get(0);
        assertThat(resultTypeDef).isNotNull();
        assertThat(resultTypeDef.getName()).isEqualTo("RelationalTableType");
    }
    
}
