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
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.v3210.GrouperVersion_v3210;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * This version is valid between January 2012 to Sept 30, 2012.
 *
 * Changes to logic in this version:
 * 
 *   1) Etiology/manifestation pairings will be retained but the HHRG, when
 * encountering an I-9 code listed under a manifestation/etiology edit, will
 * look at multiple preceding I-9 fields to check for validation based upon
 * the CMS proprietary eti/mani lists. An etiology must be sequenced prior to
 * the manifestation code for the manifestation to be considered valid for
 * scoring but not immediately preceding. There is neither a change to the
 * pairings of etiology and manifestation nor a change to the
 * etiology/manifestation logic in the payment diagnosis fields (col 3 and 4)
 * as a result of this revision. The first valid preceding etiology code
 * encountered by the HHRG is considered the manifestation code partner for
 * scoring purposes (i.e only one of the etiology/manifestation may score).
 *
 * 
 * @see com.mmm.cms.homehealth.v3210.GrouperVersion_v3210
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class GrouperVersion_v3312 extends GrouperVersion_v3210  implements
        HomeHealthGrouperIF {

    /**
     * Constructor that sets the effective start and thru date to
     * Jan 1, 2012 thru Dec 31, 2012.
     * 
     * In the original distribution, the effective start and thru dates were
     * Jan 1, 2012 thru Sept 30, 2012.  However, because the next version did
     * not start until January 2013, and the new version removes any default,
     * the date of this version was extended to cover the period between the
     * end of Sept 2012 and the beginning of January 2013.
     */
    public GrouperVersion_v3312() {
        this(new GregorianCalendar(2012, 0, 1), new GregorianCalendar(2012, 11, 31),
                null, "V3312");
    }

    /**
     * This constructor allows extended classes to set dates in the constructor
     * @param start
     * @param thru
     * @param window
     * @param versionName 
     */
    public GrouperVersion_v3312(Calendar start, Calendar thru, Calendar window, 
            String versionName) {
        super(start, thru, window, versionName);
    }

    /**
     * Returns a non-null double array of int[6][3]. The values in the array
     * elements are as follows:
     * <ul>
     * <li>0 - not scored</li>
     * <li>1 - score during first round</li>
     * <li>2 - score during second round</li>
     * </ul>
     *
     * Stand alone etiology codes are always scored first, then the
     * manifestation/ etiology pair codes are scored second. Codes that are not
     * valid for scoring are not evaluated and there for entry in the array will
     * be 0
     *
     * @param record
     * @return
     */
    @Override
    public int[][] determineScoreOrder(HomeHealthRecordIF record) {
        DiagnosisCodeIF diagCode;
        DiagnosisCodeIF prevDiagCode;
        DiagnosisCodeIF diagCodeOptional;
        int scoreOrder[][] = new int[6][3];

        // loop through the column 2 codes
        for (int diagIdx = 0; diagIdx < 6; diagIdx++) {
            diagCode = record.getDiagnosisCode(diagIdx);

            // for optional payment codes go across the row to columns 3 & 4
            if (diagCode.isOptionalPaymentCode()) {
                // determine if the 4 column is an etiology
                diagCodeOptional = record.getOptionalDiagnosisCode4(diagIdx);
                if (diagCodeOptional.isValidForScoring()) {
                    // since this a secondary code that is valid for
                    // scoring, then the previous code must its etiology
                    // and so both are scored second
                    scoreOrder[diagIdx][2] = 2;
                    scoreOrder[diagIdx][1] = 2;
                } else {
                    // determine if the 3rd column is available for scoring
                    diagCodeOptional = record.getOptionalDiagnosisCode3(diagIdx);
                    if (diagCodeOptional.isValidForScoring()) {
                        scoreOrder[diagIdx][1] = 1;
                    }
                }
            } else if (diagCode.isValidForScoring()) {
                // if the current code code is a secondary only, determine
                // whether it earns points or the etiology earns points
                if (diagCode.isSecondaryOnly()) {
                    // since this a secondary code that is valid for
                    // scoring, then the previous code must its etiology
                    // and so both are scored second
                    scoreOrder[diagIdx][0] = 2;

                    // August 2011
                    // now find an etiology code prior to this
                    // code that this can be paired with
                    for (int preIdx = diagIdx - 1; preIdx >= 0; preIdx--) {
                        prevDiagCode = record.getDiagnosisCode(preIdx);
                        if (diagCode.isValidEtiologyPairing(prevDiagCode)) {
                            scoreOrder[preIdx][0] = 2;
                            break;
                        }
                    }
                    // August 2011 - end

                } else {
                    // this is a etiology so, score it on the first run
                    scoreOrder[diagIdx][0] = 1;
                }
            }
        }

        return scoreOrder;
    }
    
    @Override
    public HomeHealthRecordValidatorIF getClinicalValidator() {
        return new HomeHealthRecordClinicalValidator_v3312(this);
    }

    /**
     * Get the name of this version
     * @return the name of this version
     */
    @Override
    public String getName() {
        return "HHA PPS Grouper - January 2012 Logic updates, " +
                getVersion();
    }

    @Override
    public HomeHealthRecordValidatorIF getNRSValidator() {
        return new HomeHealthRecordClinicalValidator_v3312(this);
    }

    /**
     * Sets up this version by loading the related Diagnosis code / Group
     * data, and initializing the scoring models: 4 clinical/functional models (one for each
     * equation), and one Non-Routine Supplies model
     *
     * This version uses it is own version of the scoring models and the
     * grouper data manager
     * 
     * @param props
     * @throws java.lang.Exception
     */
    @Override
    public void init(Properties props) throws Exception {
        GrouperDataManager grouperManager;

        grouperManager = new GrouperDataManager_V3312(this);
        grouperManager.init(props);
        setGrouperDataManager(grouperManager);

        clinicalModel_1 = new ClinicalFunctional_ScoringModel_v3312(this, grouperManager, 1);
        clinicalModel_2 = new ClinicalFunctional_ScoringModel_v3312(this, grouperManager, 2);
        clinicalModel_3 = new ClinicalFunctional_ScoringModel_v3312(this, grouperManager, 3);
        clinicalModel_4 = new ClinicalFunctional_ScoringModel_v3312(this, grouperManager, 4);

        nrsScoringModel = new NRS_ScoringModel_v3312(this, grouperManager);
    }
}




