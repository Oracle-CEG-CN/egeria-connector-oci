package oracle.odpi.egeria.datacatalog.connector.queries;

import com.fasterxml.jackson.databind.JsonNode;
import com.oracle.bmc.ConfigFileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class GenericDataCatalogClientTest {
    
    private final String CATALOG_ID = "ocid1.datacatalog.oc1.eu-frankfurt-1.aaaaaaaavftawcz5yaj5sa7seakgzhecpiu3qatqbbvuxzga4bkribyaueuq";
    
    
    
    private GenericDataCatalogClient client;
    
    @Before
    public void setUp() throws Exception {
        final ConfigFileReader.ConfigFile configuration = ConfigFileReader.parseDefault();
        final String region = configuration.get("region");
        final String tenancy = configuration.get("tenancy");
        final String user = configuration.get("user");
        final String fingerprint = configuration.get("fingerprint");
        
        final String key = Files.lines(Paths.get("/home/stefan/.oci/oci_api_key.pem")).collect(Collectors.joining("\n"));
        
        client = new GenericDataCatalogClient(region, tenancy, user, fingerprint, key, CATALOG_ID);
    }
    
    @Test
    public void testQueryCatalog() {
        JsonNode result = client.queryCatalog("dataAssets/%s", new Object[] { "fd732d11-80e5-4f1b-8288-9102e147151d" });
        assertThat(result).isNotNull();
    }
    
}
