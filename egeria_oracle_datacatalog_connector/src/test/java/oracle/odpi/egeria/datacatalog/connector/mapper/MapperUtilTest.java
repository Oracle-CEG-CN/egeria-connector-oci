package oracle.odpi.egeria.datacatalog.connector.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class MapperUtilTest {

    private final String TESTDATE1 = "2021-07-29T13:50:04.883000Z";
    private final String TESTDATE2 = "2021-07-29T13:50:04.883Z";

    private void testMatchPattern(final String testDate) {
        Pattern p = Pattern.compile(
                "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})\\d{0,3}(.*)");
        Matcher m = p.matcher(testDate);
        if (m.matches()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.println(String.format("%d: %s", i, m.group(i)));
            }
        }
    }

    @Test
    public void testUnderstandPattern() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS000X");

        testMatchPattern(TESTDATE1);
        testMatchPattern(TESTDATE2);

//        Date date = dateFormat.parse(TESTDATE1);
//        
//        assertThat(date).isNotNull();
    }

    @Test
    public void testDate1() {
        Date date = MapperUtil.dateFromJson("2021-04-26T12:47:25.348000Z");

        assertThat(date).isNotNull();
    }

    @Test
    public void testDate2() {
        Date date = MapperUtil.dateFromJson("2021-04-26T12:47:25.348Z");

        assertThat(date).isNotNull();
    }
}
