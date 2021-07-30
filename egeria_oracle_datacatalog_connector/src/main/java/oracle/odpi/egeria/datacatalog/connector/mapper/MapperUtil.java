package oracle.odpi.egeria.datacatalog.connector.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 */
public final class MapperUtil {
    private MapperUtil() {
        // Utility class constructor.
    }
    
    public static Date dateFromJson(final String jsonDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            Pattern p = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})\\d{0,3}(.*)");
            Matcher m = p.matcher(jsonDate);
            if (m.matches()) {
                final String dateForParsing = String.format("%s%s", m.group(1), m.group(2));

                return dateFormat.parse(dateForParsing);
            } else {
                return dateFormat.parse(jsonDate);
            }
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}
