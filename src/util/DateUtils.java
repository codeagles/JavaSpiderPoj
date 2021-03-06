package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formatDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(date);
    }

    public static String formatDateToString(java.util.Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);
    }

    /*
    * 将时间转换为时间戳
    */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        String hour = String.valueOf(new Date().getHours());
        String min = String.valueOf(new Date().getMinutes());
        String second = String.valueOf(new Date().getSeconds());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (s.contains("年") && s.contains("月") && s.contains("日") && s.contains(":")) {
            s = s.replace("日 ", "日");
            simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        } else if (s.contains("年") && s.contains("月") && s.contains(":")) {
            simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd HH:mm");
        } else if (s.contains("-")) {
            if (s.contains(":")) {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            } else {
                s = s + " " + hour + ":" + min + ":" + second;
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            }
        } else if (s.contains("/")) {
            if (s.contains(":")) {
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            } else {
                s = s + " " + hour + ":" + min + ":" + second;
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            }
        } else {
            long ts = new Date().getTime();
            res = String.valueOf(ts);
            return res;
        }

        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

//    public static void main(String[] args) throws ParseException {
//        System.out.printf(DateUtils.dateToStamp("2017-12-27 09:12:22"));
//
//    }
}
