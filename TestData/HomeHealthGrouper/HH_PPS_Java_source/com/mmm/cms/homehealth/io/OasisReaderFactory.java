/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.io;

import com.mmm.cms.homehealth.io.util.Oasis_B_RecordUtil;
import com.mmm.cms.homehealth.io.util.Oasis_C_RecordUtil_v2_00;
import com.mmm.cms.homehealth.io.util.Oasis_C2_RecordUtil_v2_21;
import com.mmm.cms.homehealth.io.util.Oasis_C1_RecordUtil_v2_12;
import com.mmm.cms.homehealth.io.util.Oasis_C_RecordUtil_v2_00a;
import com.mmm.cms.homehealth.io.util.Oasis_C_RecordUtil_v2_10;
import com.mmm.cms.homehealth.io.util.Oasis_C1_RecordUtil_v2_11;
import com.mmm.cms.homehealth.io.util.Oasis_C2_RecordUtil_v2_20;
import com.mmm.cms.homehealth.HomeHealthGrouperFactory;
import com.mmm.cms.homehealth.io.util.Oasis_D_RecordUtil_v2_30;
import com.mmm.cms.util.Initializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class determines the OASIS record type and provides the appropriate
 * converter for the string record.
 *
 * For more details on determining which converter to use, see
 *
 * @see Oasis_B_RecordUtil
 * @see Oasis_C_RecordUtil_v2_00
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class OasisReaderFactory implements Initializable {

    public final static String PROPERTY_OASIS_RECORD_CONVERTER = "oasis.record.converters";

    private final List<OasisRecordConverterIF> converters;

    public OasisReaderFactory() {
        this.converters = new ArrayList<OasisRecordConverterIF>(7);
    }

    /**
     * Given an OASIS record as a string (either flat-file or XML), determine
     * which converter to use in order to convert the record to a HomeHealth
     * Record object.
     *
     * @param oasisRecord
     * @return the proper converter or null if none is found.
     * @see Oasis_B_RecordUtil.isRecordConvertable(String)
     * @see Oasis_C_RecordUtil.isRecordConvertable(String)
     */
    public OasisRecordConverterIF getRecordConverter(String oasisRecord) {
        OasisRecordConverterIF converter = null;

        if (this.converters.isEmpty()) {
            loadDefaultConverters();
        }

        for (OasisRecordConverterIF curConverter : this.converters) {
            if (curConverter.isRecordConvertable(oasisRecord)) {
                converter = curConverter;
                break;
            }
        }

        return converter;
    }

    /**
     * Loads the default known converters
     */
    protected void loadDefaultConverters() {
        this.converters.clear();
        this.converters.add(new Oasis_B_RecordUtil());
        this.converters.add(new Oasis_C_RecordUtil_v2_00());
        this.converters.add(new Oasis_C_RecordUtil_v2_00a());
        this.converters.add(new Oasis_C_RecordUtil_v2_10());
        this.converters.add(new Oasis_C1_RecordUtil_v2_11());
        this.converters.add(new Oasis_C1_RecordUtil_v2_12());
        this.converters.add(new Oasis_C2_RecordUtil_v2_20());
        this.converters.add(new Oasis_C2_RecordUtil_v2_21());
        this.converters.add(new Oasis_D_RecordUtil_v2_30());
        this.converters.add(new OasisXMLConverter());
    }

    public List<OasisRecordConverterIF> getConverters() {
        return this.converters;
    }

    @Override
    public void init(Properties properties) throws Exception {
        String tmpStr;
        String classname;

        if (properties != null) {
            tmpStr = properties.getProperty(PROPERTY_OASIS_RECORD_CONVERTER);
            if (tmpStr != null) {
                OasisRecordConverterIF converter;

                /*
                 * break up the list of converters and load them
                 */
                final String array[] = tmpStr.split(" ");
                for (String item : array) {
                    item = item.trim();
                    converter = (OasisRecordConverterIF) Class.forName(item).newInstance();
                    this.converters.add(converter);
                }
            }
        }

        /*
         * since no converters were loaded, just load the default ones
         */
        if (this.converters.isEmpty()) {
            loadDefaultConverters();
        }

        if (properties != null) {
            /*
             * walk through the converters and determine if there is a start/end
             * date configured for them
             */
            for (OasisRecordConverterIF converter : this.converters) {
                classname = converter.getClass().getName();
                tmpStr = properties.getProperty(classname + HomeHealthGrouperFactory.PROPERTY_START_DATE_SUFFIX);
                if (tmpStr != null) {
                    converter.setStartDate(tmpStr);
                }

                tmpStr = properties.getProperty(classname + HomeHealthGrouperFactory.PROPERTY_END_DATE_SUFFIX);
                if (tmpStr != null) {
                    converter.setEndDate(tmpStr);
                }
            }
        }
    }

}
