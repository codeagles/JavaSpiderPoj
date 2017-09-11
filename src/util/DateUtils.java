package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static String formatDateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		return sdf.format(date);
	}
	public static String formatDateToString(java.util.Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format); 
		
		return sdf.format(date);
	}
	
	
}
