/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilita;

import Interfaces.IDate;
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
public class DatesConversion implements IDate{

    static String pattern = "yyyy-MM-dd HH:mm:ss";

    /**
     *
     * @param dt
     * @return
     * @throws ParseException
     */
    @Override
    public java.util.Date stringToDate(String dt) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        java.util.Date date = null;
        date = simpleDateFormat.parse(dt);
        return date;
    }

    /**
     *
     * @param date
     * @return
     */
    @Override
    public String dateToString(java.util.Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    @Override
    public boolean before(String date1, String date2) throws ParseException {
        return stringToDate(date1).before(stringToDate(date2));
    }

    /**
     *
     * @return
     */
    @Override
    public String now() {
        return dateToString(new java.util.Date());
    }

    /**
     *
     * @param currentDateString
     * @param year
     * @return
     */
    public static String addYears(String currentDateString,int year) {
        DateTimeFormatter form = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(currentDateString, form);
        dateTime = dateTime.plusYears(year);
        String yearsAfterString = dateTime.format(form);
        return yearsAfterString;
    }
    @Override
    public String addYears() {
        return addYears(now(),1);
    }

    /**
     *
     * @param dataString
     * @return
     * @throws ParseException
     */
    @Override
    public int calcoloEta(String dataString) throws ParseException {
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

    private static double giorniFraDueDate(GregorianCalendar dallaDataGC, GregorianCalendar allaDataGC) {
        return (allaDataGC.getTimeInMillis() - dallaDataGC.getTimeInMillis()) / 86400000;
    }

    private static int getDay(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    private static int getMonth(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    private static int getYear(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
}
