/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperFactoryIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.InvalidDateException;
import com.mmm.cms.homehealth.proto.ServiceIssueException;

import com.mmm.cms.util.MacroProperties;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * This Factory holds as set of Groupers and allows selection of a Grouper
 * version by checking the Home Health record validity against
 * isValidForVersion() method of each available Grouper. The order of the
 * Groupers for the isValidForVersion() check is the same order that the classes
 * are loading into the Factory.
 *
 * The properties file should include the following properties: <ul>
 * <li>home.health.grouper.class.list - a space separated list of full
 * HomeHealthGrouperIF implemented classes. The class will be loaded into the
 * Factory in the order that they are listed.</li> <li>[HomeHealthGrouperIF full
 * class name].config - this indicates the properties file associated with the
 * Home Health Grouper identified in the class list.</li> </ul>
 *
 *
 * Version v3413
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthGrouperFactory implements HomeHealthGrouperFactoryIF {

	/**
	 * Initialization property for defining the grouper class names to
	 * dynamically load into the factory. Property name:
	 * home.health.grouper.class.list
	 */
	public final static String PROPERTY_HH_CLASS_LIST = "home.health.grouper.class.list";
	/**
	 * Initialization property for defining the location of the configuration
	 * file for the grouper classes that are loaded dynamically. Property name:
	 * .config Use this with the grouper class name, for example:
	 * com.mmm.cms.homehealth.v2409_1.GrouperVersion_v2409_1.config={Filename}
	 *
	 */
	public final static String PROPERT_CONFIG_SUFFIX = ".config";
	/**
	 * Initialization property for defining which grouper class dynamically is
	 * used as the default grouper when no grouper can be found that can handle
	 * the current record. The default value is null, meaning that there is no
	 * default grouper. Default groupers can only be used if the M090 Date is
	 * not before the grouper's effective start date. Therefore, a record in
	 * OASIS-B format can not be grouped by a grouper that only works with
	 * OASIS-C records. Property name: default.grouper.class.name
	 */
	public final static String PROPERTY_DEFAULT_GROUPER_CLASS_NAME = "default.grouper.class.name";
	/**
	 * Initialization property for defining a default grouper version prefix
	 * that is used in conjunction with the default grouper class name. When the
	 * default grouper is used, and this prefix is not null (the default value),
	 * the version prefix is used as the first character in the grouper's
	 * version identifier to help identify that the grouper used was the default
	 * not just the one used by being valid for the record.
	 */
	public final static String PROPERTY_DEFAULT_VERSION_PREFIX = "default.version.prefix";

	/**
	 * Initialization property for telling the Grouper Factory present a pop-up
	 * dialog that visually lets the user know the HH-PPS is initializing, and
	 * to wait a moment. The values are "true" (do not show popup) and "false"
	 * (show pop up). The default is true - no popup
	 */
	public final static String PROPERTY_HOME_HEALTH_QUIET = "home_health_quiet";

	/**
	 * Initialization property suffix for reconfiguring the start date. Within
	 * the properties file, the grouper class name and this suffix would be
	 * specified with a date value in order to override the default start date
	 * for the grouper. For example, setting the start date of version v4115 to
	 * January 1, 2015 within the config file would be as follows:
	 * com.mmm.cms.homehealth.v4115.GrouperVersion_v4115.start.date=20150101.
	 * The default for a version is defined in the constructor of the class in
	 * the Java source.
	 */
	public static final String PROPERTY_START_DATE_SUFFIX = ".start.date";

	/**
	 * Initialization property suffix for reconfiguring the end date. Within the
	 * properties file, the grouper class name and this suffix would be
	 * specified with a date value in order to override the default start date
	 * for the grouper. For example, setting the start date of version v4115 to
	 * September 30, 2015 within the config file would be as follows:
	 * com.mmm.cms.homehealth.v4115.GrouperVersion_v4115.end.date=20150930 The
	 * default for a version is defined in the constructor of the class in the
	 * Java source.
	 */
	public static final String PROPERTY_END_DATE_SUFFIX = ".end.date";

	/**
	 * Internal list of available Grouper versions
	 */
	private List<HomeHealthGrouperIF> homeHealthGroupers;
	private transient HomeHealthGrouperIF defaultGrouper;
	private transient JFrame progressFrame = null;
	private Properties properties;
	private boolean quiet = true;
	protected String defaultGrouperClassName;
	protected String defaultVersionPrefix;

	/**
	 * Constructor the initializes the Home Health Groupers list
	 */
	public HomeHealthGrouperFactory() {
		homeHealthGroupers = new ArrayList<HomeHealthGrouperIF>();
	}

	/**
	 * Get the value of defaultGrouperClassName
	 *
	 * @return the value of defaultGrouperClassName
	 */
	public String getDefaultGrouperClassName() {
		return defaultGrouperClassName;
	}

	/**
	 * Get the value of defaultVersionPrefix
	 *
	 * @return the value of defaultVersionPrefix
	 */
	public String getDefaultVersionPrefix() {
		return defaultVersionPrefix;
	}

	/**
	 * Searches the list of Home Health Groupers to determine which one will
	 * handle the record based on the dates on the record.
	 *
	 * If the Therapy Need N/A is yes (1), return null - i.e. don't bother
	 * looking for a Grouper version.
	 *
	 * Version V3210 introduced the default grouper, which can be configured in
	 * the HomeHealthGrouper.properties file using the
	 * PROPERTY_DEFAULT_GROUPER_CLASS_NAME. If no group is found to encompass
	 * the Info complete date, then the default group, if configured, will be
	 * used for scoring.
	 *
	 * Version V3413 removes the default grouper configuration option due to its
	 * proximity to the ICD-10 mandatory usage date. If V3413 is not configured
	 * as one of the grouper engines, then the default group version may still
	 * apply.
	 *
	 * @param record
	 * @return HomeHealthGrouperIF for scoring the record or null if no Grouper
	 * can be determined a valid to score a record.
	 * @throws ServiceIssueException
	 */
	@Override
	public HomeHealthGrouperIF getGrouper(HomeHealthRecordIF record) throws ServiceIssueException {
            if(record == null) {
                return null;
            }
		HomeHealthGrouperIF validGrouper = null;
		boolean v3413Found = false;

		try {
			if (record.getSTART_CARE_DT() != null && record.getSTART_CARE_DT().get(Calendar.MONTH) >= 0
					&& record.getINFO_COMPLETED_DT() != null && record.getINFO_COMPLETED_DT().get(Calendar.MONTH) >= 0) {
				for (HomeHealthGrouperIF grouper : homeHealthGroupers) {
					if ("V3413".compareTo(grouper.getVersion()) <= 0) {
						v3413Found = true;
					}

					try {
						if (grouper.isValidForVersion(record)) {
							validGrouper = grouper;
							break;
						}
					} catch (InvalidDateException ex) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
					}
				}

				/*
				 * don't do the default configured grouper if V3413 or later has
				 * been configured as one of the grouper versions.  This will allow
				 * testing of the older versions as a default when V3413 is not used.
				 * 
				 * if no grouper is found, but there is a default grouper name
				 * defined then use the default grouper
				 */
				if (!v3413Found
						&& validGrouper == null && this.defaultGrouper != null
						&& (this.defaultGrouper.getEffectiveDateStart().equals(record.getINFO_COMPLETED_DT())
						|| this.defaultGrouper.getEffectiveDateStart().before(record.getINFO_COMPLETED_DT()))) {
						// ensure that the records M090 value is not prior
					// to the groupers effective start date
					validGrouper = this.defaultGrouper;
				}
			}
		} catch (IllegalArgumentException ex) {
			/*
			 * this exception happens when the Calendar has a strict
			 * Date criteria and the get() method is called. A strict
			 * Date is not accepting dates like February 29 in an odd 
			 * year.  A lenient Date would roll the date to March 1.
			 */
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: Could not determine a grouper for record because the Start of Care or Information Complete dates are not strict dates and may 'roll over' to an unintended date value");
		}

		if (validGrouper != null && "1".equals(record.getTHER_NEED_NA())) {
			throw new ServiceIssueException(validGrouper, "Therapies not needed");
		}
		
		return validGrouper;
	}

	/**
	 * Searches the list of Home Health Groupers to determine which matches the
	 * versionId
	 *
	 * @param record
	 * @return HomeHealthGrouperIF or null if no Grouper can be found for the
	 * versionId.
	 */
	@Override
	public HomeHealthGrouperIF getGrouper(String versionId) {
		HomeHealthGrouperIF validGrouper = null;

		for (HomeHealthGrouperIF grouper : homeHealthGroupers) {
			if (grouper.getVersion().equalsIgnoreCase(versionId)) {
				validGrouper = grouper;
				break;
			}
		}

		return validGrouper;
	}

	/**
	 * This returns a non-null list of HomeHealthGrouperIF objects. The list may
	 * have a size of 0 if no groupers are available
	 *
	 * @return non-null List of HomeHealthGrouperIF objects.
	 */
	@Override
	public List<HomeHealthGrouperIF> getGroupers() {
		return homeHealthGroupers;
	}

	/**
	 * This will load the Home Health Groupers in the order that they are listed
	 * in the 'home.health.grouper.class.list' of the Properties, then it will
	 * pass the properties files associated with that group by the
	 * classname.config option. The classes are listed with a space between them
	 * and must be the full class name.
	 *
	 * If the Properties is null, then it calls getPropertiesFromEnv() in order
	 * to load the default supplied by the environment variable:
	 * <code>PROPERTY_HOME_HEALTH_CONFIG</code>
	 *
	 * @param props
	 * @throws java.lang.Exception
	 * @throws java.rmi.RemoteException
	 * @see com.mmm.cms.homehealth.proto.HomeHealthGrouperFactoryIF
	 * @see
	 * com.mmm.cms.homehealth.HomeHealthGrouperFactory#getPropertiesFromEnv()
	 */
	@Override
	public void init(Properties props) throws Exception, RemoteException {
		Properties grouperProps;
		String tmpStr;
		String classes[];
		HomeHealthGrouperIF grouper;

		try {
			// accommodate a null property set
			if (props == null) {
				// try to get the properties from the default locations
				getPropertiesFromEnv();
			} else {
				// make sure that we are using the Macro Properties so that
				// the macros can be replaced within the property
				if (props instanceof MacroProperties) {
					properties = props;
				} else {
					properties = new MacroProperties(props);
				}
			}

			this.defaultGrouperClassName = properties.getProperty(PROPERTY_DEFAULT_GROUPER_CLASS_NAME);

			// get the version prefix and fix its length if necessary
			this.defaultVersionPrefix = properties.getProperty(PROPERTY_DEFAULT_VERSION_PREFIX);
			if (this.defaultVersionPrefix != null && this.defaultVersionPrefix.length() > 1) {
				this.defaultVersionPrefix = this.defaultVersionPrefix.substring(0, 1);
			}

			tmpStr = properties.getProperty(PROPERTY_HOME_HEALTH_QUIET, "false");
			if ("false".equalsIgnoreCase(tmpStr)) {
				quiet = false;
			}

			tmpStr = properties.getProperty(PROPERTY_HH_CLASS_LIST);
			if (tmpStr != null) {
				// create the progress bar
				startProgressBar();

				// now init the factory...
				classes = tmpStr.split(" ");
				for (String className : classes) {
					className = className.trim();
					homeHealthGroupers.add(loadGrouper("", className));
				}
			} else {
				throw new Exception("HomeHealthGrouperFactory no class listing found in configuration properties (name = '"
						+ PROPERTY_HH_CLASS_LIST + "'");
			}
                        
                        /*
                         * not sure this is the right place to do this, but
                         * seems good
                         * Initialize the Record Validator Factory.
                        */
                        RecordValidatorFactory.getInstance(properties);
		} finally {
			endProgressBar();
		}
	}

	private void setDefaultGrouperVersion(HomeHealthGrouperIF grouper) {
		if (this.defaultVersionPrefix != null) {
			grouper.setVersion(this.defaultVersionPrefix + grouper.getVersion().substring(1));
		}
	}

	/**
	 * This finds the configuration/properties file that is defined by the
	 * environment variable
	 * <code>String PROPERTY_HOME_HEALTH_CONFIG = "home_health_config"</code> If
	 * the environment variable is not set then it will use the default
	 * properties file defined as
	 * <code>config/HomeHealthGrouper.properties</code>
	 *
	 * @return the Properties loaded from the found configuration/properties
	 * file, or null if the environment variable was not set, or the file was
	 * not file, or some other I/O exception.
	 * @see com.mmm.cms.homehealth.proto.HomeHealthGrouperFactoryIF
	 */
	public Properties getPropertiesFromEnv() {
		String configFilename = null;
		File file;

		try {
			configFilename = System.getenv(PROPERTY_HOME_HEALTH_CONFIG);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Exception: Could not obtain Environment Variable: " + PROPERTY_HOME_HEALTH_CONFIG);
		}

		try {
			/*
			 * Using trim() seems redundant, however, isEmpty() considers a String
			 * of all blanks to be a non-empty String.
			 */
			if (configFilename == null || configFilename.trim().isEmpty()) {
				// since no config file is set on the environment, then search
				// for the file on
				file = new File("config/HomeHealthGrouper.properties");
				if (file.exists()) {
					configFilename = file.getAbsolutePath();
				} else {
					file = new File("C:/Program Files/HomeHealthGrouper/config/HomeHealthGrouper.properties");
					if (file.exists()) {
						configFilename = file.getAbsolutePath();
					} else {
                                            file = new File("C:/hhpps/HomeHealthGrouper/config/HomeHealthGrouper.properties");
                                            if (file.exists()) {
						configFilename = file.getAbsolutePath();
                                            }
                                        }
				}
				Logger.getLogger(getClass().getName()).log(Level.INFO, "HH-PPS: Home Health Java: init() - envirnment variable ''"
						+ PROPERTY_HOME_HEALTH_CONFIG + "'' not set.  Using default properties file: ''{0}''", configFilename);
			} else {
				file = new File(configFilename);
			}

			Logger.getLogger(getClass().getName()).log(Level.INFO, "HH-PPS: Home Health Java: init() - config file: ''{0}''", configFilename);

			properties = new MacroProperties();
			properties.load(new FileInputStream(file));

			if (properties.size() == 0) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "HH-PPS: Environment variable ''" + PROPERTY_HOME_HEALTH_CONFIG
						+ "'' set to file ''{0}'' but the file is empty.", file.getAbsolutePath());
			}

		} catch (FileNotFoundException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HH-PPS: Environment variable '"
					+ PROPERTY_HOME_HEALTH_CONFIG
					+ "' might not be set causing the following 'file not found' error... ", ex);
			properties = null;
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
			properties = null;
		}
		return properties;

	}

	protected HomeHealthGrouperIF loadGrouper(String versionName, String className) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException, Exception {
		final Logger logger = Logger.getLogger(getClass().getName());
		Properties grouperProps;
		HomeHealthGrouperIF grouper;
		Class clazz;
		String tmpStr;

		// load the class and create an instance
		clazz = Class.forName(className);
		grouper = (HomeHealthGrouperIF) clazz.newInstance();

		// load the classes property file
		tmpStr = properties.getProperty(className + PROPERT_CONFIG_SUFFIX);

		if (tmpStr == null) {
			grouperProps = properties;

			// initialize with the current properties file
			logger.log(Level.INFO, "HH-PPS: Grouper named ''{0}'' does not have a configuration file identified. Using default properties", className);
		} else {
			logger.log(Level.INFO, "HH-PPS: Grouper= ''{0}'', config file=''{1}''", new Object[]{className, tmpStr});

			// initialize the classes
			grouperProps = new Properties();
			grouperProps.load(new FileInputStream(tmpStr));
		}

		grouper.init(grouperProps);

		/*
		 * set the grouper dates here so that each grouper does not have 
		 * to do this in their init()
		 */
		/*
		 * reset the grouper's start date if present
		 */
		tmpStr = properties.getProperty(className + PROPERTY_START_DATE_SUFFIX);
		if (tmpStr != null) {
			try {
				final Calendar calendar = OasisCalendarFormatter.parse(tmpStr);
				grouper.setEffectiveDateStart(calendar);
				grouper.setEffectiveDateStartWindow(calendar);
			} catch (NumberFormatException e) {
				logger.log(Level.INFO, "HH-PPS: Grouper= ''{0}'', invalid start date =''{1}'', must be YYYYMMDD", new Object[]{className, tmpStr});
			} catch (IllegalArgumentException e) {
				logger.log(Level.INFO, "HH-PPS: Grouper= ''{0}'', invalid start date =''{1}'', must be YYYYMMDD", new Object[]{className, tmpStr});
			}
		}

		/*
		 * reset the grouper's end date if present
		 */
		tmpStr = properties.getProperty(className + PROPERTY_END_DATE_SUFFIX);
		if (tmpStr != null) {
			try {
				grouper.setEffectiveDateThru(OasisCalendarFormatter.parse(tmpStr));
			} catch (NumberFormatException e) {
				logger.log(Level.INFO, "HH-PPS: Grouper= ''{0}'', invalid end date =''{1}'', must be YYYYMMDD", new Object[]{className, tmpStr});
			} catch (IllegalArgumentException e) {
				logger.log(Level.INFO, "HH-PPS: Grouper= ''{0}'', invalid end date =''{1}'', must be YYYYMMDD", new Object[]{className, tmpStr});
			}
		}

		// if there is a default grouper version identified
		// and it is equal to the current class name, then
		// instantiate the version and reset its version id
		// if necessary
		if (this.defaultGrouperClassName != null
				&& this.defaultGrouperClassName.equals(className)) {
			// create a default grouper
			this.defaultGrouper = (HomeHealthGrouperIF) clazz.newInstance();
			this.defaultGrouper.init(grouperProps);
			setDefaultGrouperVersion(this.defaultGrouper);
		}

		return grouper;
	}

	/**
	 * Set the value of defaultGrouperClassName
	 *
	 * @param defaultGrouperClassName new value of defaultGrouperClassName
	 */
	public void setDefaultGrouperClassName(String defaultGrouperClassName) {
		this.defaultGrouperClassName = defaultGrouperClassName;
	}

	/**
	 * Set the value of defaultVersionPrefix
	 *
	 * @param defaultVersionPrefix new value of defaultVersionPrefix
	 */
	public void setDefaultVersionPrefix(String defaultVersionPrefix) {
		this.defaultVersionPrefix = defaultVersionPrefix;
	}

	/**
	 * provides popup message showing the status of the initialization
	 */
	protected void startProgressBar() {
		JProgressBar progressBar;
		JPanel jPanel;
		JLabel labelWait;

		if (!quiet) {

			progressBar = new JProgressBar(0, 100);
			progressBar.setValue(0);
			progressBar.setStringPainted(true);
			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(true);
			progressBar.setString("Please wait...");
			progressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			labelWait = new JLabel("Initializing Home Health Grouper's Java components");

			jPanel = new JPanel(new BorderLayout());
			jPanel.add(labelWait, BorderLayout.NORTH);
			jPanel.add(progressBar, BorderLayout.SOUTH);
			jPanel.setOpaque(true);
			jPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

			progressFrame = new JFrame("Home Health Grouper");
			progressFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			progressFrame.setLocationRelativeTo(null);

			progressFrame.setContentPane(jPanel);

			progressFrame.pack();
			progressFrame.setVisible(true);
		}
	}

	/**
	 * Stops the progress bar and closes it
	 */
	protected void endProgressBar() {
		if (!quiet
				&& progressFrame != null) {
			progressFrame.setVisible(false);
			progressFrame.dispose();
			progressFrame = null;
		}
	}

	/**
	 * gets the MacroProperties used to initialize this factory.
	 *
	 * @return
	 */
	@Override
	public Properties getProperties() {
		return properties;
	}
}
