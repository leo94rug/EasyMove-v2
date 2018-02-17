/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.text.ParseException;

/**
 *
 * @author leo
 */
public interface IDate {

    /**
     *
     * @param dt
     * @return
     * @throws Exception
     */
    java.util.Date stringToDate(String dt) throws Exception;

    /**
     *
     * @param date
     * @return
     */
    String dateToString(java.util.Date date);

    /**
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    boolean before(String date1, String date2) throws ParseException;

    /**
     *
     * @return
     */
    String now();

    /**
     *
     * @return
     */
    String addYears();

    /**
     *
     * @param dataString
     * @return
     * @throws ParseException
     */
    int calcoloEta(String dataString) throws ParseException;
}
