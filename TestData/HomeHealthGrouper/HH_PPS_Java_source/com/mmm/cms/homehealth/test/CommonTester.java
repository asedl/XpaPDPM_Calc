/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.test;

import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.HomeHealthGrouperFactory;
import com.mmm.cms.homehealth.HomeHealthRecord;
import com.mmm.cms.homehealth.io.OasisReaderFactory;
import com.mmm.cms.homehealth.io.OasisRecordConverterIF;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperFactoryIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tim Gallagher, 3M HIS, Clinical & Economic Research
 */
public abstract class CommonTester {

	final static Logger logger = Logger.getLogger(CommonTester.class.getName());
	protected CommandLineParams params;
	protected OasisReaderFactory readerFactory;
	public HomeHealthRecordIF recordEOF = new HomeHealthRecord();

	public CommonTester() {
		readerFactory = new OasisReaderFactory();
	}

	public String getOutFileSuffix() {
		return "_TESTOUT";
	}

	/**
	 * returns a HomeHealth record unless end of file, or an exception in
	 * reading the file or parsing the record.
	 *
	 * @param inReader
	 * @param curRecNum
	 * @return
	 */
	public HomeHealthRecordIF readRecord(BufferedReader inReader, int curRecNum) {
		HomeHealthRecordIF record = null;
		OasisRecordConverterIF recordConverter;

		try {
			String strRecord;

			/*
			 * check for XML file
			 */
			if (params.getInputFile().getName().toLowerCase().contains(".xml")) {
				/*
				 * Since the file is an xml, it may be multi-lined so 
				 * need to read the entire file here into a string and
				 * let the converter read all the data.  There should only
				 * be one record per file
				 */
				final StringBuilder buffer = new StringBuilder();

				while ((strRecord = inReader.readLine()) != null) {
					buffer.append(strRecord);
				}
				strRecord = buffer.length() > 0 ? buffer.toString() : null;

			} else {
				strRecord = inReader.readLine();
			}

			if (strRecord != null) {
				recordConverter = readerFactory.getRecordConverter(strRecord);
				if (recordConverter != null) {
					record = recordConverter.convertToHomeHealthRec(strRecord, curRecNum, true);
				}
			} else {
				record = recordEOF;
			}
		} catch (ParseException ex) {
			logger.log(Level.SEVERE, "HH-PPS: Record " + curRecNum, ex);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "HH-PPS: Record " + curRecNum, ex);
		}

		return record;
	}

	public void runTest(String args[]) {
		long overallStarttime;
		long elapsedTime;

		params = getCommandLineParams(args);

		printOptions(params.getCommandOptions());

		if (params.getInputFile() == null || !(new File(params.getInputFile().getPath())).exists()) {
			throw new IllegalArgumentException("Missing InputFile: \n" + params.getInputFile().getPath() + "\n"
                                + params.getSyntax(this.getClass()));
		} else {
			Properties properties = null;

			overallStarttime = System.currentTimeMillis();
			if (params.getPropertiesFile() != null) {
				try {
					properties = new Properties();
					properties.load(new FileInputStream(params.getPropertiesFile()));
				} catch (IOException ex) {
					logger.log(Level.SEVERE, "HH-PPS: Could not open properties file: "
							+ args[1], ex);
				}
			}

			runTest(params, properties);

			elapsedTime = System.currentTimeMillis() - overallStarttime;

			logger.log(Level.INFO, "Overall Elapsed time: {0}", elapsedTime);
		}
	}

	public void runTest(CommandLineParams params, Properties properties) {
		HomeHealthGrouperFactoryIF grouperFactory;
		File inputFilename;
		File outputFilename;
		String tmpStr;

		grouperFactory = new HomeHealthGrouperFactory();
		if (properties != null) {
			final String basePath = params.getBasePath();

			// Adjust the basePath based on the command line parameter
			if (basePath != null && !basePath.isEmpty()) {
				properties.setProperty(GrouperDataManager.PROPERTY_MASTER_NAME_BASE_PATH, basePath);
			}

			try {
				grouperFactory.init(properties);
			} catch (RemoteException ex) {
				logger.log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
			} catch (Exception ex) {
				logger.log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
			}

			try {
				this.readerFactory.init(properties);
			} catch (Exception ex) {
				logger.log(Level.SEVERE, null, ex);
			}
		}

		inputFilename = params.getInputFile();
		outputFilename = params.getOutputFile();
		if (outputFilename == null) {
			tmpStr = inputFilename.getAbsolutePath();
			final int idx = tmpStr.lastIndexOf('.');
			if (idx > -1) {
				outputFilename = new File(tmpStr.substring(0, idx) + getOutFileSuffix()
						+ tmpStr.substring(idx));
			} else {
				outputFilename = new File(tmpStr + getOutFileSuffix());
			}
		}
		System.out.println("Input file is: " + inputFilename.getAbsolutePath());
		System.out.println("Output file is: " + outputFilename.getAbsolutePath());

		runTest(params, grouperFactory, inputFilename, outputFilename);

	}

	public void runTest(CommandLineParams params, HomeHealthGrouperFactoryIF grouperFactory,
			File inputFilename, File outputFilename) {
		BufferedReader bufReader;
		Writer writer;

		try {
			bufReader = new BufferedReader(new FileReader(inputFilename), 57920);
			writer = new BufferedWriter(new FileWriter(outputFilename), 57920);

			runTest(bufReader, writer, grouperFactory, params);

			writer.flush();
			bufReader.close();
			writer.close();
		} catch (FileNotFoundException ex) {
			logger.log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
	}

        /**
         * Allows an extended class to create their own parameter lists
         * 
         * @param args
         * @return 
         */
	public CommandLineParams getCommandLineParams(String args[]) {
		return new CommandLineParams(args);
	}

        /**
         * Other classes must implement this to perform the actual work
         * 
         * @param bufReader
         * @param writer
         * @param grouperFactory
         * @param commandLine 
         */
	public abstract void runTest(BufferedReader bufReader,
			Writer writer, HomeHealthGrouperFactoryIF grouperFactory,
			CommandLineParams commandLine);

        /**
         * Prints the options available for this module
         * @param options 
         */
	public void printOptions(List<String> options) {
		System.out.println("Options:");
		for (String str : options) {
			System.out.print("\t");
			System.out.println(str);
		}
		System.out.println();
	}
}
