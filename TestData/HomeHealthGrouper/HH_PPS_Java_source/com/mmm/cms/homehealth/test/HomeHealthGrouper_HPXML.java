/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.test;

import com.mmm.cms.homehealth.proto.HomeHealthGrouperFactoryIF;
import static com.mmm.cms.homehealth.test.CommonTester.logger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This tests reading an OASIS-C/C1 set of records and scoring them using the
 * appropriate grouper provided by the grouper factory.
 *
 * This testing module represents the best example of how to access the Java
 * HHRG directly, without using the DLL bridge.
 *
 * There are parameters for this module that allow the details of a single
 * record to be displayed to the console. Refer to the inner class
 * CommandLineParams for more details.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthGrouper_HPXML extends HomeHealthGrouper_HP {

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
        CommonTester tester;

        tester = new HomeHealthGrouper_HPXML();
        tester.runTest(args);
    }

    /**
     * This version expects that the "input file" is a file with the list of
     * files that contain XML records for OASIS-C/C1. So, the file is opened and
     * read one line at a time to get the filename. Then those files are opened
     * and sent to the underlying class
     *
     * @param params
     * @param grouperFactory
     * @param inputFilename
     * @param outputFilename
     */
    @Override
    public void runTest(CommandLineParams params, HomeHealthGrouperFactoryIF grouperFactory, 
            File inputFilename, File outputFilename) {
        BufferedReader xmlFileListReader = null;
        BufferedReader bufReader = null;
        File xmlInputFile;
        Writer writer = null;
        String filename;

        try {
            writer = new BufferedWriter(new FileWriter(outputFilename), 57920);
            xmlFileListReader = new BufferedReader(new FileReader(inputFilename), 57920);

            while ((filename = xmlFileListReader.readLine()) != null) {
                xmlInputFile = new File(filename);
                bufReader = new BufferedReader(new FileReader(xmlInputFile), 9680);
                
                /*
                * before calling the super class, we need to reset the "input
                * file" parameter so that it sees that it is an XML file
                */
                params.setInputFile(xmlInputFile);

                runTest(bufReader, writer, grouperFactory, params);
                bufReader.close();
            }
            
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(HomeHealthGrouper_HPXML.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(HomeHealthGrouper_HPXML.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (xmlFileListReader != null) {
                try {
                    xmlFileListReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(HomeHealthGrouper_HPXML.class.getName()).log(Level.SEVERE, null, ex);
                }
            }                        
        }
    }

}

