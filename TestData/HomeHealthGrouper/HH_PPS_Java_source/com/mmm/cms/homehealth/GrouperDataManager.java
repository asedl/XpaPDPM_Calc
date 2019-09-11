/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.DataManagerIF;
import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This class provides the ICD-9-CM Data management related to the Diagnosis
 * codes, the NRS Diagnosis codes, the Diagnostic Groups, the Etiology Pairs,
 * and the optional Payment code
 *
 * The information is loaded from text files. Refer to the loading methods for
 * the file formats
 *
 * September 2012 - Replaced all HashMap field types with the more generic Map
 * reference allowing for greater flexibility. Added the createDiagnosisCode()
 * and createDiagnosticGroup() in order to allow extending classes to create
 * their own implementation of each data type.
 *
 * @see #loadDiagnosisCodeBase(File file, Map<String, DiagnosisCodeIF> codeHash)
 * throws IOException
 * @see #loadDiagnosticGroupsBase(File file, Map<String, DiagnosisCodeIF>
 * groupHash)throws FileNotFoundException,IOException
 * @see #loadEtiologyPairs(Map<String, DiagnosisCodeIF> dxCodes) throws
 * FileNotFoundException, IOException
 *
 * @author 3M HIS Clinical & Economic Research for CMS Home Health
 *
 */
public class GrouperDataManager implements DataManagerIF {

    public final static String PROPERTY_MASTER_NAME_BASE_PATH = "master.base.path";
    /**
     * Property suffix the Grouper's data directory: base.path. This is usually
     * added to the end of the Groupers classname
     */
    public final static String PROPERTY_NAME_BASE_PATH_SUFFIX = "base.path";

    /**
     * Property identifier for indicating that the code information should or
     * should not include the code's description information.
     */
    public final static String PROPERTY_INCLUDE_CODE_DESCRIPTIONS = "include.code.descriptions";
    /**
     * Diagnosis Code table name: diagnosisCodes.txt
     */
    public static String ICD_9_DIAGNOSIS_CODE_TABLENAME = "DiagnosisCodes.txt";
    /**
     * NRS Diagnosis Code table name: nrsDiagnosisCodes.txt
     */
    public static String NRS_ICD_9_DIAGNOSIS_CODE_TABLENAME = "NRSDiagnosisCodes.txt";
    /**
     * Diagnostic Group table name: diagnosticGroup.txt
     */
    public static String DIAGNOSTIC_GROUP_TABLENAME = "DiagnosticGroup.txt";
    /**
     * NRS Diagnostic Group table name: NRSDiagnosticGroup.txt
     */
    public static String NRS_DIAGNOSTIC_GROUP_TABLENAME = "NRSDiagnosticGroup.txt";
    /**
     * Scoring Case Mix Adjustment table name: Scoring_CasemixAdjustments.txt
     */
    public static String SCORING_CASEMIX_ADJUSTMENT_TABLENAME = "Scoring_CasemixAdjustments.txt";
    /**
     * NRS Scoring Case Mix Adjustment table name:
     * NRSScoring_CasemixAdjustments.txt
     */
    public static String NRSSCORING_CASEMIX_ADJUSTMENT_TABLENAME = "NRSScoring_CasemixAdjustments.txt";
    /**
     * Diagnosis code Secondary / Etiology pairs table name:
     * DiagnosisEtiologyPairs.txt
     */
    public static String DIAGNOSIS_ETIOLOGY_PAIRS = "DiagnosisEtiologyPairs.txt";
    /**
     * Optional Payment Code table name: OptionalVCodes.txt
     */
    public static String OPTIONAL_VCODES_TABLENAME = "OptionalVCodes.txt";
    /**
     * Internal Table to hold the Clinical diagnosis codes
     */
    private Map<String, DiagnosisCodeIF> diagnosisCodes;
    /**
     * Internal Table to hold the NRS diagnosis codes
     */
    private Map<String, DiagnosisCodeIF> nrsDiagnosisCodes;
    /**
     * Internal Table to hold the Clinical Diagnostic Groups
     */
    private Map<Integer, DiagnosticGroupIF> diagnosticGroups;
    /**
     * Internal Table to hold the NRS Diagnostic Groups
     */
    private Map<Integer, DiagnosticGroupIF> nrsDiagnosticGroups;
    /**
     * Internal Table to hold the Clinical Case Mix scoring values for each
     * equation
     */
    private Map<Integer, CaseMixAdjustmentItemIF> caseMixAdjustments;
    /**
     * Internal Table to hold the NRS Case Mix scoring values
     */
    private Map<Integer, CaseMixAdjustmentItemIF> nrsCaseMixAdjustments;
    /**
     * File system path to the external data files
     */
    private File basePath;
    private boolean includeDescriptions;
    private final Pattern splitPattern;
    protected HomeHealthGrouperIF homeHealthGrouper;

