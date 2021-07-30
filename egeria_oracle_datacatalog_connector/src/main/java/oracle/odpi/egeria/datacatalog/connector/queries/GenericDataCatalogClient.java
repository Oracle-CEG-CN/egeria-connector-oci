package oracle.odpi.egeria.datacatalog.connector.queries;

import com.fasterxml.jackson.databind.JsonNode;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.StringPrivateKeySupplier;

import com.oracle.bmc.http.signing.DefaultRequestSigner;
import com.oracle.bmc.http.signing.RequestSigner;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.net.URI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 *
 */
public final class GenericDataCatalogClient {
    
    private static final String BASE_URL_TEMPLATE
            = "https://datacatalog.%s.oci.oraclecloud.com/20190325/catalogs/%s";
    
    private final String region;
    private final String tenancy;
    private final String user;
    private final String fingerprint;
    private final String privateKey;
    private final String catalogId;
    
    public GenericDataCatalogClient(
            final String region,
            final String tenancy,
            final String user,
            final String fingerprint,
            final String privateKey,
            final String catalogId) {
        this.region = region;
        this.tenancy = tenancy;
        this.user = user;
        this.fingerprint = fingerprint;
        this.privateKey = privateKey;
        this.catalogId = catalogId;
    }
    
    private String baseUrl() {
        return String.format(BASE_URL_TEMPLATE, region, catalogId);
    }
    
    private WebClient createWebClient() {
        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
            return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public JsonNode queryCatalog(final String queryTemplate, final Object[] templateArgs) {
        StringPrivateKeySupplier privateKeySupplier = new StringPrivateKeySupplier(privateKey);
        
        AuthenticationDetailsProvider provider
            = SimpleAuthenticationDetailsProvider.builder()
                .tenantId(tenancy)
                .userId(user)
                .fingerprint(fingerprint)
                .privateKeySupplier(privateKeySupplier)
                .build();

        RequestSigner requestSigner;
        requestSigner = DefaultRequestSigner.createRequestSigner(provider);
        
        WebClient client = createWebClient();

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Accept", Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        
        final String uriSuffix = String.format(queryTemplate, templateArgs);
        
        final URI uri = URI.create(String.format("%s/%s", baseUrl(), uriSuffix));
        
        Map<String, String> signatureHeaders = requestSigner.signRequest(
                uri, "GET", headers, null);
        
        JsonNode result = client.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .headers((HttpHeaders headersConsumer) -> {
                    signatureHeaders.keySet().forEach(key -> {
                        headersConsumer.add(key, signatureHeaders.get(key));
                    });
                })
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        
        return result;
    }
}
