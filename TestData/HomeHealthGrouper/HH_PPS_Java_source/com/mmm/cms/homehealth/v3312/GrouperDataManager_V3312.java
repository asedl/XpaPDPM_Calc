/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3312;

import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.EtiologyPairingListIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This class provides the ICD-9 Data management related to the Diagnosis codes,
 * the NRS Diagnosis codes, the Diagnostic Groups, the Etiology Pairs, and the
 * optional Payment Codes.
 *
 * The information is loaded from text files. Refer to the loading methods for
 * the file formats
 *
 * @see #loadDiagnosisCodeBase(File file, HashMap codeHash) throws IOException
 * @see #loadDiagnosticGroupsBase(File file, HashMap groupHash)throws
 * FileNotFoundException,IOException
 * @see #loadEtiologyPairs(HashMap dxCodes) throws FileNotFoundException,
 * IOException
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperDataManager_V3312 extends GrouperDataManager {

    public GrouperDataManager_V3312(HomeHealthGrouperIF homeHealthGrouper) {
        super(homeHealthGrouper);
    }

    /**
     * This loads all the data required to represent a version of the Home
     * Health Grouper. The VCodes table has been combined with the Diagnosis
     * code table in this version, so no separate loading of that data is
     * required
     *
     * @param props
     * @throws java.lang.Exception
     * @throws java.rmi.RemoteException
     */
    @Override
    public void init(Properties props) throws Exception, RemoteException {
        String tmpstr;
        final Logger logger = Logger.getLogger(getClass().getName());

        logger.log(Level.INFO, "HH-PPS: init() - starting...");
        setBasePath(new File(getBasePathName(props)));

        tmpstr = props.getProperty(PROPERTY_INCLUDE_CODE_DESCRIPTIONS, "false");
        setIncludeDescriptions(Boolean.parseBoolean(tmpstr));

        logger.log(Level.INFO, "HH-PPS: Loading configuration from base path: ''{0}''", getBasePath());

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

        logger.log(Level.INFO, "HH-PPS: init() - Done");
    }

    /**
     * This is a consolidated/generic Diagnosis code loaded that requires the
     * input file, and the hashtable to put the codes into
     *
     * The data is loaded from a tab separated file with the format: <ul>
     * <li>Diagnosis Code</li> <li>Description</li> <li>Diagnosis Group
     * Number</li> <li>Code type indicator - M = Secondary only code (without
     * quotes), P = a Payment code, E = an Etiology code, or can be blank
     * indicating it is an Etiology code</li> <li>Diabetes related indicator,
     * used for NRS codes only - D = Diabetes, U = Diabetic Ulcer - can be
     * blank</li> </ul>
     *
     * September 2012 - instead of parsing each token directly in this method,
     * several "onParse" methods were created and called which allows extended
     * classes to add parsing to the specific items. Also, added a call to
     * createDiagnosisCode() to allow the extended class to create is own
     * DiagnosisCodeIF for use in that specific version.
     *
     * @param file
     * @param codeHash
     * @see #onParseDiagnosisCodeAttributes(DiagnosisCodeIF, String)
     * @see onParseDiagnosisDescription(DiagnosisCodeIF diag, String str)
     * @see onParseDiagnosisDiagGroup(DiagnosisCodeIF diag, String str, boolean
     * isNrs)
     * @see onParseDiagnosisUcler(DiagnosisCodeIF diag, String str) *
     *
     * @throws java.io.IOException
     */
    @Override
    protected void loadDiagnosisCodeBase(File file,
            Map<String, DiagnosisCodeIF> codeHash, boolean isNrs) throws IOException {
        BufferedReader reader;
        String line;
        DiagnosisCodeIF diag;
        String tokens[] = null;
        boolean incDesc = isIncludeDescriptions();
        String tmpStr;
        Pattern pattern = getSplitPattern();
        StringTokenizer tokenizer;
        Logger logger = Logger.getLogger(getClass().getName());

        reader = new BufferedReader(new FileReader(file));
        try {
            logger.log(Level.INFO, "HH-PPS: Reading diagnosis file: {0}", file.getAbsolutePath());

            while ((line = reader.readLine()) != null) {

                // parse the string to extract the code information
                // current format is tab separated:
                // value, description, diagnostic Group, secondary only
//				tokens = pattern.split(line, 0);
                tokenizer = new StringTokenizer(line, "\t");
                if (tokens == null || tokens.length != tokenizer.countTokens()) {
                    tokens = new String[tokenizer.countTokens()];
                }
                for (int idx = 0; idx < tokens.length; idx++) {
                    tokens[idx] = tokenizer.nextToken();
                }

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
//                            // Ulcer indicator - used for NRS codes only
//                            // determine the type of Ulcer to indicate
//                            // The default is not an Ulcer related code
//                            // "U" is a general Ulcer
//                            // "D" is a Diabetic Ulcer
//                            tmpStr = tokens[4].trim();
//                            onParseDiagnosisUcler(diag, tmpStr);

                        case 4:
                            // code type
                            // The default type is Etiology
                            // "M" indicates Manifestation, i.e. Secondary only
                            // "P" indicates Optional Payment code
                            tmpStr = tokens[3].trim();
                            onParseDiagnosisCodeAttributes(diag, tmpStr);

                        case 3:
                            // the Diagnostic Group
                            // The default is Group 0, i.e. non Group
                            //   which means that the code is not related to
                            //   Home Health, but is still a valid I-9 code
                            // Otherwise a positive number the links to the
                            //  DiagnosticGroup table
                            if (!tokens[2].isEmpty()) {
                                onParseDiagnosisDiagGroup(diag, tokens[2], isNrs);
                            }
                        case 2:
                            // the disgnosis description
                            if (incDesc) {
                                //diag.setDescription(tokens[1]);
                                onParseDiagnosisDescription(diag, tokens[1]);
                            }
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
            logger.log(Level.INFO, "Out of memory");
        }
    }

    /**
     * Parses the Diagnosis Code Attributes
     *
     * @param diag
     * @param str
     * @see loadDiagnosisCodeBase(File file, Map<String, DiagnosisCodeIF>
     * codeHash, boolean isNrs)
     */
    protected void onParseDiagnosisCodeAttributes(DiagnosisCodeIF diag, String str) {
        if ("M".equals(str)) {
            diag.setSecondaryOnly(true);
        } else if ("P".equals(str)) {
            diag.setOptionalPaymentCode(true);
        }
    }

    /**
     * Parses the Diagnosis Description
     *
     * @param diag
     * @param str
     * @see loadDiagnosisCodeBase(File file, Map<String, DiagnosisCodeIF>
     * codeHash, boolean isNrs)
     */
    protected void onParseDiagnosisDescription(DiagnosisCodeIF diag, String str) {
        diag.setDescription(str);
    }

    /**
     * Parses the Diagnosis Diagnostic Group
     *
     * @param diag
     * @param str
     * @param isNrs
     * @see loadDiagnosisCodeBase(File file, Map<String, DiagnosisCodeIF>
     * codeHash, boolean isNrs)
     */
    protected void onParseDiagnosisDiagGroup(DiagnosisCodeIF diag, String str, boolean isNrs) {
        diag.setDiagnosticGroup(isNrs
                ? getNRSDiagnosticGroup(Long.parseLong(str))
                : getDiagnosticGroup(Long.parseLong(str)));
    }

    /**
     *
     * IMPORTANT: This requires that the diagnosis codes are loaded first.
     *
     * This loads the Etiology Pairs and associates the with the appropriate
     * diagnosis code.
     *
     * The data is loaded from a tab separated file with the format: <ul>
     * <li>Secondary Code</li> <li>Paired Etiology Code</li> </ul>
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    @Override
    public void loadEtiologyPairs(Map<String, DiagnosisCodeIF> dxCodes) throws FileNotFoundException, IOException {

        // load the table from the source
        File file;
        BufferedReader reader;
        String line;
        String tokens[] = null;
        DiagnosisCodeIF primaryCode;
        DiagnosisCodeIF pairedCode;
        Map<String, Boolean> errorReported;
        EtiologyPairingListIF pairingList;
        StringTokenizer tokenizer;
        final Logger logger = Logger.getLogger(getClass().getName());

        if (dxCodes == null || dxCodes.isEmpty()) {
            logger.log(Level.WARNING, "HH-PPS: Etiology Pairs: Can not load pairs.  No list of Diagnosis codes exist.  Please Diagnosis codes first.");
        }

        errorReported = new HashMap<String, Boolean>();

        file = new File(getBasePath(), DIAGNOSIS_ETIOLOGY_PAIRS);
        reader = new BufferedReader(new FileReader(file));
        while ((line = reader.readLine()) != null) {
            // parse the string to extract the code information
            // current format is tab separated:
            // id, blank(1), description
            //tokens = line.split("\\t");
            tokenizer = new StringTokenizer(line, "\t");
            if (tokens == null || tokens.length != tokenizer.countTokens()) {
                tokens = new String[tokenizer.countTokens()];
            }
            for (int idx = 0; idx < tokens.length; idx++) {
                tokens[idx] = tokenizer.nextToken();
            }

            if (tokens.length >= 2) {

                // check if header and skip it
                if (tokens[0].trim().toUpperCase().startsWith("DIAGNOS")) { // OVK 6/15/2016 PBI-147437
                    continue;
                }

                primaryCode = dxCodes.get(tokens[0]);
                if (primaryCode != null) {
                    pairedCode = dxCodes.get(tokens[1]);
                    if (pairedCode == null) {
                        // adding the missing code for now
//                        pairedCode = new DiagnosisCode(tokens[1]);
                        pairedCode = createDiagnosisCode(tokens[1], false, false);

                        if (errorReported.get(tokens[1]) == null) {
                            errorReported.put(tokens[1], Boolean.TRUE);
                            logger.log(Level.FINE, "HH-PPS: Etiology Pairs: Paired/secondary Diagnosis code not found or not valid: ''{0}''",
                                    tokens[1]);
                        }
                    }
                    pairingList = primaryCode.getEtiologyPairCodes();
                    // if there is an list type indicator on the record, then
                    // check it
                    // by default the paing list is inclusionary, so don't
                    // worry about setting the value to the default, only
                    // the exclusionary type
                    if (tokens.length == 3
                            && "E".equalsIgnoreCase(tokens[2])) {
                        // set the list type to Exclusionary
                        pairingList.setInclusionary(false);
                    }
                    pairingList.add(pairedCode);
                } else if (errorReported.get(tokens[0]) == null) {
                    errorReported.put(tokens[0], Boolean.TRUE);
                    logger.log(Level.FINE, "HH-PPS: Etiology Pairs: Primary Diagnosis code not found or not valid: ''{0}''",
                            tokens[0]);
                }
            }
        }

        if (!errorReported.isEmpty()) {
            logger.log(Level.WARNING, "HH-PPS: Etiology Pairs: Number of Missing/Invalid Diagnosis codes: {0}",
                    errorReported.size());
        }
    }
}
