/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.test;

import com.mmm.cms.homehealth.io.OasisRecordConverterIF;
import com.mmm.cms.homehealth.io.util.Oasis_B_RecordUtil;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This tests reading a set of Oasis records from a text file, based on the
 * Oasis Data Spec 1.60, and writing it back out to another file in order to
 * ensure that the reading and writing work properly.
 *
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class OasisRecord_B_ReadWriteTest {

	public final static String DURING_READTEST = "During ReadTest: ";
	public final static String ELAPSED_TIME = " elapsed time: ";
	public final static String SUFFIX = "_TESTOUT";
	private String inputFilename;
	private String outputFilename;
	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	
	
	public String getInputFilename() {
		return inputFilename;
	}

	public void setInputFilename(String inputFilename) {
		this.inputFilename = inputFilename;
	}

	public String getOutputFilename() {
		return outputFilename;
	}

	public void setOutputFilename(String outputFilename) {
		this.outputFilename = outputFilename;
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
		OasisRecord_B_ReadWriteTest tester;

		tester = new OasisRecord_B_ReadWriteTest();
		tester.runTests(args);
	}

	public int parseArguments(String args[]) {
		int count = 0;

		if (args.length > 0) {
			String configFilename = null;
			Pattern pattern = Pattern.compile("=");
			String tokens[];

			for (String arg : args) {
				tokens = pattern.split(arg);
				
				if ("CONFIG".equalsIgnoreCase(tokens[0])) {
					++count;
					configFilename = tokens[1];
				} else if ("OUTPUTFILE".equalsIgnoreCase(tokens[0])) {
					++count;
					this.outputFilename = tokens[1];
				} else if ("INPUT".equalsIgnoreCase(tokens[0])) {
					++count;
					this.inputFilename = tokens[1];
				}
			}

			if (this.outputFilename == null) {
				int idx = this.inputFilename.lastIndexOf(".");
				if (idx > -1) {
					this.outputFilename = this.inputFilename.substring(0, idx) + SUFFIX + inputFilename.substring(idx);
				} else {
					this.outputFilename = this.inputFilename + SUFFIX;
				}
			}

			if (configFilename != null) {
			this.properties = new Properties();
				try {
					this.properties.load(new FileInputStream(configFilename));
				} catch (IOException ex) {
					Logger.getLogger(OasisRecord_B_ReadWriteTest.class.getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER + "Could not open properties file: " + args[1], ex);
				}
			}

			System.out.println("Input file is: " + this.inputFilename);
			System.out.println("Output file is: " + this.outputFilename);
		} else {
			count = 0;
			System.out.println("Syntax: OasisRecordReadWriteTest \"input file name\" <output file name - optional>");
		}

		return count;
	}

	public void runTests(String args[]) {
		List<HomeHealthRecordIF> oasisRecords;
		long starttime;
		long elapsedTime;

		if (parseArguments(args) > 0) {
			try {
				int count;
				starttime = System.currentTimeMillis();
				count = readTest(new File(this.inputFilename));
				elapsedTime = System.currentTimeMillis() - starttime;
				System.out.println("Number of Records read: " + count + "," + ELAPSED_TIME + elapsedTime);

			} catch (FileNotFoundException ex) {
				Logger.getLogger(OasisRecord_B_ReadWriteTest.class.getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER + DURING_READTEST, ex);
				System.out.println(DURING_READTEST + ex);
				System.exit(2);
			} catch (IOException ex) {
				Logger.getLogger(OasisRecord_B_ReadWriteTest.class.getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER + DURING_READTEST, ex);
				System.out.println(DURING_READTEST + ex);
				System.exit(3);
			}

			try {
				starttime = System.currentTimeMillis();
				oasisRecords = readConvertTest(new File(inputFilename));
				elapsedTime = System.currentTimeMillis() - starttime;
				System.out.println("Number of Oasis Records read: " + oasisRecords.size() + "," + ELAPSED_TIME + elapsedTime);

				starttime = System.currentTimeMillis();
				writeTest(oasisRecords, new File(outputFilename));
				elapsedTime = System.currentTimeMillis() - starttime;
				System.out.println("Number of Oasis Records written: " + oasisRecords.size() + "," + ELAPSED_TIME + elapsedTime);

				starttime = System.currentTimeMillis();
				writeTestDelimitted(oasisRecords, new File(outputFilename + ".delimited.txt"), "\t");
				elapsedTime = System.currentTimeMillis() - starttime;
				System.out.println("Number of Oasis Records written: " + oasisRecords.size() + "," + ELAPSED_TIME + elapsedTime);

			} catch (FileNotFoundException ex) {
				Logger.getLogger(OasisRecord_B_ReadWriteTest.class.getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER + DURING_READTEST, ex);
				System.out.println(DURING_READTEST + ex);
				System.exit(2);
			} catch (IOException ex) {
				Logger.getLogger(OasisRecord_B_ReadWriteTest.class.getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER + DURING_READTEST, ex);
				System.out.println(DURING_READTEST + ex);
				System.exit(3);
			}
		}
	}

	public OasisRecordConverterIF getConverterReader() {
		return new Oasis_B_RecordUtil();
	}

	public OasisRecordConverterIF getConverterWriter() {
		return new Oasis_B_RecordUtil();
	}

	public int readTest(File input) throws FileNotFoundException, IOException {
		BufferedReader reader;
		FileReader fileReader;
		int idx = 1;

		// open the file
		fileReader = new FileReader(input);
		reader = new BufferedReader(fileReader);

		try {
			// read the records from the input
			while (reader.readLine() != null) {
				idx++;
			}
		} finally {
			reader.close();
		}

		return --idx;
	}

	public List<HomeHealthRecordIF> readConvertTest(File input) throws FileNotFoundException, IOException {
		BufferedReader reader;
		FileReader fileReader;

		// open the file
		fileReader = new FileReader(input);
		reader = new BufferedReader(fileReader, 16384);

		try {
			return readConvertTest(reader);
		} finally {
			reader.close();
		}
	}

	public List<HomeHealthRecordIF> readConvertTest(Reader reader) throws FileNotFoundException, IOException {
		BufferedReader bufReader;
		String line;
		HomeHealthRecordIF record;
		List<HomeHealthRecordIF> records;
		int idx = 1;
		OasisRecordConverterIF recordConverter = getConverterReader();

		// open the file
		if (reader instanceof BufferedReader) {
			bufReader = (BufferedReader) reader;
		} else {
			bufReader = new BufferedReader(reader, 16384);
		}

		records = new ArrayList<HomeHealthRecordIF>();

		try {
			// read the records from the input
			while ((line = bufReader.readLine()) != null) {
				try {
					// convert them to Oasis Records and add them to the list
					if (idx == 14714) {
						System.out.println("Found input record number " + idx);
					}
					record = recordConverter.convertToHomeHealthRec(line, idx++);
					records.add(record);
				} catch (ParseException ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER + "Could convert record: " + ex, ex);
				}
			}
		} finally {
			reader.close();
		}

		return records;
	}

	public void writeTest(List<HomeHealthRecordIF> oasisRecords, File output) throws IOException {
		BufferedWriter writer;
		FileWriter fileWriter;
		StringBuilder line;
		OasisRecordConverterIF recordConverter = getConverterWriter();
		int count = 0;
    	Logger logger = Logger.getLogger(getClass().getName());

		// open the file
		fileWriter = new FileWriter(output);
		writer = new BufferedWriter(fileWriter);

		try {
			// read the records from the input
			for (HomeHealthRecordIF record : oasisRecords) {
				// convert them from Oasis Records and write them to the file
				count++;
				try {
					line = recordConverter.convertFromHomeHealthRec(record);
					writer.write(line.toString());
				} catch (java.lang.IllegalArgumentException ex) {
					logger.log(Level.SEVERE, "Record with error: " + record);
					logger.log(Level.SEVERE, "Error Writing record item: " + count, ex);
				}
			}
		} finally {
			writer.close();
		}
		
		logger.log(Level.INFO, "Number of records written: {0}", count);
	}

	public void writeTestDelimitted(List<HomeHealthRecordIF> oasisRecords, File output, String delimiter) throws IOException {
		BufferedWriter writer;
		FileWriter fileWriter;
		StringBuilder line;
		OasisRecordConverterIF recordConverter = getConverterWriter();

		// open the file
		fileWriter = new FileWriter(output);
		writer = new BufferedWriter(fileWriter);

		try {
//			writer.write(recordConverter.toHeaderOasisRecDelimeted(delimiter).toString());
			// read the records from the input
			for (HomeHealthRecordIF record : oasisRecords) {
				// convert them from Oasis Records and write them to the file
				line = recordConverter.convertFromHomeHealthRecDelimeted(record, delimiter);
				writer.write(line.toString());
			}
		} finally {
			writer.close();
		}
	}
}
