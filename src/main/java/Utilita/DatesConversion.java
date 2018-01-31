/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilita;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author leo
 */
/**
 * java.util.date to java.sql.date
 */
public class DatesConversion {

    static String pattern = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    public static java.util.Date stringToDate(String dt) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        java.util.Date date = null;
        date = simpleDateFormat.parse(dt);
        return date;
    }

    public static String dateToString(java.util.Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String format = formatter.format(date);
        return format;
    }

    public static boolean before(String date1, String date2) throws ParseException {
        return stringToDate(date1).before(stringToDate(date2));
    }

    public static String now() {
        return dateToString(new java.util.Date());
    }

    public static String addYears(String currentDateString,int year) {
        LocalDateTime dateTime = LocalDateTime.parse(currentDateString, FORMATTER);
        dateTime = dateTime.plusYears(year);
        String yearsAfterString = dateTime.format(FORMATTER);
        return yearsAfterString;
    }
    public static String addYears() {
        LocalDateTime dateTime = LocalDateTime.parse(now(), FORMATTER);
        dateTime = dateTime.plusYears(1);
        String yearsAfterString = dateTime.format(FORMATTER);
        return yearsAfterString;
    }
    public static java.sql.Date setDate(int giorno, int mese, int anno) {
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gc.clear();
        gc.set(anno, mese, giorno);
        java.sql.Date date = new java.sql.Date(gc.getTimeInMillis());
        return date;
    }

    public static java.sql.Timestamp setDateTime(int minuto, int ora, int giorno, int mese, int anno) {
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gc.clear();
        gc.set(anno, mese, giorno, ora - 2, minuto);
        java.sql.Timestamp date = new java.sql.Timestamp(gc.getTimeInMillis());
        return date;
    }

    public static int calcoloEta(String dataString) throws ParseException {
        java.util.Date data = stringToDate(dataString);
        Calendar c = Calendar.getInstance();
        int anno = c.get(Calendar.YEAR);
        /* Ottieni l'anno */
        int mese = c.get(Calendar.MONTH);
        /* Ottieni il mese */
        int giorno = c.get(Calendar.DAY_OF_MONTH);
        /* Ottieni il giorno */
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gc.clear();
        gc.setTimeInMillis(data.getTime());
        gc.set(getYear(data), getMonth(data), getDay(data));
        today.set(anno, mese, giorno);
        double giorniFra = giorniFraDueDate(gc, today);
        return (int) (giorniFra / 365);
    }

    public static double giorniFraDueDate(GregorianCalendar dallaDataGC, GregorianCalendar allaDataGC) {

        // conversione in millisecondi
        long dallaDataMilliSecondi = dallaDataGC.getTimeInMillis();
        long allaDataMilliSecondi = allaDataGC.getTimeInMillis();

        long millisecondiFraDueDate = allaDataMilliSecondi - dallaDataMilliSecondi;

        // conversione in giorni con la divisione intera
        double giorniFraDueDate_DivInt = millisecondiFraDueDate / 86400000;

        return giorniFraDueDate_DivInt;
    }

    public static int getDay(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getYear(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getDay(java.sql.Date sDate) {
        java.util.Date uDate = new java.util.Date(sDate.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(uDate);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(java.sql.Date sDate) {
        java.util.Date uDate = new java.util.Date(sDate.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(uDate);
        return cal.get(Calendar.MONTH);
    }

    public static int getYear(java.sql.Date sDate) {
        java.util.Date uDate = new java.util.Date(sDate.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(uDate);
        return cal.get(Calendar.YEAR);
    }
}
