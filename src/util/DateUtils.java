package util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateUtils {
	
	public static String formatDateToString(java.util.Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm"); 
		
		return sdf.format(date);
	}
	public static String formatDateToString(java.util.Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format); 
		
		return sdf.format(date);
	}
	
	
}
