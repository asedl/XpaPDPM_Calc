/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.pps;

import com.mmm.cms.homehealth.HomeHealthGrouperFactory;
import com.mmm.cms.homehealth.ScoringResults;
import com.mmm.cms.homehealth.io.OasisReaderFactory;
import com.mmm.cms.homehealth.io.OasisRecordConverterIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the high level entry point into the HH-PPS using a singleton
 * architecture; Use the method getInstance() to obtain the object, and then
 * call init() to ensure it is ready to run; After calling init(), you can call
 * scoreRecord() as many times as needed.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public final class HH_PPS {

	/**
	 * set the interval default to 2 minutes This item can be controlled within
	 * the properties file with gcInterval=120000 with the value being in
	 * milliseconds
	 */
	private long gcInterval = 120000;
	private transient long lastGC;
	protected boolean initComplete;
	protected HomeHealthGrouperFactory homeHealthFactory;
	protected OasisReaderFactory readerFactory;
	final static protected HH_PPS gHhPps = new HH_PPS();

	public static HH_PPS getInstance() {
		return gHhPps;
	}

	private HH_PPS() {
		this.readerFactory = new OasisReaderFactory();
	}

	public void init() {
		init((Properties) null);
	}

	public void init(File propertiesFile) {
		try {
			Properties properties;
			FileReader reader;

			properties = new Properties();
			reader = new FileReader(propertiesFile);
			properties.load(reader);
			init(properties);
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: init() - no not load properties file: '" + propertiesFile + "'", ex);
		}
	}

	/**
	 * This initializes the Java Grouper version for use within a CPP-DLL. It
	 * use environment variables in order to configure the grouper.
	 *
	 * @param properties - this can filled with the properties information used
	 * by the HomeHealthGrouperFactory. Refer to that class for details on
	 * properties values. If this is null, then the default property information
	 * is used.
	 */
	public void init(Properties properties) {
		if (!this.initComplete) {
			try {
				this.homeHealthFactory = new HomeHealthGrouperFactory();

				this.homeHealthFactory.init(properties);
                                if (properties == null) {
                                    properties = this.homeHealthFactory.getProperties();
                                }
				this.initComplete = true;
    				this.readerFactory.init(properties);
                                
				if (properties != null) {
					String strGcInterval = properties.getProperty("gcInterval");
					if (strGcInterval != null) {
						try {
							this.gcInterval = Long.parseLong(strGcInterval);
						} catch (NumberFormatException ex) {
							// don't care
						}
					}
				}
			} catch (Exception ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: init() ", ex);
			}
		}
	}

	/**
	 * This convenience method calls scoreRecord(String record, boolean
	 * validateDates, CollectionValidationEditsIF validationEdits,
	 * Collection<HomeHealthEventListenerIF> listeners) with (record, true,
	 * null,null)
	 *
	 * @see ScoringResultsIF scoreRecord(String record, boolean validateDates,
	 * CollectionValidationEditsIF validationEdits,
	 * Collection<HomeHealthEventListenerIF> listeners)
	 *
	 * @param record
	 * @return
	 */
	public ScoringResultsIF scoreRecord(String record) {
		return scoreRecord(record, true, null, null);
	}

	/**
	 * Scores the record using the supplied validation information (if non-null)
	 * instead of using the built in validation capabilities.
	 *
	 * @param record - string format of the OASIS-B, OASIS-C original flat file,
	 * OASIS-C XML, OASIS-C1 flat file or OASIS-C1 XML
	 * @param validateDates - if true, then dates are strictly enforced
	 * @param validationEdits - if non-null, then this information is used
	 * during scoring as the validation information, otherwise, if null, the
	 * built in validation is performed, i.e. the HH-PPS will call the VUT to
	 * validate.
	 * @param listeners - can be null, otherwise list of listeners for notified
	 * of events
	 *
	 * @return non-null scoring details which includes the traditional HIPPS,
	 * Treatment Authorization, version name, and flags, plus the Validation
	 * information (either the one supplied on the method call or the results of
	 * the built in validation) and scoring process information (e.g. what item
	 * scored what value)
	 */
	public ScoringResultsIF scoreRecord(String record, boolean validateDates,
			CollectionValidationEditsIF validationEdits, Collection<HomeHealthEventListenerIF> listeners) {

		HomeHealthRecordIF homeHealthRecord;
		ScoringResultsIF results;

		try {
			if (initComplete) {
				/*
				 * this ensures that any gc interval less than 1 second is not
				 * worth doing - so the system will do the gc when wanted. Then
				 * it compares the last gc to the interval
				*/
				if (this.gcInterval >= 1000 &&
					System.currentTimeMillis() - this.lastGC > this.gcInterval) {
					System.gc();
					this.lastGC = System.currentTimeMillis();
				}

				// convert the incoming record and score it
				homeHealthRecord = convertRecord(record);
				results = scoreRecord(homeHealthRecord, validateDates, validationEdits, listeners);
			} else {
				throw new Exception(getClass().getName() + ".init() must be called once prior to calling scoreRecord()");
			}
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: Exception: "
					+ e.toString(), e);
			results = new ScoringResults();
			results.setException(e);
		}

		return results;

	}

	/**
	 * Scores the record using the supplied validation information (if non-null)
	 * instead of using the built in validation capabilities.
	 *
	 * @param homeHealthRecord
	 * @param validateDates
	 * @param validationEdits - if non-null, then this information is used
	 * during scoring as the validation information, otherwise, if null, the
	 * built in validation is performed.
	 * @param listeners - can be null, otherwise list of listeners for notified
	 * of events
	 *
	 * @return non-null scoring details which includes the traditional HIPPS,
	 * Treatment Authorization, version name, and flags, plus the Validation
	 * information (either the one supplied on the method call or the results of
	 * the built in validation) and scoring process information (e.g. what item
	 * scored what value)
	 */
	public ScoringResultsIF scoreRecord(HomeHealthRecordIF homeHealthRecord, boolean validateDates,
			CollectionValidationEditsIF validationEdits, Collection<HomeHealthEventListenerIF> listeners) {

		ScoringResultsIF results;
		HomeHealthGrouperIF grouper;

		try {
			if (initComplete) {
				grouper = homeHealthFactory.getGrouper(homeHealthRecord);
				if (grouper != null) {
					// Grouper found, so score the record
					results = grouper.score(homeHealthRecord, validateDates, validationEdits, listeners);
				} else {
					results = new ScoringResults();
					results.setException(new Exception("No Grouper found for record."));
				}

			} else {
				throw new Exception(getClass().getName() + ".init() must be called once prior to calling scoreRecord()");
			}
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: Exception: "
					+ e.toString(), e);
			results = new ScoringResults();
			results.setException(e);
		}

		return results;
	}

	/**
	 * returns a HomeHealth record unless end of file, or an exception in
	 * reading the file or parsing the record.
	 *
	 * @param strRecord
	 * @return
	 */
	public HomeHealthRecordIF convertRecord(String strRecord) {
		HomeHealthRecordIF record = null;
		OasisRecordConverterIF recordConverter;

		recordConverter = readerFactory.getRecordConverter(strRecord);
		if (recordConverter != null) {
			try {
				record = recordConverter.convertToHomeHealthRec(strRecord, 1, true);
			} catch (ParseException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: Could not convert record with cnoverter: " + recordConverter.getClass().getName(), ex);
			}
		}

		return record;
	}
}
