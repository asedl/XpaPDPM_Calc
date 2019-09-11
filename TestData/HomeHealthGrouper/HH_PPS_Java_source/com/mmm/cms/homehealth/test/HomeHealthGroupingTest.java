/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth.test;


import com.mmm.cms.util.HHEventConsole;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.v3210.GrouperVersion_v3210;
import com.mmm.cms.util.IntegerFormat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HomeHealthGroupingTest {

    /**
     *
     * @param args An array of 1) input file name, and 2) output filename.  The
     * output filename is optional with the default being the input filename
     * suffixed with "_TESTOUT" before the extension.  The extension will be
     * the same as the input filename.
     *
     * Any status of the test or errors are presented to the console.
     */
    public static void main(String args[]) {

        OasisRecord_B_ReadWriteTest tester;
        HomeHealthGroupingTest groupingTester;
        HomeHealthGrouperIF grouper;

        String inputFilename;
        String outputFilename;
        String suffix = "_TESTOUT";
        List<HomeHealthRecordIF> oasisRecords = null;
        long starttime;
        long elapsedTime;
        long overallStarttime;
        List<ScoringResultsIF> scoringResults;
        Properties props = null;
        File tmpfile;
        int recordNumber = 0;

        // dump some system data
        Properties props2 = System.getProperties();
        Enumeration keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            System.out.println(key + "=" + props2.getProperty(key));
        }

        System.out.flush();


        if (args.length > 0) {
            overallStarttime = System.currentTimeMillis();
            inputFilename = args[0];

            int idx = inputFilename.lastIndexOf(".");
            if (idx > -1) {
                outputFilename = inputFilename.substring(0, idx) + suffix +
                        inputFilename.substring(idx);
            } else {
                outputFilename = inputFilename + suffix;
            }

            props = new Properties();
            if (args.length > 1) {
                try {
                    tmpfile = new File(args[1]);
                    props.load(new FileInputStream(tmpfile));
                } catch (IOException ex) {
                    Logger.getLogger(HomeHealthGrouperIF.class.getName()).log(Level.SEVERE, "HH-PPS: Could not open properties file: " +
                            args[1], ex);
                }
            }

            if (args.length > 2) {
                recordNumber = Integer.parseInt(args[2]);
            }

            System.out.println("Input file is: " + inputFilename);
            System.out.println("Output file is: " + outputFilename);

            tester = new OasisRecord_B_ReadWriteTest();
            groupingTester = new HomeHealthGroupingTest();
            grouper = new GrouperVersion_v3210();

            try {
                starttime = System.currentTimeMillis();
                oasisRecords = tester.readConvertTest(new File(inputFilename));
                elapsedTime = System.currentTimeMillis() - starttime;
                System.out.println("Number of Oasis Records read: " +
                        oasisRecords.size() + ", elapsed time: " + elapsedTime);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(HomeHealthGrouperIF.class.getName()).log(Level.SEVERE, "HH-PPS: During ReadTest", ex);
                System.out.println("During ReadTest: " + ex);
                System.exit(2);
            } catch (IOException ex) {
                Logger.getLogger(HomeHealthGrouperIF.class.getName()).log(Level.SEVERE, "HH-PPS: During ReadTest", ex);
                System.out.println("During ReadTest: " + ex);
                System.exit(3);
            }

            try {
                starttime = System.currentTimeMillis();
                scoringResults = groupingTester.groupingTest(grouper, oasisRecords, props, recordNumber);
                elapsedTime = System.currentTimeMillis() - starttime;
                System.out.println("Number of Oasis Records scored: " +
                        scoringResults.size() + ", elapsed time: " + elapsedTime);

                // output the results
                groupingTester.writeOutputFile(scoringResults, new File(outputFilename));

            } catch (Exception ex) {
                Logger.getLogger(HomeHealthGrouperIF.class.getName()).log(Level.SEVERE, "HH-PPS: Validation Test", ex);
                System.out.println("Grouping test could not complete due to: " +
                        ex);
                ex.printStackTrace();
            }

            elapsedTime = System.currentTimeMillis() - overallStarttime;
            System.out.println();
            System.out.println("Total processing elapsed time: " + elapsedTime);

        } else {
            System.out.println("Syntax: HomeHealthGroupingTest \"nput file name\" <output file name - optional>");
        }
    }

    public List<ScoringResultsIF> groupingTest(HomeHealthGrouperIF grouper,
            List<HomeHealthRecordIF> oasisRecords, Properties grouperProperties, int recordNumber) throws
            Exception {
        List<ScoringResultsIF> scoringResults;
        ScoringResultsIF scoring;
        int idx = 0;
        Level tmpLevel = null;
        ConsoleHandler consoleHandler = null;
        Logger logger = null;
        HomeHealthEventListenerIF listener = null;

        if (grouperProperties != null) {
            grouper.init(grouperProperties);
        }

        scoringResults = new ArrayList<ScoringResultsIF>(oasisRecords.size());

        // read the records from the input
        for (HomeHealthRecordIF record : oasisRecords) {
            if (++idx == recordNumber) {

                System.out.println("record " + idx);
                System.out.flush();
                listener = new HHEventConsole();
                grouper.addEventListener(listener);
                consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.FINE);

                logger = Logger.getLogger(getClass().getName());
                tmpLevel = logger.getLevel();
                logger.setLevel(Level.FINE);
                logger.addHandler(consoleHandler);
            }

            scoring = grouper.score(record, true);
            scoringResults.add(scoring);

            if (logger != null) {
                logger.removeHandler(consoleHandler);
                logger.setLevel(tmpLevel);
                System.err.flush();
                logger = null;
                grouper.removeEventListener(listener);
                listener = null;
            }
        }

        return scoringResults;
    }

    /**
     * This method assumes the records have been read, converted, and validated
     *
     * @param scoringResults
     * @param output
     * @throws java.io.IOException
     */
    public void writeOutputFile(List<ScoringResultsIF> scoringResults,
            File output) throws IOException {
        BufferedWriter writer;
        FileWriter fileWriter;
        IntegerFormat iformat;
        StringBuilder buffer;
        long starttime = System.currentTimeMillis();
        long elapsedTime;

        buffer = new StringBuilder();

        // open the file
        fileWriter = new FileWriter(output);
        writer = new BufferedWriter(fileWriter);

        iformat = new IntegerFormat('0');
        iformat.setMinimumIntegerDigits(6);
        iformat.setMaximumIntegerDigits(6);

        try {
            int idx = 1;

            // read the records from the input
            for (ScoringResultsIF scoringResult : scoringResults) {
                buffer.setLength(0);

                // convert them from Oasis Records and write them to the file

                buffer.append("Record ");
                buffer.append(iformat.format(idx++));
                buffer.append(": Code ");
                if (scoringResult.getHIPPSCode() != null) {
                        buffer.append(scoringResult.getHIPPSCode().getCode());
                        buffer.append("--OASIS ");
                } else
                    buffer.append("     ");

                if (scoringResult.getHIPPSCode() != null && !scoringResult.getHIPPSCode().getCode().trim().isEmpty())
                    buffer.append(scoringResult.getTreatmentAuthorization().getAuthorizationCode());
                else
                    buffer.append("                  ");

                buffer.append("--Version ");
                buffer.append(scoringResult.getGrouperVersion());
                buffer.append(" --Flag ");
                buffer.append(scoringResult.getValidityFlag().getValidityFlag());
                buffer.append("\r\n");
                writer.write(buffer.toString());
            }

        } finally {
            writer.close();
                elapsedTime = System.currentTimeMillis() - starttime;
                System.out.println("Records written: elapsed time: " + elapsedTime);

        }
    }



}
