/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.test;


import com.mmm.cms.homehealth.io.OasisRecordConverterIF;
import com.mmm.cms.homehealth.io.util.Oasis_C1_RecordUtil_v2_11;

/**
 * This tests reading a set of Oasis records from a text file, based on the
 * Oasis Data Spec 1.60, and writing it back out to another file in order to
 * ensure that the reading and writing work properly.
 *
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class OasisRecord_C1_ReadWriteTest extends OasisRecord_B_ReadWriteTest {

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

		OasisRecord_C_ReadWriteTest tester;
		tester = new OasisRecord_C_ReadWriteTest();
		tester.runTests(args);
	}

	@Override
	public OasisRecordConverterIF getConverterReader() {
		return new Oasis_C1_RecordUtil_v2_11();
	}

	@Override
	public OasisRecordConverterIF getConverterWriter() {
		return new Oasis_C1_RecordUtil_v2_11();
	}

	
}