    public GrouperDataManager(HomeHealthGrouperIF homeHealthGrouper) {
        this.homeHealthGrouper = homeHealthGrouper;
        splitPattern = Pattern.compile("\\t");
        caseMixAdjustments = new HashMap<Integer, CaseMixAdjustmentItemIF>();
        nrsCaseMixAdjustments = new HashMap<Integer, CaseMixAdjustmentItemIF>();
    }

    /**
     * This creates a Diagnosis code allowing any extending class to create its
     * own code implementation for use in its grouper version.
     *
     * @param code
     * @param validCode
     * @param validForScoring
     * @return non-null DiagnosisCodeIF with its code value, valid code flag and
     * valid for scoring flag set
     */
    @Override
    public DiagnosisCodeIF createDiagnosisCode(String code, boolean validCode,
            boolean validForScoring) {
        return new DiagnosisCode(code, validCode, validForScoring);
    }

    /**
     * This creates a Diagnostic Group allowing any extending class to create
     * its own group implementation for use in its grouper version.
     *
     * @param id
     * @return non-null DiagnosticGroupIF with its ID set
     */
    @Override
    public DiagnosticGroupIF createDiagnosticGroup(int id) {
        return new DiagnosticGroup(id);
    }

    /**
     * gets the list of diagnostic Groups associated with the
     * Clinical/Functional diagnosis
     *
     * @return non-null List of Diagnostic Groups items. If there was an error
     * during initialization, the list may be empty
     */
    @Override
    public List<DiagnosticGroupIF> getDiagnosticGroups() {
        List<DiagnosticGroupIF> groups;
        final Collection collection = diagnosticGroups.values();

        groups = new ArrayList<DiagnosticGroupIF>(collection);
        Collections.sort(groups, new Comparator<DiagnosticGroupIF>() {
            @Override
            public int compare(DiagnosticGroupIF o1, DiagnosticGroupIF o2) {
                return o1.getId() - o2.getId();
            }
        });
        return groups;
    }

    /**
     * gets the list of diagnostic Groups associated with the Non-routine
     * supplies diagnosis
     *
     * @return non-null List of Diagnostic Groups items. If there was an error
     * during initialization, the list may be empty
     */
    @Override
    public List<DiagnosticGroupIF> getDiagnosticGroupsNRS() {
        List<DiagnosticGroupIF> groups;
        final Collection collection = nrsDiagnosticGroups.values();

        groups = new ArrayList<DiagnosticGroupIF>(collection);
        Collections.sort(groups, new Comparator<DiagnosticGroupIF>() {
            @Override
            public int compare(DiagnosticGroupIF o1, DiagnosticGroupIF o2) {
                return o1.getId() - o2.getId();
            }
        });
        return groups;
    }

    /**
     * This calculates the data path based on the Grouper class name and the
     * base path suffix.
     *
     * @param properties
     * @return
     */
    protected String getBasePathName(Properties properties) {
        String tmpStr = properties.getProperty(homeHealthGrouper.getClass().getName() + "." + PROPERTY_NAME_BASE_PATH_SUFFIX);
        if (tmpStr == null) {
            tmpStr = ".";
        }

        return tmpStr;
    }

    /**
     * Get the value of basePath which is the folder location of the data files
     *
     * @return the value of basePath
     */
    public File getBasePath() {
        return basePath;
    }

    /**
     * Get the value of homeHealthGrouper
     *
     * @return the value of homeHealthGrouper
     */
    @Override
    public HomeHealthGrouperIF getHomeHealthGrouper() {
        return homeHealthGrouper;
    }

