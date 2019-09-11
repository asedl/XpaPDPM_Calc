/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.io;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import java.text.ParseException;

/**
 * This provides definitions for testing if a record converted is appropriate
 * and then to actually convert a record.
 *
 * Converters should be able to convert from and to an OASIS related string
 * format. However, since the focus of the HHRG is only a subset of the OASIS
 * record, converting to an OASIS string format does not have to guarantee all
 * the fields that were originally available in an original source OASIS string.
 *
 * For example, the HHRG is not concerned with the patient's name, address, etc.
 * and therefore a converter does not have to read those fields when converting
 * to an internal HomeHealthRecordIF. Conversely, when converting from an
 * internal HomeHealthRecordIF, the converter does not have to output those
 * fields either.
 *
 * So, an input OASIS record is only guaranteed to look like the output OASIS
 * record where the fields related to HHRG are concerned. All other fields may
 * be a space.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public interface OasisRecordConverterIF {

    /**
     * Given an OASIS 1448 or 1446 length string, determines if the converter
     * can actually convert the record to its supported object.
     *
     * @param record
     * @return
     */
    boolean isRecordConvertable(String record);

    /**
     * Converts an OASIS record extended from the HomeHealth Record to a String
     * that is a continuous set of characters, i.e. no delimiter
     *
     * @param homeHealthRecord
     * @return
     * @see #convertFromHomeHealthRecDelimeted(HomeHealthRecordIF
     * homeHealthRecord, String delimiter) {
     */
    StringBuilder convertFromHomeHealthRec(HomeHealthRecordIF homeHealthRecord);

    /**
     * Converts an OASIS record extended from the HomeHealth Record to a String
     * using the delimiter to separate the values
     *
     * @param homeHealthRecord
     * @param delimiter
     * @return
     */
    StringBuilder convertFromHomeHealthRecDelimeted(HomeHealthRecordIF homeHealthRecord,
            String delimiter);

    /**
     * Converts a string to a Home Health record, using the 3 parameter
     * converter
     *
     * @param strRecord
     * @param recNum
     * @return
     * @throws java.text.ParseException
     * @see #convertToHomeHealthRec(String strRecord, int recNum, boolean
     * skipPassthru) throws ParseException
     */
    HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum) throws ParseException;

    /**
     * Converts an OASIS string record to an OASIS Body record using the generic
     * Home Health Record interface. Since much of the OASIS information is not
     * needed for Home Health grouping, this method can ignore them or store
     * them with the record using the <code>skipPassthru</code> parameter
     *
     * @param strRecord
     * @param recNum
     * @param skipPassthru
     * @return
     */
    HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum,
            boolean skipPassthru) throws ParseException;

    /**
     * Sets the start date of the converter, inclusive
     *
     * @param startDate - must be non-null in the format of YYYYMMDD, for
     * example October 1, 2014 would formatted as 20140101
     */
    void setStartDate(String startDate);

    /**
     * Sets the end date of the converter, inclusive
     *
     * @param endDate - must be non-null in the format of YYYYMMDD, for example
     * Sept 30, 2014 would formatted as 20140930
     */
    void setEndDate(String endDate);

}
