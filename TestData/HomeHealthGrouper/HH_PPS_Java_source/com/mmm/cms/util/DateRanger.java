/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.util;

/**
 * Holds a date range represented as a string with the format of YYYYMMDD
 *
 * @author 3M HIS Clinical & Economic
 */
public class DateRanger {

    private String startDate;
    private String endDate;

    public DateRanger() {
    }

    public DateRanger(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the start date, this is an inclusive date
     *
     * @param date - non-null string representing date with the format of
     * YYYYMMDD
     */
    public void setStartDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
        }
        if (date.length() == 8) {
            this.startDate = date;
        } else {
            throw new IllegalArgumentException("date format must be YYYYMMDD");
        }
    }

    /**
     * Sets the end date, this is an inclusive date
     *
     * @param date - non-null string representing date with the format of
     * YYYYMMDD
     */
    public void setEndDate(String date) throws IllegalArgumentException {
        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
        }
        if (date.length() == 8) {
            this.endDate = date;
        } else {
            throw new IllegalArgumentException("date format must be YYYYMMDD");
        }
    }

    /**
     * This compares a date to a date range and only guaranteed to work if the
     * dates are formatted as YYYYMMDD, eg April 1, 2014 would 20140101
     *
     * @param date - non-null string representing date with the format of
     * YYYYMMDD
     * @return true if the date is >= start date and &amp;= to end date
     */
    public boolean isDateWithinRange(String date) {
        return date.compareTo(this.startDate) >= 0 && date.compareTo(this.endDate) <= 0;
    }
}
