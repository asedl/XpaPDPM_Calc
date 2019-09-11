/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.io;

import com.mmm.cms.homehealth.io.record.Oasis_C1_Record_2_11;
import com.mmm.cms.homehealth.io.record.Oasis_C1_Record_2_12;
import com.mmm.cms.homehealth.io.record.Oasis_C2_Record_2_20;
import com.mmm.cms.homehealth.io.record.Oasis_C_Record;
import com.mmm.cms.homehealth.DiagnosisCode;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_ASSESSMENT;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_SPEC_VRSN_CD;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_C;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_11;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_12;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_C2_2_20;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_C2_2_21;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_DEFAULT;
import static com.mmm.cms.homehealth.io.OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_D_2_30;
import com.mmm.cms.homehealth.io.record.Oasis_D_Record_2_30;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * In conjunction with SAX XML parsing, this collects the tags with values and
 * then creates a record, either C or C1, based on the M0090 and the version
 * identifier. This actually allows the XML to have more than one record, so the
 * getRecords() may return more than one record, but according to the OASIS
 * spec, most likely there will only be one record in the list.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class OasisXmlHandler extends DefaultHandler {

    private final List<HomeHealthRecordIF> records;
    private transient String currentElementName;
    private final transient StringBuilder currentElementValue;
    private final Map<String, String> tagValues;
    private transient Map<String, Method> fieldMethods;
    private transient Map<String, Class> homeHealthRecordFactory;

    public OasisXmlHandler() {
        records = new ArrayList();
        currentElementValue = new StringBuilder();
        tagValues = new HashMap<String, String>();

        fieldMethods = new HashMap<String, Method>();
        homeHealthRecordFactory = new HashMap<String, Class>();

        homeHealthRecordFactory.put(TAG_SPEC_VRSN_CD_VALUE_OASIS_DEFAULT, Oasis_D_Record_2_30.class);
        homeHealthRecordFactory.put(TAG_SPEC_VRSN_CD_VALUE_OASIS_C, Oasis_C_Record.class);
        homeHealthRecordFactory.put(TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_11, Oasis_C1_Record_2_11.class);
        homeHealthRecordFactory.put(TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_12, Oasis_C1_Record_2_12.class);
        homeHealthRecordFactory.put(TAG_SPEC_VRSN_CD_VALUE_OASIS_C2_2_20, Oasis_C2_Record_2_20.class);
        homeHealthRecordFactory.put(TAG_SPEC_VRSN_CD_VALUE_OASIS_C2_2_21, Oasis_C2_Record_2_20.class);
        homeHealthRecordFactory.put(TAG_SPEC_VRSN_CD_VALUE_OASIS_D_2_30, Oasis_D_Record_2_30.class);
        homeHealthRecordFactory.put(OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_10A, Oasis_C_Record.class);
        homeHealthRecordFactory.put(OasisXMLConverter.TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_10A_OTHER, Oasis_C_Record.class);
    }

    public List<HomeHealthRecordIF> getRecords() {
        return records;
    }

    @Override
    public void characters(char[] chars, int startIdx, int endIdx) throws
            SAXException {
        currentElementValue.append(chars, startIdx, endIdx);
    }

    /**
     * This collects the tag/values until it hits the ASSESSMENT end tag, at
     * which point is creates the internal HH record and converts the data to
     * the fields in the record.
     *
     * @param uri
     * @param localName
     * @param qualifiedName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qualifiedName) throws
            SAXException {

        if (TAG_ASSESSMENT.equalsIgnoreCase(qualifiedName)) {
            String tmpStr = this.tagValues.get(TAG_SPEC_VRSN_CD).trim();

            if (tmpStr != null) {
                /*
                 * We have to do this here because it is unclear what record
                 * type to use until we get to this variable, then use the value
                 * within the tag to pull the class from the record factory
                 */
                HomeHealthRecordIF currentRecord;
                Class clazz;

                clazz = homeHealthRecordFactory.get(tmpStr);
                if (clazz == null) {
                    clazz = homeHealthRecordFactory.get(TAG_SPEC_VRSN_CD_VALUE_OASIS_DEFAULT);
                }

                try {
                    // add the current record to the list
                    currentRecord = (HomeHealthRecordIF) clazz.newInstance();

                    fillInRecord(currentRecord);

                    /*
                     * fill in any DX codes that are null
                     */
                    if (currentRecord.getPRIMARY_DIAG_ICD() == null) {
                        currentRecord.setPRIMARY_DIAG_ICD(new DiagnosisCode());
                    }
                    //Format: XX99.XX
                    if (currentRecord.getOTH_DIAG1_ICD() == null) {
                        currentRecord.setOTH_DIAG1_ICD(new DiagnosisCode());
                    }
                    //Format: XX99.XX
                    if (currentRecord.getOTH_DIAG2_ICD() == null) {
                        currentRecord.setOTH_DIAG2_ICD(new DiagnosisCode());
                    }
                    //Format: XX99.XX
                    if (currentRecord.getOTH_DIAG3_ICD() == null) {
                        currentRecord.setOTH_DIAG3_ICD(new DiagnosisCode());
                    }
                    //Format: XX99.XX
                    if (currentRecord.getOTH_DIAG4_ICD() == null) {
                        currentRecord.setOTH_DIAG4_ICD(new DiagnosisCode());
                    }
                    //Format: XX99.XX
                    if (currentRecord.getOTH_DIAG5_ICD() == null) {
                        currentRecord.setOTH_DIAG5_ICD(new DiagnosisCode());
                    }
                    if (currentRecord.getPMT_DIAG_ICD_A3() == null) {
                        currentRecord.setPMT_DIAG_ICD_A3(new DiagnosisCode());
                    }
                    if (currentRecord.getPMT_DIAG_ICD_B3() == null) {
                        currentRecord.setPMT_DIAG_ICD_B3(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_C3() == null) {
                        currentRecord.setPMT_DIAG_ICD_C3(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_D3() == null) {
                        currentRecord.setPMT_DIAG_ICD_D3(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_E3() == null) {
                        currentRecord.setPMT_DIAG_ICD_E3(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_F3() == null) {
                        currentRecord.setPMT_DIAG_ICD_F3(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_A4() == null) {
                        currentRecord.setPMT_DIAG_ICD_A4(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_B4() == null) {
                        currentRecord.setPMT_DIAG_ICD_B4(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_C4() == null) {
                        currentRecord.setPMT_DIAG_ICD_C4(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_D4() == null) {
                        currentRecord.setPMT_DIAG_ICD_D4(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_E4() == null) {
                        currentRecord.setPMT_DIAG_ICD_E4(new DiagnosisCode());
                    }
                    //Format: X999.XX
                    if (currentRecord.getPMT_DIAG_ICD_F4() == null) {
                        currentRecord.setPMT_DIAG_ICD_F4(new DiagnosisCode());
                    }

                    // add the current record to the list
                    records.add(currentRecord);

                } catch (InstantiationException ex) {
                    Logger.getLogger(OasisXMLConverter.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(OasisXMLConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            this.tagValues.put(this.currentElementName, this.currentElementValue.toString());
            this.currentElementValue.setLength(0);
        }
    }

    /**
     * this loops through the keys in the tagValues and applies their values to
     * the supplied record.
     *
     * @param record
     */
    public void fillInRecord(HomeHealthRecordIF record) {
        Set<String> keySet = this.tagValues.keySet();
        String value;

        for (String key : keySet) {
            value = this.tagValues.get(key);
            fillInRecordValue(record, key, value);

        }
    }

    /**
     * Given a specific record, it associates the named (tag) value to the
     * related field. Some fields are known to be non-String, and they are
     * automatically converted to internal data types, such as DiagnosisCode,
     * Calendar, or integer.
     *
     * In order to find the field name within the record, the "M----_" prefix of
     * the tag is removed, which should match with the field and the get/set
     * methods within the record object.
     *
     * @param record
     * @param tag
     * @param value
     */
    public void fillInRecordValue(HomeHealthRecordIF record, String tag, String value) {
        // use the element name to set the value in the home
        // health record
        try {
            Method method;
            Class clazzParams[];
            Object settingObj;

            // determine if the name is an ICD-9-Diagnosis
            if (tag.indexOf("_DIAG", 0) > -1) {
                // build the diagnosis code
                clazzParams = new Class[]{DiagnosisCodeIF.class};
                value = HomeHealthRecordUtil.trimWithCarets(value);
                settingObj = new DiagnosisCode("^".equals(value) ? "" : value);

            } else if (tag.startsWith("M0030")
                    || tag.startsWith("M0090")) {
                // build the Calendar
                clazzParams = new Class[]{Calendar.class};
                settingObj = OasisCalendarFormatter.parse(value.substring(0, 8));
            } else if (tag.indexOf("NEED_NUM", 0) > -1
                    || tag.indexOf("NEED_NBR", 0) > -1) {
                // build the integer
                clazzParams = new Class[]{int.class};
                settingObj = Integer.valueOf(HomeHealthRecordUtil.parseTherapyNeedNumber_C1(value, -1));

            } else {
                // set the string value
                clazzParams = new Class[]{String.class};
                settingObj = value.intern();
            }

            method = getSetMethod(record, tag, clazzParams);
            method.setAccessible(true);
            method.invoke(record, settingObj);

        } catch (NumberFormatException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (ParseException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (NoSuchMethodException ex) {
            /*
             * This is not really an error since a record object may not 
             * care about all the fields. So, just log it for debugging.
             */
            Logger.getLogger(getClass().getName()).log(Level.FINE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        }

    }

    @Override
    public void startElement(String uri, String localName, String qualifiedName,
            Attributes arg3) throws SAXException {

        if (TAG_ASSESSMENT.equalsIgnoreCase(qualifiedName)) {
            /*
             * This is the record begin/end tag, so don't worry about saving
             * any data - just clear the data
             */
            this.tagValues.clear();
        } else {
            // keep the element name and reset the value
            currentElementName = qualifiedName;
            currentElementValue.setLength(0);
        }
    }

    /**
     * This assumes that the methods are overloaded, so there is only one per
     * field name.
     *
     * @param fieldName
     * @param params
     * @return
     */
    protected Method getSetMethod(Object obj, String fieldName, Class params[]) throws NoSuchMethodException {
        Method method;
        String methodName;

        methodName = "set" + getBaseMethodName(fieldName);
        
        method = fieldMethods.get(methodName);
        if (method == null) {
            final Class clazz = obj.getClass();
            method = clazz.getMethod(methodName, params);
            fieldMethods.put(methodName, method);
        }
        return method;
    }

    /**
     * This checks for variables with "M" numbers at the beginning; if it starts
     * with "M" then strip that off and return the name starting with the first
     * non-number, non-underline character which is at character position 7
     * (offset 6).
     *
     * @param variableName
     * @return
     */
    private String getBaseMethodName(String variableName) {
        // Temp solution for C2
        String retName = null;
        if (variableName.charAt(0) == 'M') { // OVK
            if (variableName.startsWith("M1311")) {
                if (variableName.equals("M1311_NSTG_DEEP_TSUE_F1")) {
                    retName = "NSTG_DEEP_TISUE";
                } else {
                    retName = variableName.substring(6);
                    retName = retName.substring(0, retName.length() - 3);
                }
            } else {
                retName = variableName.substring(6);
            }
        } else {
            retName = variableName;
        }
        return retName;
    }

}
