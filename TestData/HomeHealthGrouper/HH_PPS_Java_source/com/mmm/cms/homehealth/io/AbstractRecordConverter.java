/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.io;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.util.DateRanger;
import java.text.ParseException;

/**
 * Provide basic conversion functionality
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public abstract class AbstractRecordConverter 
        implements OasisRecordConverterIF {

    protected DateRanger dateRanger;
    private int recordLength;
    private String versionCDPrefix;

    public AbstractRecordConverter() {
        this("20070101", "20091231");
    }

    protected AbstractRecordConverter(String startDate, String endDate) {
        this(startDate, endDate, 1446);
    }

    protected AbstractRecordConverter(String startDate, String endDate, int recordLength) {
        this(startDate, endDate, recordLength, "B1");
    }

    protected AbstractRecordConverter(String startDate, String endDate, int recordLength, String versionCDPrefix) {
        this.dateRanger = new DateRanger(startDate, endDate);
        this.recordLength = recordLength;
        this.versionCDPrefix = versionCDPrefix;
    }

    public StringBuilder convertFromHomeHealthRec(HomeHealthRecordIF oasisRecord) {
        return convertFromHomeHealthRecDelimeted(oasisRecord, "");
    }
    
    /**
     * calls convertToHomeHealthRec() with the current parameters and false for
     * the passthru
     * 
     * @param strRecord
     * @param recNum
     * @return
     * @throws ParseException 
     */
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum) throws ParseException {
        return convertToHomeHealthRec(strRecord, recNum, false);
    }

    
    public DateRanger getDateRanger() {
        return dateRanger;
    }

    public void setDateRanger(DateRanger dateRanger) {
        this.dateRanger = dateRanger;
    }

    /**
     * Sets the end date of the Date Ranger
     * @param date - must be non-null, and formatted as yyyyMMdd
     */
    public void setEndDate(String date) {
        if (date != null && date.length() == 8) {
            this.dateRanger.setEndDate(date);
        }
    }

    /**
     * Sets the start date of the Date Ranger
     * @param date - must be non-null, and formatted as yyyyMMdd
     */
    public void setStartDate(String date) {
        if (date != null && date.length() == 8) {
            this.dateRanger.setStartDate(date);
        }
    }
    
    public int getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    /**
     * Given an OASIS record string, determines if this converter can actually
     * convert the record to its supported internal object.
     *
     * This checks the record length, the record's identifying date (usually the
     * M0090 date) and the Version indicator. These can be in different offsets
     * in different records.
     *
     * @param record
     * @return true if this converter can convert the string to an internal
     * HomeHealthRecordIF object
     */
    public boolean isRecordConvertable(String record) {
        boolean convertable = false;

        if (record.length() >= recordLength) {
            final String versionCD = getVersionCD(record);
            final String recordDate = getRecordDate(record);

            // make sure the CD1 is C and the year is 2010 or greater
            convertable = (versionCD.length() >= 2 && versionCD.startsWith(this.versionCDPrefix))
                    && dateRanger.isDateWithinRange(recordDate);
        }

        return convertable;
    }

    /**
     * Gets the date that marks the time period for the record, usually the
     * M0090 Info Complete Date, if the date is null, or bad it is returned as 8
     * spaces
     *
     * @param record
     * @return non-null String representing an OASIS date (yyyyMMdd), but must
     * be 8 spaces if blank
     */
    abstract protected String getRecordDate(String record);

    /**
     * Gets the record version which is identified in different locations with
     * the different records.
     *
     * @param record
     * @return non-null String, but may be empty
     */
    abstract protected String getVersionCD(String record);

}
