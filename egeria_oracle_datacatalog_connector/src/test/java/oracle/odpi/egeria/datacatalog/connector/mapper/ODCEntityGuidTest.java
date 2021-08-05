package oracle.odpi.egeria.datacatalog.connector.mapper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class ODCEntityGuidTest {
    @Test
    public void testFromOMRSGuid() {
        ODCEntityGuid guid = ODCEntityGuid.fromOMRSGuid("aa-bb.cc-dd.ee-ff");
        
        assertThat(guid).isNotNull();
        assertThat(guid.getMetadataCollectionId()).isEqualTo("aa-bb");
        assertThat(guid.getOdcTypeKey()).isEqualTo("cc-dd");
        assertThat(guid.getOdcKey()).isEqualTo("ee-ff");
    }
}
