/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.test;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisDataItemIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisValidationEditIF;
import com.mmm.cms.homehealth.DataValidityFlag;
import com.mmm.cms.homehealth.ScoringEventCollector;
import com.mmm.cms.homehealth.ScoringResults;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.EventId_EN;
import com.mmm.cms.homehealth.proto.HomeHealthEventIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperFactoryIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.proto.ServiceIssueException;
import com.mmm.cms.homehealth.vut.CollectionValidationEdits_HhPpsOnly;
import com.mmm.cms.util.HHEventConsole;
import com.mmm.cms.util.ScoringResultsFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This tests reading an OASIS-B/C set of records and scoring them using the
 * appropriate grouper provided by the grouper factory.
 *
 * This testing module represents the best example of how to access the Java
 * HHRG directly, without using the DLL bridge.
 *
 * There are parameters for this module that allow the details of a single
 * record to be displayed to the console. Refer to the inner class
 * CommandLineParams for more details.
 *
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthGrouper_HP extends CommonTester {

    protected ScoringResultsIF scoringResultsEmpty;
    // this represents the end of file record
    protected ScoringResultsIF scoringBLANK = new ScoringResults();

    public HomeHealthGrouper_HP() {
        super();
        scoringResultsEmpty = new ScoringResults();
    }

    /**
     *
     * @param args An array of 1) input file name, and 2) output filename. The
     * output filename is optional with the default being the input filename
     * suffixed with "_TESTOUT" before the extension. The extension will be the
     * same as the input filename.
     *
     * Any status of the test or errors are presented to the console.
     */
    public static void main(String args[]) {
        HomeHealthGrouper_HP tester;

        tester = new HomeHealthGrouper_HP();
        tester.runTest(args);
    }

    @Override
    public void runTest(BufferedReader bufReader, Writer writer,
            HomeHealthGrouperFactoryIF grouperFactory,
            CommandLineParams commandLine) {
        int recCount;
        long overallStarttime;
        long elapsedTime;

        overallStarttime = System.currentTimeMillis();

        recCount = scoreEachIndividually(bufReader, writer,
                grouperFactory, (GrouperTestParams) commandLine);

        elapsedTime = System.currentTimeMillis() - overallStarttime;
        final Logger logger = Logger.getLogger(getClass().getName());

        logger.log(Level.INFO, "Records processed: {0}, Elapsed time: {1}, records/sec {2}",
                new Object[]{recCount, elapsedTime, elapsedTime > 0 ? recCount * 1000 / elapsedTime : recCount});
    }

    public void calendarCompareTest() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd GGG hh:mm:ss");
        Calendar effectiveStartDate;
        Calendar effectiveThruDate;
        Calendar calendar3;
        int compareValue;

        // compare two dates that the same and are built the same way
        effectiveThruDate = new GregorianCalendar(2013, 11, 31);
        System.out.println("effectiveThruDate: " + dateFormat.format(effectiveThruDate.getTime()));
        effectiveStartDate = new GregorianCalendar(2013, 11, 31);
        System.out.println("effectiveStartDate: " + dateFormat.format(effectiveStartDate.getTime()));

        calendar3 = new GregorianCalendar();
        calendar3.set(2013, 12 - 1, 31, 0, 0, 0);
        calendar3.set(Calendar.MILLISECOND, 0);
        System.out.println("Calendar 3: " + dateFormat.format(effectiveStartDate.getTime()));

        if (effectiveThruDate.after(effectiveStartDate)) {
            System.out.println("effectiveThruDate is after effectiveStartDate");
        } else if (effectiveStartDate.after(effectiveThruDate)) {
            System.out.println("effectiveStartDate is after effectiveThruDate");
        } else if (!effectiveThruDate.after(effectiveStartDate)) {
            System.out.println("effectiveThruDate is not after effectiveStartDate");
        }

        // perform similar valid test
        if (calendar3.before(effectiveStartDate)) {
            System.out.println("calendar3 is before effectiveStartDate");
        }
        if (calendar3.after(effectiveThruDate)) {
            System.out.println("calendar3 is after effectiveThruDate");
        }

        compareValue = calendar3.compareTo(effectiveThruDate);
        System.out.println("calendar3 compared to effectiveThruDate = " + compareValue);

    }

    /**
     * This reads, scores, and writes one record at a time.
     *
     * @param inReader
     * @param outWriter
     * @param grouperFactory
     * @param commandLineParams
     */
    public int scoreEachIndividually(BufferedReader inReader, Writer outWriter,
            HomeHealthGrouperFactoryIF grouperFactory,
            GrouperTestParams commandLineParams) {
        HomeHealthRecordIF record = null;
        ScoringResultsIF scoring;
        int idx = 1;

        if (commandLineParams.getNumOfRecords() > 0) {
            int recordLimit = commandLineParams.getNumOfRecords();
            // create the writing thread and start is running
            while (recordLimit-- > 0 && (record = readRecord(inReader, idx)) != recordEOF) {
                if (record == null) {
                    scoring = scoringBLANK;
                } else {
                    if (!this.isRecordIncludingCodes(record, commandLineParams.getSkipWithCodes())) {
                        // score the record
                        scoring = scoreRecord(grouperFactory, record, false, commandLineParams);
                    } else {
                        recordLimit++;
                        continue;
                    }
                }
                writeOutput(outWriter, scoring, idx++, commandLineParams);
                displayEdits(commandLineParams, scoring, idx);
            }

        } else if (commandLineParams.getDetailRecordNum() > 0) {
            int detailRecNum = commandLineParams.getDetailRecordNum();
            while (detailRecNum-- > 0 && (record = readRecord(inReader, idx)) != recordEOF) {
                if (this.isRecordIncludingCodes(record, commandLineParams.getSkipWithCodes())) {
                    // add one back to the record count since we are not 
                    // scoring this record
                    detailRecNum++;
                }
            }
            if (record != recordEOF) {
                if (record != null) {
                    scoring = scoreRecord(grouperFactory, record, true, commandLineParams);
                } else {
                    scoring = scoringBLANK;
                }
                writeOutput(outWriter, scoring, idx++, commandLineParams);
                displayDetails(scoring, record);
            }
        } else if (commandLineParams.getFirstRecord() > 0) {
            int firstRecord = commandLineParams.getFirstRecord();
            int recsToScore = commandLineParams.getLastRecord() - firstRecord;

            // skip to the first record
            while (firstRecord-- > 0 && (record = readRecord(inReader, idx++)) != recordEOF) {
                if (this.isRecordIncludingCodes(record, commandLineParams.getSkipWithCodes())) {
                    // add one back to the record count since we are not 
                    // scoring this record
                    firstRecord++;
                }
            }

            if (record != recordEOF && record != null) {
                idx = commandLineParams.getFirstRecord();
                do {
                    if (this.isRecordIncludingCodes(record, commandLineParams.getSkipWithCodes())) {
                        // add one back to the record count since we are not 
                        // scoring this record
                        recsToScore++;
                    } else {
                        scoring = scoreRecord(grouperFactory, record, false, commandLineParams);
                        displayEdits(commandLineParams, scoring, idx);
                        writeOutput(outWriter, scoring, idx++, commandLineParams);
                    }
                } while (recsToScore-- > 0 && (record = readRecord(inReader, idx)) != recordEOF);
            }
            // reset the idx to the number of records scored
            idx = commandLineParams.getLastRecord() - commandLineParams.getFirstRecord() + 1 - recsToScore;
        } else {
            while ((record = readRecord(inReader, idx)) != recordEOF) {
                if (record == null) {
                    scoring = scoringBLANK;
                } else {
                    if (this.isRecordIncludingCodes(record, commandLineParams.getSkipWithCodes())) {
                        continue;
                    } else {
                        // score the record
                        scoring = scoreRecord(grouperFactory, record, false, commandLineParams);
                    }
                }

                writeOutput(outWriter, scoring, idx, commandLineParams);
                displayEdits(commandLineParams, scoring, idx++);
            }
        }
        return --idx;
    }

    public void writeOutput(Writer writer, ScoringResultsIF scoring,
            int curRecNum, GrouperTestParams commandLineParams) {

        try {
            writer.write(ScoringResultsFormatter.formatTestingScore(scoring, curRecNum,
                    commandLineParams.isHideTAC(), commandLineParams.isHideVersion()));
            writer.write(System.getProperty("line.separator"));

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        }
    }

    public void writeOutput(Writer writer, HomeHealthRecordIF record,
            int curRecNum) {

        try {
            writer.write(ScoringResultsFormatter.formatRecordNumber(curRecNum, true));
            writer.write(": ");
            if (record == null) {
                System.out.println("Rec null");
            } else {
                writer.write(record.toString());
            }
            writer.write(System.getProperty("line.separator"));
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        }
    }

    public void displayEdits(GrouperTestParams commandLineParams, ScoringResultsIF scoring,
            int recId) {
        if (commandLineParams.isShowEdits()) {
            final CollectionValidationEditsIF edits = scoring.getValidationEdits();
            final StringBuilder buffer = new StringBuilder(500);
            OasisEditIF oasisEdit;
            OasisDataItemIF dataItem;

            buffer.append("***** Edits for Rec: ");
            buffer.append(recId);
            buffer.append(" *****");
            buffer.append(System.getProperty("line.separator"));
            if (edits != null && !edits.isEmpty()) {
                for (OasisValidationEditIF edit : edits) {
                    oasisEdit = edit.getEdit();
                    dataItem = edit.getOasisDataItem();

                    buffer.append("  edit: ");
                    buffer.append(oasisEdit.getId());
                    buffer.append(", item: ");
                    buffer.append(dataItem.getKey());
                    buffer.append("='");
                    buffer.append(dataItem.getValue());
                    buffer.append("'");
                    buffer.append(", desc: ");
                    buffer.append(oasisEdit.getDescription());

                    System.out.println(buffer.toString());

                    buffer.setLength(0);
                }
            } else {
                Collection<HomeHealthEventIF> scoreEvents = scoring.getScoringEvents();
                if (scoreEvents != null) {
                    for (HomeHealthEventIF scoreEvent : scoreEvents) {
                        if (scoreEvent.getEventId() == EventId_EN.VALIDATION_ISSUE) {
                            buffer.append("  validation issue: ");
                            buffer.append(scoreEvent.getMessage());

                            System.out.println(buffer.toString());

                            buffer.setLength(0);
                        }
                    }
                }
            }
        }
    }

    public ScoringResultsIF scoreRecord(
            HomeHealthGrouperFactoryIF grouperFactory, HomeHealthRecordIF record,
            boolean showDetails,
            GrouperTestParams commandLineParams) {
        ScoringEventCollector scoringEventCollector = null;
        HomeHealthGrouperIF grouper;
        ScoringResultsIF scoring;

        try {
            grouper = grouperFactory.getGrouper(record);
            if (grouper == null) {
                scoring = scoringResultsEmpty;
            } else {
                final String testVersion = commandLineParams.getTestVersion();

                /*
                 * If testVersion is not null, then only test that version
                 */
                if (testVersion == null
                        || testVersion.isEmpty()
                        || grouper.getVersion().compareToIgnoreCase(testVersion) <= 0) {
                    if (commandLineParams.isUseNewScoreMethod()) {
                        Collection<HomeHealthEventListenerIF> lisetners = null;
                        CollectionValidationEditsIF validation = null;

                        if (showDetails) {
                            lisetners = new ArrayList<HomeHealthEventListenerIF>();
                            lisetners.add(new HHEventConsole());
                            if (grouper.getVersion().compareTo("V4115") < 0) {
                                scoringEventCollector = new ScoringEventCollector();
                                lisetners.add(scoringEventCollector);
                            }
                        }

                        if (commandLineParams.isTurnOffValidation()) {
                            // This will only turn off Oasis record 
                            validation = new CollectionValidationEdits_HhPpsOnly();
                        }

                        scoring = grouper.score(record, false, validation, lisetners);

                    } else {
                        scoringEventCollector = new ScoringEventCollector();
                        grouper.addEventListener(scoringEventCollector);

                        scoring = grouper.score(record, false);

                        grouper.removeEventListener(scoringEventCollector);
                    }
                    if (scoringEventCollector != null && !scoringEventCollector.getEvents().isEmpty()) {
                        scoring.setScoringEvents(scoringEventCollector.getEvents());
                    }
                } else {
                    scoring = scoringResultsEmpty;
                }
            }
        } catch (ServiceIssueException ex) {
            DataValidityFlag flag = new DataValidityFlag();
            flag.setServiceIssue(true);
            scoring = new ScoringResults(null, ex.getGrouper() != null ? ex.getGrouper().getVersion() : "",
                    flag, null, null, null);
            Logger.getLogger(HomeHealthGrouper_HP.class.getName()).log(Level.SEVERE, null, ex);
        }

        return scoring;
    }

    private boolean isRecordIncludingCodes(HomeHealthRecordIF record, Map<String, String> codes) {
        boolean recIncludes = false;
        if (codes != null) {
            DiagnosisCodeIF recCode;

            for (int idx = 0; idx < 18; idx++) {
                recCode = record.getDiagnosisCode(idx);
                if (!recCode.isEmpty()
                        && codes.get(recCode.getCode().trim()) != null) {
                    recIncludes = true;
                    break;
                }
            }
        }
        return recIncludes;
    }

    public void displayDetails(ScoringResultsIF scoring, HomeHealthRecordIF record) {
        final Logger logger = Logger.getLogger(getClass().getName());
        final String newLine = System.getProperty("line.separator");
        final StringBuilder buffer = new StringBuilder();

        buffer.append("ScoringResults{").append(newLine).append("\thippsCode=");
        buffer.append(scoring.getHIPPSCode());
        buffer.append(",").append(newLine).append("\tgrouperVersion=");
        buffer.append(scoring.getGrouperVersion());
        buffer.append(",").append(newLine).append("\tvalidityFlag=");
        buffer.append(scoring.getValidityFlag());
        buffer.append(",").append(newLine).append("\ttreatmentAuthorization=");
        buffer.append(scoring.getTreatmentAuthorization());
        buffer.append(",").append(newLine).append("\tclinicalValidator=");
        buffer.append(scoring.getClinicalValidator());
        buffer.append(",").append(newLine).append("\tnrsValidator=");
        buffer.append(scoring.getNrsValidator());
        buffer.append(",").append(newLine).append("\tvalidationEdits=");
        buffer.append(formatScoringEvents(scoring.getValidationEdits()));
        buffer.append(",").append(newLine).append("\tscoringEvents=");
        buffer.append(formatScoringEvents(scoring.getScoringEvents()));
        buffer.append(",").append(newLine).append("\tdiagnosisScoringStatus=");
        buffer.append(Arrays.toString(scoring.getDiagnosisScoringStatus()));
        buffer.append(",").append(newLine).append("\tnrsDiagnosisScoringStatus=");
        buffer.append(Arrays.toString(scoring.getNrsDiagnosisScoringStatus()));
        buffer.append(",").append(newLine).append("\texception=");
        buffer.append(scoring.getException());
        buffer.append("}").append(newLine);

        logger.log(Level.INFO, "HH-PPS: Score Details: {0}", buffer.toString());
    }

    public String formatScoringEvents(Collection events) {
        final String newLine = System.getProperty("line.separator");
        final StringBuilder buffer = new StringBuilder();
        if (events != null) {
            for (Object event : events) {
                buffer.append(event.toString());
                buffer.append(newLine);
            }
        }
        return buffer.toString();
    }

    public CommandLineParams getCommandLineParams(String args[]) {
        return new GrouperTestParams(args);
    }

    class GrouperTestParams extends CommandLineParams {

        boolean useNewScoreMethod;
        boolean turnOffValidation;
        boolean showEdits;
        String testVersion;

        public GrouperTestParams(String[] args) {
            super(args);
        }

        @Override
        public void parseExtra(String nameValue[]) {
            if ("useNewScoreMethod".equalsIgnoreCase(nameValue[0])) {
                this.useNewScoreMethod = nameValue.length == 1
                        || "true".equalsIgnoreCase(nameValue[1])
                        || "T".equalsIgnoreCase(nameValue[1])
                        || "yes".equalsIgnoreCase(nameValue[1])
                        || "Y".equalsIgnoreCase(nameValue[1]);
            } else if ("turnOffValidation".equalsIgnoreCase(nameValue[0])) {
                this.turnOffValidation = nameValue.length == 1
                        || "true".equalsIgnoreCase(nameValue[1])
                        || "T".equalsIgnoreCase(nameValue[1])
                        || "yes".equalsIgnoreCase(nameValue[1])
                        || "Y".equalsIgnoreCase(nameValue[1]);
            } else if ("showEdits".equalsIgnoreCase(nameValue[0])) {
                this.showEdits = true;
            } else if ("testVersion".equalsIgnoreCase(nameValue[0])) {
                this.testVersion = nameValue[1];
            }
        }

        @Override
        public void validateParams() {
            super.validateParams();

            if (this.turnOffValidation && !this.useNewScoreMethod) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "'turnOffValidation' can only be used when 'useNewScoreMethod' is true.");
            }
        }

        public boolean isUseNewScoreMethod() {
            return this.useNewScoreMethod;
        }

        public boolean isTurnOffValidation() {
            return this.turnOffValidation;
        }

        public boolean isShowEdits() {
            return showEdits;
        }

        @Override
        public List<String> getCommandOptions() {
            List<String> options;

            options = super.getCommandOptions();
            if (super.isShowExtraOptions()) {
                options.add("useNewScoreMethod[=true/T/Yes/Y] If no equals or blank, assumed to be true.  Any other value is assumed false. Default = false.");
                options.add("turnOffValidation[=true/T/Yes/Y] If no equals or blank, assumed to be true.  Any other value is assumed false. Default = false.");
                options.add("showEdits - Not required.  Shows the edit items whenever the validity flag is not 1. Default = false.");
                options.add("testVersion - Not required.  The version number to test such as v4115. Default is to test all versions");
            }

            return options;
        }

        public String getTestVersion() {
            return this.testVersion;
        }
    }
}
