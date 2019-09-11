/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Holds the command line parameters based on a name=value pair.
 *
 * @author Tim Gallagher, 3M HIS, Clinical & Economic Research
 */
public class CommandLineParams {

    private File propertiesFile;
    private File inputFile;
    private int numOfRecords;
    private int firstRecord;
    private int lastRecord = Integer.MAX_VALUE;
    private int detailRecordNum;
    private File outputFile;
    private boolean hideTAC;
    private boolean hideVersion;
    private String basePath;
    private boolean showExtraOptions;

    private Map<String, String> skipWithCodes;

    public CommandLineParams(String args[]) {
        String parts[];
        Pattern pattern = Pattern.compile("=");

        try {

            for (int idx = 0; idx < args.length; idx++) {
                parts = pattern.split(args[idx]);

                if (parts.length == 2) {
                    if ("config".equalsIgnoreCase(parts[0])) {
                        propertiesFile = new File(parts[1]);
                    } else if ("input".equalsIgnoreCase(parts[0])) {
                        inputFile = new File(parts[1]);
                    } else if ("limit".equalsIgnoreCase(parts[0])) {
                        numOfRecords = Integer.parseInt(parts[1]);
                    } else if ("details".equalsIgnoreCase(parts[0])) {
                        detailRecordNum = Integer.parseInt(parts[1]);
                    } else if ("outputFile".equalsIgnoreCase(parts[0])) {
                        outputFile = new File(parts[1]);
                    } else if ("records".equalsIgnoreCase(parts[0])) {
                        int dashIdx = parts[1].indexOf("-");
                        if (dashIdx >= 0) {
                            firstRecord = Integer.parseInt(parts[1].substring(0, dashIdx++));
                            lastRecord = Integer.parseInt(parts[1].substring(dashIdx));
                        } else {
                            firstRecord = Integer.parseInt(parts[1]);
                        }
                    } else if ("skipWithCodes".equalsIgnoreCase(parts[0])) {
                        String tokens[] = parts[1].split(",");
                        skipWithCodes = new HashMap<String, String>(tokens.length);
                        for (String token : tokens) {
                            this.skipWithCodes.put(token, token);
                        }
                    } else if ("basePath".equalsIgnoreCase(parts[0])) {
                        this.basePath = parts[1];
                    } else {
                        parseExtra(parts);
                    }
                } else if (parts.length == 1) {
                    if ("hideTAC".equalsIgnoreCase(parts[0])) {
                        hideTAC = true;
                    } else if ("hideVersion".equalsIgnoreCase(parts[0])) {
                        hideVersion = true;
                    } else if ("X".equalsIgnoreCase(parts[0])) {
                        showExtraOptions = true;
                    } else {
                        parseExtra(parts);
                    }
                } else {
                    parseExtra(parts);
                }
            }
        } catch (Exception e) {
            // don't really care, we'll just take the defaults
        }

        validateParams();
    }

    public String getSyntax(Class clazz) {
        return "Syntax: "
                + clazz.getSimpleName()
                + " config=<file name> input=<file name> [details=<record number>] [limit=<number of records>] [records=<start record>[-<end record>]]\n"
                + " item within [] are optional\nlimit and datails can not be used together\nlimit negates details";
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getPropertiesFile() {
        return propertiesFile;
    }

    public int getDetailRecordNum() {
        return detailRecordNum;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public int getFirstRecord() {
        return firstRecord;
    }

    public int getLastRecord() {
        return lastRecord;
    }

    public Map<String, String> getSkipWithCodes() {
        return skipWithCodes;
    }

    public String getBasePath() {
        return this.basePath;
    }

    public boolean isShowExtraOptions() {
        return showExtraOptions;
    }

    /**
     * This receives the name and value of a parameter on the command line to
     * allow extended classes to perform extra parsing for specific variables
     *
     * @param parts
     */
    public void parseExtra(String parts[]) {
        // extending classes should override this
    }

    public void validateParams() {
        if (inputFile == null) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Missing \"input\" file name.");
        }
    }

    public List<String> getCommandOptions() {
        List<String> options;

        options = new ArrayList<String>();

        options.add("config=<file name>.properties  Required. No default.");
        options.add("input=<file name>  Required. No default.");
        options.add("limit=<positive integer> Optional. Default = all records");
        options.add("details=<record number> Optional. Default no details");
        options.add("records=<first record number> Optional. Default all records");
        options.add("records=<first record number>-<last recod number> Optional. For doing a range of records. Default all records");
        options.add("outputFile=<file name> Optional. Default is input file name with \"_OUT\", appended to the base name - before the last decimal.");
        options.add("basePath=[path to the parent of the folders with the 'Grouper...tables' data");
        if (this.showExtraOptions) {
            options.add("skipWithCodes=<code[,code...]> Optional.  Comma separate list of codes that if found on the record, that record is skipped from scoring. Default all records.");
            options.add("hideTAC Optional. If this is present, the Treatment Authorization Code is not output");
            options.add("hideVersion Optional. If this is present, the Version indicator is not output");
        }

        return options;
    }

    public boolean isHideTAC() {
        return hideTAC;
    }

    public boolean isHideVersion() {
        return hideVersion;
    }

    public void setInputFile(File file) {
        this.inputFile = file;
    }
}
