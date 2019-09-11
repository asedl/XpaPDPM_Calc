/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.test;

import com.mmm.cms.util.HHEventConsole;
import com.mmm.cms.homehealth.ScoringResults;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperFactoryIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.util.IntegerFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HomeHealthGroupingFactoryTest extends CommonTester {

    public static void main(String args[]) {
        CommonTester tester;

        tester = new HomeHealthGroupingFactoryTest();
        tester.runTest(args);
    }

    @Override
    public void runTest(BufferedReader bufReader, Writer writer,
            HomeHealthGrouperFactoryIF grouperFactory,
            CommandLineParams commandLine) {
        OasisRecord_B_ReadWriteTest tester;
        HomeHealthGroupingFactoryTest groupingTester;

        List<HomeHealthRecordIF> oasisRecords = null;
        long starttime;
        long elapsedTime;
        long overallStarttime;
        List<ScoringResultsIF> scoringResults;
        int recordNumber = 0;

        overallStarttime = System.currentTimeMillis();

        tester = new OasisRecord_B_ReadWriteTest();
        groupingTester = new HomeHealthGroupingFactoryTest();

        try {
            starttime = System.currentTimeMillis();
            oasisRecords = tester.readConvertTest(bufReader);
            elapsedTime = System.currentTimeMillis() - starttime;
            System.out.println("Number of Oasis Records read: " +
                    oasisRecords.size() + ", elapsed time: " + elapsedTime);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: During ReadTest", ex);
            System.out.println("During ReadTest: " + ex);
            System.exit(2);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: During ReadTest", ex);
            System.out.println("During ReadTest: " + ex);
            System.exit(3);
        }

        try {
            starttime = System.currentTimeMillis();
            scoringResults = groupingTester.groupingTest(grouperFactory, oasisRecords, recordNumber);
            elapsedTime = System.currentTimeMillis() - starttime;
            System.out.println("Number of Oasis Records scored: " +
                    scoringResults.size() + ", elapsed time: " + elapsedTime);

            // output the results
            groupingTester.writeOutputFile(scoringResults, writer);

        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: Validation Test", ex);
            System.out.println("Grouping test could not complete due to: " +
                    ex);
            ex.printStackTrace();
        }

        elapsedTime = System.currentTimeMillis() - overallStarttime;
        System.out.println();
        System.out.println("Total processing elapsed time: " + elapsedTime);

    }

    public List<ScoringResultsIF> groupingTest(
            HomeHealthGrouperFactoryIF grouperFactory,
            List<HomeHealthRecordIF> oasisRecords,
            int recordNumber) throws
            Exception {
        List<ScoringResultsIF> scoringResults;
        HomeHealthGrouperIF grouper;
        ScoringResultsIF scoring;
        int idx = 0;
        Level tmpLevel = null;
        ConsoleHandler consoleHandler = null;
        Logger logger = null;
        HomeHealthEventListenerIF listener = null;

        scoringResults = new ArrayList<ScoringResultsIF>(oasisRecords.size());

        // read the records from the input
        for (HomeHealthRecordIF record : oasisRecords) {
            grouper = grouperFactory.getGrouper(record);
            if (grouper != null) {
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

                scoring = grouper.score(record, false);
                scoringResults.add(scoring);

                if (logger != null) {
                    logger.removeHandler(consoleHandler);
                    logger.setLevel(tmpLevel);
                    System.err.flush();
                    logger = null;
                    grouper.removeEventListener(listener);
                    listener = null;
                }
            } else {
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.WARNING, "HH-PPS: Could not find grouper for record number: {0}", ++idx);
                scoringResults.add(new ScoringResults(record));
            }
        }

        return scoringResults;
    }

    /**
     * This method assumes the records have been read, converted, and validated
     *
     * @param scoringResults
     * @param fileWriter
     * @throws java.io.IOException
     */
    public void writeOutputFile(List<ScoringResultsIF> scoringResults,
            Writer writer) throws IOException {
        BufferedWriter bufferedWriter;
        IntegerFormat iformat;
        StringBuilder buffer;
        long starttime = System.currentTimeMillis();
        long elapsedTime;

        buffer = new StringBuilder();

        // open the file
        bufferedWriter = new BufferedWriter(writer);

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
                } else {
                    buffer.append("     ");
                }

                if (scoringResult.getHIPPSCode() != null &&
                        !scoringResult.getHIPPSCode().getCode().trim().isEmpty()) {
                    buffer.append(scoringResult.getTreatmentAuthorization().getAuthorizationCode());
                } else {
                    buffer.append("                  ");
                }

                buffer.append("--Version ");
                buffer.append(scoringResult.getGrouperVersion());
                buffer.append(" --Flag ");
                buffer.append(scoringResult.getValidityFlag().getValidityFlag());
                buffer.append("\r\n");
                bufferedWriter.write(buffer.toString());
            }

        } finally {
            bufferedWriter.close();
            elapsedTime = System.currentTimeMillis() - starttime;
            System.out.println("Records written: elapsed time: " + elapsedTime);

        }
    }
}
