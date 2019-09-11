/*
 *  This is an unpublished work containing 3M confidential and
 *  proprietary information. Disclosure or reproduction without the
 *  written  authorization of 3M is prohibited.  If publication occurs,
 *  the following notice applies:
 *
 *  Copyright (C) 1998-2003, 3M All rights reserved.
 *
 */
package com.mmm.cms.util;

import com.mmm.cms.homehealth.proto.CommonMessageText;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This extends the java.util.Properties class to override the getProperty()
 * methods to allow automatic conversion of macros in the property to the
 * actual property value.  Marcos are defined as ${...}
 *
 * For example, if the properties file has the next two entries:
 *
 *   base.dir=C:/MyPlace
 *   data.dir=${base.dir}/dataAndStuff
 *
 * Then the value of data.dir, when using getProperties(), the macro would be
 * substituted to be "C:/MyPlace/dataAndStuff"
 *
 * Using the getPropertyRaw() methods would be the same as the original
 * Properties.getProperty() methods.
 *
 * methods
 * @author  3M HIS, Tim Gallagher
 */
public class MacroProperties extends Properties {

    public static void main(String args[]) {
        Properties props = new MacroProperties();

        props.setProperty("base.dir", "C:\\Program Files\\HomeHealthGrouper");
        props.setProperty("data.dir", "${base.dir}\\dataAndStuff");

        System.out.println("Raw Properties");
        System.out.println("\t" +
                ((MacroProperties) props).getPropertyRaw("base.dir"));
        System.out.println("\t" +
                ((MacroProperties) props).getPropertyRaw("data.dir"));
        System.out.println("\t" +
                ((MacroProperties) props).getPropertyRaw("junk"));

        System.out.println("Marco Properties");
        System.out.println("\t" + props.getProperty("base.dir"));
        System.out.println("\t" + props.getProperty("data.dir"));
        System.out.println("\t" + props.getProperty("junk"));

    }

    public MacroProperties() {
    }

    /**
     * This overrides the Properties() constructor to set the props with
     * the super(props), but also to set the props in the current table
     * @param props
     */
    public MacroProperties(Properties props) {
        super(props);
        // move the properties into the real table
        Enumeration enumeration = props.keys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            setProperty(key, props.getProperty(key));
        }
    }

    @Override
    public String getProperty(String arg0) {
		String str; 
		
        try {
            String value = super.getProperty(arg0);
            str = parsePropertiesInString(value, false);

        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
			str = null;
        }
        return str;
    }

    /**
     * Gets the property identified by key, and then parses it for any
     * macro information and replaces the macros that can be replaced
     *
     * @param key
     * @param defaultValue
     * @return
     */
    @Override
    public String getProperty(String key, String defaultValue) {
		String property;
		
        try {
            defaultValue = super.getProperty(key, defaultValue);
            property = parsePropertiesInString(defaultValue, false);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
			property = defaultValue;
        }
        return property;
    }

    /**
     * Parses a string by replacing all occurrences of a property macro with
     * the resolved value of the property. Nested macros are allowed - the
     * inner most macro will be resolved first, moving out from there.
     *
     * @param string The string to be parsed
     * @param failIfMissing if true, then throws an exception if the item is
     * missing, otherwise, it ignores the fact that it is missing
     * @return The parsed string
     * @throws CruiseControlException if a property cannot be resolved
     */
    public String parsePropertiesInString(String string,
            final boolean failIfMissing)
            throws Exception {

        if (string != null) {
            final int startIndex = string.indexOf("${");
            if (startIndex != -1) {
                int openedBrackets = 1;
                int lastStartIndex = startIndex + 2;
                int endIndex;

                do {
                    endIndex = string.indexOf("}", lastStartIndex);
                    int otherStartIndex = string.indexOf("${", lastStartIndex);
                    if (otherStartIndex != -1 && otherStartIndex < endIndex) {
                        openedBrackets++;
                        lastStartIndex = otherStartIndex + 2;
                    } else {
                        openedBrackets--;
                        if (openedBrackets == 0) {
                            break;
                        }
                        lastStartIndex = endIndex + 1;
                    }
                } while (true);

                if (endIndex < startIndex + 2) {
                    throw new Exception("Unclosed brackets in " + string);
                }

                final String property = string.substring(startIndex + 2, endIndex);
                // not necessarily resolved
                final String propertyName = parsePropertiesInString(property, failIfMissing);
                String value = "".equals(propertyName) ? "" : (String) get(propertyName);

                if (value == null) {
                    if (failIfMissing) {
                        throw new Exception("Property \"" + propertyName +
                                "\" is not defined. Please check the order in which you have used your properties.");
                    } else {
                        // we don't resolve missing properties
                        value = "${" + propertyName + "}";
                    }
                }
                string = string.substring(0, startIndex) + value +
                        parsePropertiesInString(string.substring(endIndex + 1), failIfMissing);
            }
        }
        return string;
    }

    /**
     * Gets the property value without substituting any macro information. So,
     * using the example at the top of this class, getting "data.dir" would
     * return the value ${base.dir}/dataAndStuff
     *
     * @param key
     * @return the unchanged value of the property for key.  null if the
     * property does not exist.
     */
    public String getPropertyRaw(String key) {
        return super.getProperty(key);
    }
}
