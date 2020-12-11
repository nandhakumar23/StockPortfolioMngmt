package csci310.service;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.junit.Test;

public class UnixConverterClientTest {

	@Test
	public void testConvertUnixToDate() {
	    long unix = 1604620800; // 2020-11-05
	    Date resultDate = UnixConverterClient.convertUnixToDate(unix);
	    Calendar cal = Calendar.getInstance();
        cal.setTime(resultDate);
        int year = cal.get(Calendar.YEAR);
        assertEquals(2020, year);
        
        // TEST constructor
        @SuppressWarnings("unused")
		UnixConverterClient unixConverterClient = new UnixConverterClient();
	}

	@Test
	public void testConvertDateToUnix() {
		long unixSeconds = 1604620800;
		// convert seconds to milliseconds
		Date date = new java.util.Date(unixSeconds*1000L);
		assertEquals(UnixConverterClient.convertDateToUnix(date), unixSeconds*1000L);
	}

}