    /**
     * This is a proxy to getDiagnosisCodeBase
     *
     * @see #getDiagnosisCodeBase(HashMap codes, String value)
     * @param value
     * @return NRS Diagnosis code or null if not a valid code
     */
    @Override
    public DiagnosisCodeIF getNRSDiagnosisCode(String value) {
        return getDiagnosisCodeBase(nrsDiagnosisCodes, value);
    }

    /**
     * This is a proxy to getDiagnosisCodeBase
     *
     * @see #getDiagnosisCodeBase(HashMap codes, String value)
     * @param value
     * @return Diagnosis code or null if not a valid code
     */
    @Override
    public DiagnosisCodeIF getDiagnosisCode(String value) {
        return getDiagnosisCodeBase(diagnosisCodes, value);
    }

    /**
     * Searchs the supplied diagnosis code table, codes, for the code value. If
     * found, then the code is cloned to allow the calling module to change its
     * attributes without effecting the original code, which remains in the same
     * state as when it was loaded.
     *
     * @param codes
     * @param value
     * @return the cloned() code or null if the the value is not valid code
     * value
     */
    protected DiagnosisCodeIF getDiagnosisCodeBase(
            Map<String, DiagnosisCodeIF> codes, String value) {

        DiagnosisCodeIF code;

        code = codes.get(value);
        if (code != null) {
            try {
                // clone the code to allow to be changed by the calling module
                // without effecting the original
                code = code.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
            }
        }

        return code;
    }

    /**
     * gets the Standard services case mix adjustment table
     *
     * @return non-null List of CaseMixAdjustment items. If there was an error
     * during initialization, the list may be empty
     */
    @Override
    public List<CaseMixAdjustmentItemIF> getCaseMixAdjustments() {
        return getCaseMixAdjustments_general(this.caseMixAdjustments);
    }

