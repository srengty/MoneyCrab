package com.camant.moneycrab.util;

import java.util.Date;

/**
 * Created by sreng on 11/13/2016.
 */

public class DbUtil {
    public static long dateToLong(Date date){
        if(date == null) {
            return 0l;
        }
        return  date.getTime();
    }
    public static Date longToDate(long dateLong){
        return new Date(dateLong);
    }
    public static Date longToDate(long dateLong, Date defaultDate){
        if(dateLong == 0) return defaultDate;
        return new Date(dateLong);
    }
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
}
