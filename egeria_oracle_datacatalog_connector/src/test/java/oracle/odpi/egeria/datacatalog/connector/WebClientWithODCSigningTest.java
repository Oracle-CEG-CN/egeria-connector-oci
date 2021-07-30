package oracle.odpi.egeria.datacatalog.connector;

import com.fasterxml.jackson.databind.JsonNode;

import com.oracle.bmc.ConfigFileReader;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;

import com.oracle.bmc.http.signing.DefaultRequestSigner;
import com.oracle.bmc.http.signing.RequestSigner;

import java.net.URI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.Test;

import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 */
public class WebClientWithODCSigningTest {
    
    private static final URI DATA_ASSET_URI = URI.create("https://datacatalog.eu-frankfurt-1.oci.oraclecloud.com/20190325/catalogs/ocid1.datacatalog.oc1.eu-frankfurt-1.aaaaaaaavftawcz5yaj5sa7seakgzhecpiu3qatqbbvuxzga4bkribyaueuq/dataAssets/fd732d11-80e5-4f1b-8288-9102e147151d");
    
    @Test
    public void testGetAsset() throws Exception {
        final ConfigFileReader.ConfigFile configuration = ConfigFileReader.parseDefault();
        final String keyFile = configuration.get("key_file");
        final String tenancy = configuration.get("tenancy");
        final String user = configuration.get("user");
        final String fingerprint = configuration.get("fingerprint");
        
        SimplePrivateKeySupplier privateKeySupplier = new SimplePrivateKeySupplier(keyFile);

        AuthenticationDetailsProvider provider
            = SimpleAuthenticationDetailsProvider.builder()
                .tenantId(tenancy)
                .userId(user)
                .fingerprint(fingerprint)
                .privateKeySupplier(privateKeySupplier)
                .build();

        RequestSigner requestSigner;
        requestSigner = DefaultRequestSigner.createRequestSigner(provider);
        
        WebClient client = WebClient.create();
        
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Accept", Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        
        Map<String, String> signatureHeaders = requestSigner.signRequest(
                DATA_ASSET_URI, "GET", headers, null);
        
        JsonNode content = client.get()
                .uri(DATA_ASSET_URI)
                .accept(MediaType.APPLICATION_JSON)
                .headers(headersConsumer -> {
                    for (String key : signatureHeaders.keySet()) {
                        headersConsumer.add(key, signatureHeaders.get(key));
                    }
                })
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        
        
        
        System.out.println(content);
        
    }
}
