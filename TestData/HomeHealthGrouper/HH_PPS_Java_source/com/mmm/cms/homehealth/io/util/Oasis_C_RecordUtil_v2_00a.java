/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.io.util;


/**
 * This class reads OASIS records that were originally OASIS C 2.00 at 1449 characters
 * but incorrectly converted to extend C at 3256 character, but did not change the
 * version number, so the version number is "02.00" or "2.00"
 * 
 * @author 3M HIS
 */
public class Oasis_C_RecordUtil_v2_00a extends Oasis_C_RecordUtil_v2_10 {

    /**
     * this needs to be overridden because some records coming out of the ASAP/QIES
     * system have the wrong version id indicator.
     * 
     * @param record
     * @return 
     */
    @Override
    public boolean isRecordConvertable(String record) {
        boolean convertable = false;

        if (record.length() >= getRecordLength()) {
            String infoCompleteDate;
            String specVersionCD;

            specVersionCD = getVersionCD(record);
            infoCompleteDate = getRecordDate(record);

            // make sure the CD1 is C and the year is 2014 or greater
            convertable = (specVersionCD.startsWith("02") || specVersionCD.startsWith("2.0"))
                    && dateRanger.isDateWithinRange(infoCompleteDate);
        }

        return convertable;
    }
    
}