    /**
     * gets the Case Mix Adjustment item from the table, and converts them to a
     * read only version when putting them into a sorted list.
     *
     * @return non-null List of CaseMixAdjustment items. If there was an error
     * during initialization, the list may be empty
     *
     * @param table
     * @return
     */
    protected final List<CaseMixAdjustmentItemIF> getCaseMixAdjustments_general(Map<Integer, CaseMixAdjustmentItemIF> table) {
        List<CaseMixAdjustmentItemIF> list = new ArrayList(table.size());

        // convert the hashtable to a list
        for (CaseMixAdjustmentItemIF casemix : table.values()) {
            list.add(new ReadOnlyCaseMix(casemix));
        }

        // sort the list by the case mix id
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 != o2) {
                    if (o1 != null) {
                        CaseMixAdjustmentItemIF caseMix1 = (CaseMixAdjustmentItemIF) o1;
                        if (o2 != null) {
                            CaseMixAdjustmentItemIF caseMix2 = (CaseMixAdjustmentItemIF) o2;
                            return caseMix1.getId() - caseMix2.getId();
                        } else {
                            return 1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return 0;
                }
            }
        });

        return list;
    }

    /**
     * Gets all the Clinical/Functional codes that are sorted by code value.
     *
     * @return
     */
    @Override
    public List<DiagnosisCodeIF> getClinicalCodes() {
        List<DiagnosisCodeIF> codes;

        codes = new ArrayList<DiagnosisCodeIF>(this.diagnosisCodes.values());
        return codes;
    }

    /**
     * Searches for the Diagnostic Group object based on its ID
     *
     * @param id
     * @return DiagnosticGroupIF or null if the ID is not valid
     */
    @Override
    public DiagnosticGroupIF getDiagnosticGroup(long id) {
        DiagnosticGroupIF group = diagnosticGroups.get(Integer.valueOf((int) id));
        if (group == null) {
            group = DiagnosticGroup.GROUP_UNKNOWN;
        }

        return group;
    }

    /**
     * Searches for the Non-Routine Supplies Diagnostic Group based on the ID
     *
     * @param id
     * @return DiagnosticGroup or null if the id is not found
     */
    @Override
    public DiagnosticGroupIF getNRSDiagnosticGroup(long id) {
        DiagnosticGroupIF group = nrsDiagnosticGroups.get(Integer.valueOf((int) id));
        if (group == null) {
            group = DiagnosticGroup.GROUP_UNKNOWN;
        }

        return group;
    }

    /**
     * Gets all the
     *
     * @return
     */
    @Override
    public List<DiagnosisCodeIF> getNonRoutineCodes() {
        List<DiagnosisCodeIF> codes;

        codes = new ArrayList<DiagnosisCodeIF>(this.nrsDiagnosisCodes.values());
        return codes;
    }

    /**
     * This will search the case mix table for the case mix id, and return the
     * case mix adjustment item
     *
     * @param caseMixId
     * @return Case mix adjustment item if found. null if not found
     */
    @Override
    public CaseMixAdjustmentItemIF getNRSCaseMixAdjustment(int caseMixId) {
        return nrsCaseMixAdjustments.get(Integer.valueOf(caseMixId));
    }

    /**
     * gets the Non-routines services case mix adjustment table
     *
     * @return non-null List of CaseMixAdjustment items. If there was an error
     * during initialization, the list may be empty
     */
    @Override
    public List<CaseMixAdjustmentItemIF> getNRSCaseMixAdjustments() {
        return getCaseMixAdjustments_general(this.nrsCaseMixAdjustments);
    }

    @Override
    public Pattern getSplitPattern() {
        return splitPattern;
    }

    /**
     * This loads all the data required to represent a version of the Home
     * Health Grouper.
     *
     * @param props
     * @throws java.lang.Exception
     * @throws java.rmi.RemoteException
     */
    @Override
    public void init(Properties props) throws Exception, RemoteException {
        String tmpstr;
        final Logger logger = Logger.getLogger(getClass().getName());

        logger.log(Level.INFO, "init() - starting...");

        tmpstr = getBasePathName(props);
        basePath = new File(tmpstr);

        tmpstr = props.getProperty(PROPERTY_INCLUDE_CODE_DESCRIPTIONS, "false");
        includeDescriptions = Boolean.parseBoolean(tmpstr);

        logger.log(Level.INFO, "HH-PPS: Loading configuration from base path: ''{0}''", basePath);

        // load the Clinical tables
        logger.log(Level.INFO, "HH-PPS: Loading Diagnostic Groups");
        loadDiagnosticGroups();

        logger.log(Level.INFO, "HH-PPS: Loading Diagnosis Codes");
        loadDiagnosisCodes();

        logger.log(Level.INFO, "HH-PPS: Loading Case Mix Adujustments");
        loadCaseMixAdjustments();

        // load nrs information
        logger.log(Level.INFO, "HH-PPS: Loading NRS Diagnostic Groups");
        loadNRSDiagnosticGroups();

        logger.log(Level.INFO, "HH-PPS: Loading NRS Diagnosis Codes");
        loadNRSDiagnosisCodes();

        logger.log(Level.INFO, "HH-PPS: Loading NRS Case Mix Adujustments");
        loadNRSCaseMixAdjustments();

        // load this effects the other code listing
        logger.log(Level.INFO, "HH-PPS: Loading Optional Payment Codes");
        loadOptionalVCodes();

        logger.log(Level.INFO, "HH-PPS: init() - Done");
    }

    @Override
    public boolean isIncludeDescriptions() {
        return includeDescriptions;
    }

    /**
     * Loads the Diagnostic Group information from a text file
     *
     * The data is loaded from a tab separated file with the format:
     * <ul>
     * <li>Id</li>
     * <li>Diagnostic Group name</li>
     * </ul>
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected void loadDiagnosticGroups() throws FileNotFoundException,
            IOException {

        File file;

        file = new File(basePath, DIAGNOSTIC_GROUP_TABLENAME);

        diagnosticGroups = new HashMap<Integer, DiagnosticGroupIF>();
        loadDiagnosticGroupsBase(file, diagnosticGroups);
    }

    /**
     * This will add the Diagnosis Codes associated with this version. By
     * default it will set the ValidCode and ValidForScoring indicators to true.
     * This uses the loadDiagnosisCodeBase() to read the file.
     *
     * @see #loadDiagnosisCodeBase(File file, HashMap codeHash) throws
     * IOException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected void loadDiagnosisCodes() throws FileNotFoundException,
            IOException {
        File file;

        file = new File(basePath, ICD_9_DIAGNOSIS_CODE_TABLENAME);
        diagnosisCodes = new HashMap<String, DiagnosisCodeIF>();

        loadDiagnosisCodeBase(file, diagnosisCodes, false);
        loadEtiologyPairs(diagnosisCodes);
    }

    /**
     * This is a consolidated/generic Diagnosis code loaded that requires the
     * input file, and the hash map to put the codes into
     *
     * The data is loaded from a tab separated file with the format:
     * <ul>
     * <li>Diagnosis Code</li>
     * <li>Description</li>
     * <li>Diagnosis Group Number</li>
     * <li>Secondary only code indicated with M (without quotes) - can be
     * blank</li>
     * <li>Diabetes related indicator - D = Diabetes, U = Diabetic Ulcer - can
     * be blank</li>
     * </ul>
     *
     * @param file
     * @param codeHash
     * @throws java.io.IOException
     */
    protected void loadDiagnosisCodeBase(File file,
            Map<String, DiagnosisCodeIF> codeHash,
            boolean isNrs) throws IOException {
        BufferedReader reader;
        String line;
        DiagnosisCodeIF diag;
        String tokens[];

        reader = new BufferedReader(new FileReader(file));
        try {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "HH-PPS: Reading diagnosis file: {0}", file.getAbsolutePath());

            while ((line = reader.readLine()) != null) {

                // parse the string to extract the code information
                // current format is tab separated:
                // value, description, diagnostic Group, secondary only
                tokens = splitPattern.split(line, 0);

                // skip blank lines
                if (tokens.length > 1) {

                    // check if header and skip it
                    if (tokens[0].trim().toUpperCase().startsWith("DIAGNOS")) { // OVK 6/15/2016 PBI-147437
                        continue;
                    }

                    diag = createDiagnosisCode(tokens[0].trim(), true, true);

                    // check the length of the tokens array, and starting at the
                    // largest array value, set the appropriate code attribute,
                    // letting the attribute setting to fall through to the previous
                    // tokens in the array
                    switch (tokens.length) {
                        /*
                         * May 2013 - the diabetic ulcer and ulcer 
                         * flags are no longer used.
                         */
                        case 5:

                        case 4:
                            // if a code is set as a manifestation code, it
                            // is marked as not valid for scoring (unless it
                            // is matched with code pair
                            diag.setSecondaryOnly("M".equals(tokens[3].trim()));

                        case 3:
                            if (!tokens[2].isEmpty()) {
                                diag.setDiagnosticGroup(isNrs
                                        ? getNRSDiagnosticGroup(Long.parseLong(tokens[2]))
                                        : getDiagnosticGroup(Long.parseLong(tokens[2])));
                            }
                        case 2:
                            if (includeDescriptions) {
                                diag.setDescription(tokens[1]);
                            }

                        default:
                    }

                    // set any diagnosis that does not have a valid Home Health
                    // diagnosis Group, to be invalid for scoring
                    // although it is a valid code
                    if (diag.getDiagnosticGroup().getId() == 0) {
                        diag.setValidForScoring(false);
                    }

                    // add the code to the internal table
                    codeHash.put(diag.getCode(), diag);
                }
            }
        } catch (OutOfMemoryError o) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Out of memory");
        }
    }

    /**
     * Loads Non-Routine Supplies related Diagnosis codes. This uses the
     * loadDiagnosisCodeBase() to read the file.
     *
     * @see #loadDiagnosisCodeBase(File file, HashMap codeHash) throws
     * IOException
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected void loadNRSDiagnosisCodes() throws FileNotFoundException,
            IOException {
        File file;

        file = new File(basePath, NRS_ICD_9_DIAGNOSIS_CODE_TABLENAME);
        nrsDiagnosisCodes = new HashMap<String, DiagnosisCodeIF>();

        loadDiagnosisCodeBase(file, nrsDiagnosisCodes, true);
        loadEtiologyPairs(nrsDiagnosisCodes);
    }

    /**
     * This loads the Non-Routine Supplies Diagnostic Groups using the
     * loadDiagnosticGroupsBase()
     *
     * @see #loadDiagnosticGroupsBase(File file, HashMap groupHash) throws
     * FileNotFoundException, IOException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected void loadNRSDiagnosticGroups() throws FileNotFoundException,
            IOException {

        final File file;

        nrsDiagnosticGroups = new HashMap<Integer, DiagnosticGroupIF>();
        file = new File(basePath, NRS_DIAGNOSTIC_GROUP_TABLENAME);

        loadDiagnosticGroupsBase(file, nrsDiagnosticGroups);
    }

    /**
     * Loads the Diagnostic Group information from a text file.
     *
     * The data is loaded from a tab separated file with the format:
     * <ul>
     * <li>Id</li>
     * <li>Diagnostic Group name</li>
     * </ul>
     *
     * @param file
     * @param groupHash
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected void loadDiagnosticGroupsBase(File file,
            Map<Integer, DiagnosticGroupIF> groupHash) throws
            FileNotFoundException,
            IOException {
        BufferedReader reader;
        String line;
        DiagnosticGroupIF diagGroup;
        String tokens[];

        reader = new BufferedReader(new FileReader(file));
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                // parse the string
                tokens = splitPattern.split(line, 0);

                // check if header and skip it
                if (tokens[0].trim().toUpperCase().startsWith("DIAGNOS")) { // OVK 6/15/2016 PBI-147437
                    continue;
                }

                diagGroup = createDiagnosticGroup(Integer.valueOf(tokens[0]));
                diagGroup.setDescription(tokens[1]);

                groupHash.put(Integer.valueOf(diagGroup.hashCode()), diagGroup);
            }
        }
    }

    /**
     * Loads the Case mix adjustment values for the Clinical scoring
     *
     * The data is loaded from a tab separated file with the format:
     * <ul>
     * <li>Id</li>
     * <li>name</li>
     * <li>equation 1 value</li>
     * <li>equation 2 value</li>
     * <li>equation 3 value</li>
     * <li>equation 4 value</li>
     * </ul>
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void loadCaseMixAdjustments() throws FileNotFoundException,
            IOException {

        // load the table from the source
        File file;
        BufferedReader reader;
        String line;
        CaseMixAdjustmentItemIF caseMix;
        String tokens[];
        int idx;

        file = new File(basePath, SCORING_CASEMIX_ADJUSTMENT_TABLENAME);
        reader = new BufferedReader(new FileReader(file));
        while ((line = reader.readLine()) != null) {
            // parse the string to extract the code information
            // current format is tab separated:
            // id, blank(1), description
            tokens = splitPattern.split(line);
//            tokens = line.split("\\t");

            // check if header and skip it
            if (tokens[0].trim().toUpperCase().startsWith("ID")) { // OVK 6/15/2016 PBI-147437
                continue;
            }

            caseMix = new CaseMixAdjustmentItem();
            caseMix.setId(Integer.valueOf(tokens[0]));

            caseMix.setName(tokens[1]);
            for (idx = 2; idx < tokens.length; idx++) {
                try {
                    // case mix adjustments are 1 based ids, since the
                    // idx is starting at 2, subtract 1 for the appropriate
                    // equation value
                    caseMix.setAdjustment(idx - 1, Integer.parseInt(tokens[idx]));
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.WARNING, "HH-PPS: Loading CaseMixAdjustment: Equation "
                            + idx + " contains invalid number of '" + tokens[idx]
                            + "'", e);
                }
            }

            caseMixAdjustments.put(Integer.valueOf(caseMix.hashCode()), caseMix);
        }
    }

    /**
     * This will search the case mix table for the case mix id, and return the
     * case mix adjustment item
     *
     * @param caseMixId
     * @return Case mix adjustment item if found. null if not found
     */
    @Override
    public CaseMixAdjustmentItemIF getCaseMixAdjustment(int caseMixId) {
        return caseMixAdjustments.get(Integer.valueOf(caseMixId));
    }

    /**
     * Loads the Case mix adjustment values for the Non-Routine Supplies
     * scoring.
     *
     * The data is loaded from a tab separated file with the format:
     * <ul>
     * <li>Id</li>
     * <li>name</li>
     * <li>equation 1 value</li>
     * </ul>
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void loadNRSCaseMixAdjustments() throws FileNotFoundException,
            IOException {

        // load the table from the source
        File file;
        BufferedReader reader;
        String line;
        CaseMixAdjustmentItemIF caseMix;
        String tokens[];
        int idx;

        file = new File(basePath, NRSSCORING_CASEMIX_ADJUSTMENT_TABLENAME);
        reader = new BufferedReader(new FileReader(file));
        while ((line = reader.readLine()) != null) {
            // split the line
            tokens = splitPattern.split(line);

            // check if header and skip it
            if (tokens[0].trim().toUpperCase().startsWith("ID")) { // OVK 6/15/2016 PBI-147437
                continue;
            }

            caseMix = new CaseMixAdjustmentItem();
            caseMix.setId(Integer.valueOf(tokens[0]));
            caseMix.setName(tokens[1]);
            for (idx = 2; idx < tokens.length; idx++) {
                try {
                    // case mix adjustments are 1 based ids, since the
                    // idx is starting at 2, subtract 1 for the appropriate
                    // equation value
                    caseMix.setAdjustment(idx - 1, Integer.parseInt(tokens[idx]));
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.WARNING, "HH-PPS: Loading NRSCaseMixAdjustment: Equation "
                            + idx + " contains invalid number of '" + tokens[idx]
                            + "'", e);
                }
            }

            nrsCaseMixAdjustments.put(Integer.valueOf(caseMix.hashCode()), caseMix);
        }
    }

    /**
     *
     * IMPORTANT: This requires that the diagnosis codes are loaded first.
     *
     * This loads the Etiology Pairs and associates the with the appropriate
     * diagnosis code.
     *
     * The data is loaded from a tab separated file with the format:
     * <ul>
     * <li>Secondary Code</li>
     * <li>Paired Etiology Code</li>
     * </ul>
     *
     * @param dxCodes
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void loadEtiologyPairs(Map<String, DiagnosisCodeIF> dxCodes) throws FileNotFoundException, IOException {

        // load the table from the source
        File file;
        BufferedReader reader;
        String line;
        String tokens[];
        DiagnosisCodeIF primaryCode;
        DiagnosisCodeIF pairedCode;
        Map<String, Boolean> errorReported;
        final Logger logger = Logger.getLogger(getClass().getName());

        if (dxCodes == null || dxCodes.isEmpty()) {
            logger.log(Level.WARNING, "Etiology Pairs: Can not load pairs.  No list of Diagnosis codes exist.  Please Diagnosis codes first.");
        }

        errorReported = new HashMap<String, Boolean>();

        file = new File(basePath, DIAGNOSIS_ETIOLOGY_PAIRS);
        reader = new BufferedReader(new FileReader(file));
        while ((line = reader.readLine()) != null) {
            // parse the string to extract the code information
            // current format is tab separated:
            // id, blank(1), description
            tokens = splitPattern.split(line);
            if (tokens.length == 2) {

                // check if header and skip it
                if (tokens[0].trim().toUpperCase().startsWith("DIAGNOS")) { // OVK 6/15/2016 PBI-147437
                    continue;
                }

                primaryCode = dxCodes.get(tokens[0]);
                if (primaryCode != null) {
                    pairedCode = dxCodes.get(tokens[1]);
                    if (pairedCode == null) {
                        // adding the missing code for now
                        pairedCode = createDiagnosisCode(tokens[1], false, false);

                        if (errorReported.get(tokens[1]) == null) {
                            errorReported.put(tokens[1], Boolean.TRUE);
                            logger.log(Level.FINE, "HH-PPS: Etiology Pairs: Paired/secondary Diagnosis code not found or not valid: ''{0}''", tokens[1]);
                        }
                    }
                    primaryCode.getEtiologyPairCodes().add(pairedCode);
                } else {
                    if (errorReported.get(tokens[0]) == null) {
                        errorReported.put(tokens[0], Boolean.TRUE);
                        logger.log(Level.FINE, "HH-PPS: Etiology Pairs: Primary Diagnosis code not found or not valid: ''{0}''", tokens[0]);
                    }
                }
            }
        }

        if (errorReported.size() > 0) {
            logger.log(Level.WARNING, "HH-PPS: Etiology Pairs: Number of Missing/Invalid Diagnosis codes: {0}", errorReported.size());
        }
    }

    /**
     * IMPORTANT: This requires that the diagnosis codes are loaded first.
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void loadOptionalVCodes() throws FileNotFoundException, IOException {

        // load the table from the source
        File file;
        BufferedReader reader;
        String line;
        String tokens[];
        DiagnosisCode code;
        final Logger logger = Logger.getLogger(getClass().getName());

        if (diagnosisCodes == null || diagnosisCodes.isEmpty()) {
            logger.log(Level.WARNING, "Optional Payment Code: Can not load.  No list of Diagnosis codes exist.  Please Diagnosis codes first.");
        }

        if (nrsDiagnosisCodes == null || nrsDiagnosisCodes.isEmpty()) {
            logger.log(Level.WARNING, "Optional Payment Code: Can not load.  No list of Diagnosis codes exist.  Please Diagnosis codes first.");
        }

        file = new File(basePath, OPTIONAL_VCODES_TABLENAME);
        reader = new BufferedReader(new FileReader(file));
        while ((line = reader.readLine()) != null) {
            // parse the string to extract the code information
            // current format is tab separated:
            // id, blank(1), description
            tokens = splitPattern.split(line);
            if (tokens.length == 2) {
                code = (DiagnosisCode) this.diagnosisCodes.get(tokens[0]);
                if (code == null) {
                    code = new DiagnosisCode(tokens[0], false, false);
                    code.setDescription(tokens[1]);
                }
                code.setOptionalPaymentCode(true);
                this.diagnosisCodes.put(code.getCode(), code);

                code = (DiagnosisCode) this.nrsDiagnosisCodes.get(tokens[0]);
                if (code == null) {
                    code = new DiagnosisCode(tokens[0], false, false);
                    code.setDescription(tokens[1]);
                }
                code.setOptionalPaymentCode(true);
                this.nrsDiagnosisCodes.put(code.getCode(), code);
            }
        }
    }

    /**
     * Set the value of basePath which is the folder location of the data files
     *
     * @param basePath new value of basePath
     */
    public void setBasePath(File basePath) {
        this.basePath = basePath;
    }

    /**
     * Set the value of homeHealthGrouper
     *
     * @param grouperVersion new value of homeHealthGrouper
     */
    public void setHomeHealthGrouper(HomeHealthGrouperIF grouperVersion) {
        this.homeHealthGrouper = grouperVersion;
    }

    @Override
    public void setIncludeDescriptions(boolean includeDescriptions) {
        this.includeDescriptions = includeDescriptions;
    }

    //---------------------------------------
    // READ ONLY VERSIONS OF STANDARD OBJECTS
    //---------------------------------------
    class ReadOnlyCaseMix implements CaseMixAdjustmentItemIF {

        CaseMixAdjustmentItemIF item;

        public ReadOnlyCaseMix(CaseMixAdjustmentItemIF item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return item.toString();
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return item.equals(obj);
        }

        @Override
        public String getName() {
            return item.getName();
        }

        @Override
        public int getId() {
            return item.getId();
        }

        @Override
        public int getLater14Plus() {
            return item.getLater14Plus();
        }

        @Override
        public int getLater13AndUnder() {
            return item.getLater13AndUnder();
        }

        @Override
        public int getEquationValue(int idx) {
            return item.getEquationValue(idx);
        }

        @Override
        public int getEarly14Plus() {
            return item.getEarly14Plus();
        }

        @Override
        public int getEarly13AndUnder() {
            return item.getEarly13AndUnder();
        }

        @Override
        public int getAdjustment(int equationId) {
            return item.getAdjustment(equationId);
        }

        @Override
        public void setAdjustment(int equationId, int value) {
            // does nothing
        }

        @Override
        public void setEarly13AndUnder(int equation1) {
            // does nothing
        }

        @Override
        public void setEarly14Plus(int equation2) {
            // does nothing
        }

        @Override
        public void setId(int id) {
            // does nothing
        }

        @Override
        public void setLater13AndUnder(int equation3) {
            // does nothing
        }

        @Override
        public void setLater14Plus(int equation4) {
            // does nothing
        }

        @Override
        public void setName(String Name) {
            // does nothing
        }

        @Override
        public void add(PointsScoringEquationsIF points) {
            // does nothing
        }

        @Override
        public void addToEarly13AndUnder(int equation) {
            // does nothing
        }

        @Override
        public void addToEarly14Plus(int equation) {
            // does nothing
        }

        @Override
        public void addToLater13AndUnder(int equation) {
            // does nothing
        }

        @Override
        public void addToLater14Plus(int equation) {
            // does nothing
        }

        @Override
        public void setEquationValue(int idx, int value) {
            // does nothing
        }
    }
}
