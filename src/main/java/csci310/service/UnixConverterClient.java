package csci310.service;

import java.util.Date;
//For converting UNIX to data and vice versa
public class UnixConverterClient {
    //Converts Unix long value to date object	
    public static Date convertUnixToDate(long unix) {
    	// Convert unixSeconds to milliseconds
    	Date date = new Date(unix*1000L);
    	return date;
    }
    //Converts date object to unix long value
    public static long convertDateToUnix(Date date) {
    	return date.getTime();
    }
}
