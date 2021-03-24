package com.ydqp.common.utils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date todayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date todayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static int dateToZonedDateTime(Date date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("+08:00"));
        ZonedDateTime ydZonedDateTime = zonedDateTime.withZoneSameLocal(ZoneId.of("+05:30"));
        return new Long(Timestamp.from(ydZonedDateTime.toInstant()).getTime() / 1000).intValue();
    }

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String timestampToStr(int time) {
        Timestamp timestamp = new Timestamp(time * 1000L);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("+05:30"));
        return dateTimeFormatter.format(zonedDateTime);
    }

    public static void main(String[] args) {
//        String time = "2020-03-24 09:28:00";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        ZonedDateTime ydTime = LocalDateTime.parse(time, formatter).atZone(ZoneId.of("+05:30"));

        ZonedDateTime ydTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("+05:30"));
        if (ydTime.getHour() >= 0 && ydTime.getHour() < 9 || ydTime.getHour() == 9 && ydTime.getMinute() < 30) {
            System.out.println(ydTime.getHour());
            System.out.println(ydTime.getMinute());
        }
    }
}
