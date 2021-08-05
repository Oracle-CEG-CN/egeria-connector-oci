package oracle.odpi.egeria.datacatalog.connector.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *  The {@code String} representation of an {@code ODCEntityGuid} is used as <b>GUID</b>
 *  for <b>Egeria</b> {@code EntityDetail} instances.
 *  The {@code String} representation consists of several parts separated by a dot ({@code .}):
 *  <ul>
 *    <li>the {@code metadataCollectionId} used in <b>Egeria</b> for the <b>Oracle Data Catalog</b> instance.
 *    <li>the key of the <b>ODC</b> type</li>
 *    <li>the <b>ODC</b> key</li>
 *  </ul>
 */
public final class ODCEntityGuid {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ODCEntityGuid.class);
    
    private static final String DOT = ".";
    
    private final String metadataCollectionId;
    private final String odcTypeKey;
    private final String odcKey;
    
    public ODCEntityGuid(
            final String metadataCollectionId,
            final String odcTypeKey,
            final String odcKey) {
        this.metadataCollectionId = metadataCollectionId;
        this.odcTypeKey = odcTypeKey;
        this.odcKey = odcKey;
    }
    
    public static final ODCEntityGuid fromOMRSGuid(final String omrsGuid) {
        LOGGER.debug("ODCEntityGuid.fromOMRSGuid({})", omrsGuid);
        if (omrsGuid != null) {
            String[] omrsGuidParts = omrsGuid.split("\\.");
            if (omrsGuidParts.length == 3) {
                return new ODCEntityGuid(
                        omrsGuidParts[0], omrsGuidParts[1], omrsGuidParts[2]);
            }
        }
        return null;
    }

    public String getMetadataCollectionId() {
        return metadataCollectionId;
    }

    public String getOdcTypeKey() {
        return odcTypeKey;
    }

    public String getOdcKey() {
        return odcKey;
    }
    
    
    
    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();
        
        resultBuilder.append(metadataCollectionId);
        resultBuilder.append(DOT);
        resultBuilder.append(odcTypeKey);
        resultBuilder.append(DOT);
        resultBuilder.append(odcKey);
        
        return resultBuilder.toString();
    }
    
}
