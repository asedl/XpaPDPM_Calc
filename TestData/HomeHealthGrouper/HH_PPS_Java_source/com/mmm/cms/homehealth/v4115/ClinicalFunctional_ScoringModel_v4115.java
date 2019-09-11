/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v4115;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.DiagnosisCode_Empty;
import com.mmm.cms.homehealth.DiagnosisScoringGrid;
import com.mmm.cms.homehealth.ScoringPoints;
import com.mmm.cms.homehealth.proto.*;
import com.mmm.cms.homehealth.v3312.ManifestationUtils;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ScoringModelUtils;
import com.mmm.cms.util.ValidateUtils;
import java.util.Collection;

/**
 * This Model provides scoring for the 4 clinical and functional equations
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ClinicalFunctional_ScoringModel_v4115
        implements HomeHealthScoringModelIF {

    private final int id;
    protected DataManagerIF grouperDataManager;
    protected HomeHealthGrouperIF grouper;
    private transient String shortName;
    private boolean useOptionalLogic;

    /**
     * Constructor with references to the Grouper, the Data manager, and the
     * equation id
     *
     * @param grouper
     * @param grouperDataManager
     * @param equationId
     */
    public ClinicalFunctional_ScoringModel_v4115(HomeHealthGrouperIF grouper,
            DataManagerIF grouperDataManager,
            int equationId) {
        this(grouper, grouperDataManager, equationId, "Clinical/Functional, Equation " + equationId + ": ");
    }

    /**
     *
     * @param grouper
     * @param grouperDataManager
     * @param equationId
     * @param shortName
     */
    public ClinicalFunctional_ScoringModel_v4115(HomeHealthGrouperIF grouper,
            DataManagerIF grouperDataManager, int equationId, String shortName) {
        this.id = equationId;
        this.grouperDataManager = grouperDataManager;
        this.grouper = grouper;
        this.shortName = shortName;
        this.useOptionalLogic = true;
    }

    public boolean isUseOptionalLogic() {
        return useOptionalLogic;
    }

    public void setUseOptionalLogic(boolean useOptionalLogic) {
        this.useOptionalLogic = useOptionalLogic;
    }

    @Override
    public HomeHealthGrouperIF getGrouper() {
        return grouper;
    }

    @Override
    public String getName() {
        return "Clinical / Functional Scoring Model V4115";
    }

    public String getShortName() {
        return this.shortName;
    }

    /**
     * @param codeValue
     * @return the Code with its value being codeValue. If the code is not valid
     * (found for this version), then the Invalid flag will be set to true and
     * all other information about the code will be meaningless. This method
     * should never return null.
     */
    protected DiagnosisCodeIF getDiagnosisCode(String codeValue) {
        DiagnosisCodeIF code = DiagnosisCode_Empty.DEFAULT;

        // get the diagnosis code from the data manager
        if (codeValue != null) {
            codeValue = codeValue.trim();
            if (!codeValue.isEmpty()) {
                code = grouperDataManager.getDiagnosisCode(codeValue);

                // if the code is not found in the manager, it is
                // considered invalid for this version.  However, this method
                // should never return a null, so create Diagnosis with the code
                // value - by default a Diagnosis code is not a ValidCode, and
                // is it not Valid for scoring
                if (code == null) {
                    code = grouperDataManager.createDiagnosisCode(codeValue, false, false);
                }
            }
        }

        return code;
    }

    /**
     * This populates the record with all Diagnosis codes that are valid for
     * this grouper version that are listed on the record. Codes that are found
     * are marked as Valid, otherwise the code is mark as not Valid, and not
     * Valid for scoring. This method should be called prior to scoring the
     *
     * @param record
     */
    @Override
    public void populateCodes(HomeHealthRecordIF record) {
        DiagnosisCodeIF tmpCode;

        tmpCode = record.getPRIMARY_DIAG_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPRIMARY_DIAG_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG1_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG1_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG2_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG2_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG3_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG3_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG4_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG4_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG5_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG5_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        /*
         * for the rest of the codes, (x3 and x4), if the optional logic it not used,
         * then do not move the PMT codes since they are not needed
         */
        if (isUseOptionalLogic()) {
            tmpCode = record.getPMT_DIAG_ICD_A3();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_A3(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_B3();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_B3(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_C3();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_C3(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_D3();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_D3(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_E3();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_E3(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_F3();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_F3(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_A4();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_A4(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_B4();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_B4(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_C4();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_C4(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_D4();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_D4(getDiagnosisCode(tmpCode.getCode()));
            }

            tmpCode = record.getPMT_DIAG_ICD_E4();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_E4(getDiagnosisCode(tmpCode.getCode()));
//            } else if (!isUseOptionalLogic()) {
//                tmpCode.setValidForScoring(false);
            }

            tmpCode = record.getPMT_DIAG_ICD_F4();
            if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
                record.setPMT_DIAG_ICD_F4(getDiagnosisCode(tmpCode.getCode()));
            }
        }
    }

    /**
     *
     * @param record
     * @param validator
     * @return
     * @deprecated
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * No longer used
     *
     * @param record
     * @param validator
     * @deprecated
     */
    @Override
    public void preprocessRecord(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        throw new UnsupportedOperationException();
    }

    /**
     * There is no preprocessing of the HomeHealth Record for this model, and
     * this does nothing.
     *
     * @param record
     * @param validator
     * @param listeners - can be null, otherwise list of listeners for notified
     * of events
     */
    @Override
    public void preprocessRecord(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator,
            Collection<HomeHealthEventListenerIF> listeners) {
        // not needed for this scoring model
    }

    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator,
            int[][] scoreOrder,
            Collection<HomeHealthEventListenerIF> listeners) {
        final ScoringPoints points = new ScoringPoints(2);

        // process the record prior to detailed scoring
        preprocessRecord(record, validator, listeners);

        // score the clinical and functional parts, assigning them to the results
        points.setScoreAt(0, scoreClinical(record, validator, scoreOrder, listeners));
        points.setScoreAt(1, scoreFunctional(record, validator, listeners));

        ScoringEventFormatter.fireScoringGeneral(listeners, this.grouper, this,
                "Final equation score (clinical + functional): ",
                Integer.toString(points.getScores()[0] + points.getScores()[1]));

        return points;
    }

    /**
     * Calls score() with listeners being null
     *
     * @param record
     * @param validator
     * @param scoreOrder
     * @return non-null ScoringPointsIF
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, int[][] scoreOrder) {
        return score(record, validator, scoreOrder, null);
    }

    /**
     * This section covers scoring of Diagnostic groups and Case Mix Adjustment
     * Rows that do not depend on Primary or Other specific values for the code
     *
     * May 2013: updated to use OASIS-C values
     *
     * @param record
     * @param diagCode
     * @param validator - used to check edits during scoring
     * @param diagIdx
     * @param scoringGrid
     * @param listeners - used to collect/report events
     * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
     */
    public void scoreAny(HomeHealthRecordIF record,
            DiagnosisCodeIF diagCode, HomeHealthRecordValidatorIF validator,
            int diagIdx, DiagnosisScoringGridIF scoringGrid,
            Collection<HomeHealthEventListenerIF> listeners) throws AlreadyScoredException {
        int tmpInt;
        int tmpInt2;
        int tmpScore;

        // determine the Diagnostic Group,
        switch (diagCode.getDiagnosticGroup().getId()) {
            case 1: // Blindness and low vision
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 1, id);
                scoringGrid.addScore(diagCode, diagIdx, 1,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 1, tmpScore);
                break;

            case 2: // Blood disorders
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 2, id);
                scoringGrid.addScore(diagCode, diagIdx, 2,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 2, tmpScore);
                break;

            case 3: // Cancer and selected benign neoplasms
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 3, id);
                scoringGrid.addScore(diagCode, diagIdx, 3,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 3, tmpScore);
                break;

            case 5: // Dysphagia
                if (record.isDiagnosticGroupOnRecord(12, diagIdx)) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 6, id);
                    scoringGrid.addScore(diagCode, diagIdx, 6,
                            tmpScore, true);
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 6, tmpScore);
                }

                if (validator.isTHERAPIES_Valid()
                        && validator.isINTERNAL_LOGIC_Valid()
                        && "1".equals(record.getTHH_ENT_NUTRITION())) {

                    // add Table 5, row 7
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 7, id);
                    scoringGrid.addScore(diagCode, diagIdx, 7,
                            tmpScore, true);
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 7, tmpScore);
                }
                break;

            case 6: // Gait Abnormality
                if (validator.isSTGPRSUL_Valid()) {
                    tmpInt = IntegerUtils.parseInt(record.getSTG_PRBLM_ULCER(), Integer.MAX_VALUE);
                    if (tmpInt >= 1 && tmpInt <= 4) {

                    // add Table 5, row 19
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 19, id);
                    scoringGrid.addScore(diagCode, diagIdx, 19,
                            tmpScore, true);
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 19, tmpScore);
                    }
                }
                break;

            case 7: // Gastrointestinal disorders
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 8, id);
                scoringGrid.addScore(diagCode, diagIdx, 8,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 8, tmpScore);

                if (validator.isOSTOMY_Valid()) {
                    tmpInt = IntegerUtils.parseInt(record.getOSTOMY(), Integer.MAX_VALUE);
                    if (tmpInt == 1 || tmpInt == 2) {
                        tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 9, id);
                        scoringGrid.addScore(diagCode, diagIdx, 9, tmpScore, true);
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 9, tmpScore);
                    }
                }

                if (record.isDiagnosticGroupOnRecord(10, diagIdx)
                        || record.isDiagnosticGroupOnRecord(11, diagIdx)
                        || record.isDiagnosticGroupOnRecord(12, diagIdx)
                        || record.isDiagnosticGroupOnRecord(13, diagIdx)) {

                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 10, id);
                    scoringGrid.addScore(diagCode, diagIdx, 10,
                            tmpScore, true);
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 10, tmpScore);
                }

                break;

            case 8: // Heart Disease
            case 9: // Hypertension
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 11, id);
                scoringGrid.addScore(diagCode, diagIdx, 11,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 11, tmpScore);
                break;

            case 10: // Neuro 1 - Brain disorders and paralysis
                if (validator.isCUR_TOILETING_Valid()) {
                    tmpInt = IntegerUtils.parseInt(record.getCRNT_TOILTG(), Integer.MAX_VALUE);
                    if (tmpInt >= 2 && tmpInt <= 4)  {
                        tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 13, id);
                        scoringGrid.addScore(diagCode, diagIdx, 13,                            tmpScore, true);
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 13, tmpScore);
                    }
                }

            // fall through to Neuro 2 since the next criteria could
            // also apply to Neuro 1
            case 11: // Neuro 2 - Peripheral neurological disorders
                if (validator.isCUR_DRESS_Valid()) {
                    tmpInt = IntegerUtils.parseInt(record.getCRNT_DRESS_UPPER(), Integer.MAX_VALUE);
                    tmpInt2 = IntegerUtils.parseInt(record.getCRNT_DRESS_LOWER(), Integer.MAX_VALUE);
                    if ((tmpInt >= 1 && tmpInt <=3) || (tmpInt2 >= 1 && tmpInt2 <=3)) {
                        tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 14, id);
                        scoringGrid.addScore(diagCode, diagIdx, 14, tmpScore, true);
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 14, tmpScore);
                    }
                }
                break;

            case 12: // Neuro 3 - Stroke
                // add Table 5, row 15
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 15, id);
                scoringGrid.addScore(diagCode, diagIdx, 15,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 15, tmpScore);

                if (validator.isCUR_DRESS_Valid()) {
                    tmpInt = IntegerUtils.parseInt(record.getCRNT_DRESS_UPPER(), Integer.MAX_VALUE);
                    tmpInt2 = IntegerUtils.parseInt(record.getCRNT_DRESS_LOWER(), Integer.MAX_VALUE);
                    if ((tmpInt >= 1 && tmpInt <=3) || (tmpInt2 >= 1 && tmpInt2 <=3)) {
                        tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 16, id);
                        scoringGrid.addScore(diagCode, diagIdx, 16,tmpScore, true);
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 16, tmpScore);
                    }
                }

                // MODIFIED FOR OASIS-C AMBULATION VALUES
                if (validator.isCUR_AMBULATION_Valid()) {
                    tmpInt = IntegerUtils.parseInt(record.getCRNT_AMBLTN(), Integer.MAX_VALUE);
                    if (tmpInt >= 4 && tmpInt <= 6) {
                        tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 17, id);
                        scoringGrid.addScore(diagCode, diagIdx, 17, tmpScore, true);
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 17, tmpScore);
                    }
                }
                break;

            case 13: //Neuro 4 - Multiple Sclerosis
                // MODIFIED FOR OASIS-C BATHING AND AMBULATIONS VALUES
                tmpInt = validator.isCUR_BATHING_Valid() ? IntegerUtils.parseInt(record.getCRNT_BATHG(), Integer.MIN_VALUE) : 0;
                tmpInt2 = validator.isCUR_TOILETING_Valid() ? IntegerUtils.parseInt(record.getCRNT_TOILTG(), Integer.MIN_VALUE) : 0;
                int tmpInt3 = validator.isCUR_TRANSFER_Valid() ? IntegerUtils.parseInt(record.getCRNT_TRNSFRNG(), Integer.MIN_VALUE) : 0;
                int tmpInt4 = validator.isCUR_AMBULATION_Valid() ? IntegerUtils.parseInt(record.getCRNT_AMBLTN(), Integer.MIN_VALUE) : 0;
                
                if (tmpInt >= 2 || tmpInt2 >= 2 || tmpInt3 >= 2 || tmpInt4 >= 4) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 18, id);
                    scoringGrid.addScore(diagCode, diagIdx, 18, tmpScore, true);
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                                this.grouper, this,
                                diagCode, diagIdx + 1, 18, tmpScore);
                }
                break;

            case 14: // Ortho 1 - Leg Disorders
                if (validator.isSTGPRSUL_Valid()) {
                    tmpInt = IntegerUtils.parseInt(record.getSTG_PRBLM_ULCER(), Integer.MIN_VALUE);
                    if (tmpInt >= 1 && tmpInt <= 4) {
                        tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 19, id);
                        scoringGrid.addScore(diagCode, diagIdx, 19, tmpScore, true);
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 19, tmpScore);
                    }
                }

            // fall through to Ortho 2 since the next criteria could
            // also apply to Ortho 1
            case 15: // Ortho 2 - Other Orthopedic disorders
                if (validator.isINTERNAL_LOGIC_Valid()
                        && validator.isTHERAPIES_Valid()
                        && ("1".equals(record.getTHH_IV_INFUSION())
                        || "1".equals(record.getTHH_PAR_NUTRITION()))) {

                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 20, id);
                    scoringGrid.addScore(diagCode, diagIdx, 20,
                            tmpScore, true);
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 20, tmpScore);
                }
                break;

            case 16: // Psych 1 - Affective and other psychoses, depression
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 21, id);
                scoringGrid.addScore(diagCode, diagIdx, 21,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 21, tmpScore);
                break;

            case 17: // Psych 2 - Degenerative and other organic psychiatric disorders
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 22, id);
                scoringGrid.addScore(diagCode, diagIdx, 22,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 22, tmpScore);
                break;

            case 18: // Pulmonary disorders
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 23, id);
                scoringGrid.addScore(diagCode, diagIdx, 23,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 23, tmpScore);

                if (validator.isCUR_AMBULATION_Valid()) {
                    tmpInt = IntegerUtils.parseInt(record.getCRNT_AMBLTN(), Integer.MIN_VALUE);
                    if (tmpInt >= 1 && tmpInt <= 6) {
                        tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 24, id);
                        scoringGrid.addScore(diagCode, diagIdx, 24, tmpScore, true);
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 24, tmpScore);
                    }
                }
                break;

            case 20: // Skin 2 - Ulcers and other skin conditions
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 28, id);
                scoringGrid.addScore(diagCode, diagIdx, 28,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 28, tmpScore);

            // fall through to Skin 1 since the next criteria could
            // also apply to Skin 2
            case 19: // Skin 1 -Traumatic wounds, burns and post-operative complications
                if (validator.isINTERNAL_LOGIC_Valid()
                        && validator.isTHERAPIES_Valid()
                        && ("1".equals(record.getTHH_IV_INFUSION())
                        || "1".equals(record.getTHH_PAR_NUTRITION()))) {

                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 27, id);
                    scoringGrid.addScore(diagCode, diagIdx, 27,
                            tmpScore, true);
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                            this.grouper, this,
                            diagCode, diagIdx + 1, 27, tmpScore);
                }
                break;

            case 21: // Tracheostomy care
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 29, id);
                scoringGrid.addScore(diagCode, diagIdx, 29,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 29, tmpScore);
                break;

            case 22: // Urostomy/Cystostomy Care
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 30, id);
                scoringGrid.addScore(diagCode, diagIdx, 30,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners,
                        this.grouper, this,
                        diagCode, diagIdx + 1, 30, tmpScore);
                break;
        }
    }

    /**
     *
     * September 2012 - Overriding this method in order to added check for
     * payment code pairings prior to scoring related codes.
     *
     * Though this "override" duplicates much the super implementation of this
     * method, it is worth looking at the super implementation for more info
     *
     * @param record
     * @param validator
     * @param scoreOrder
     * @param listeners
     * @return score
     * @see
     * ClinicalFunctional_ScoringModel_v3312#scoreClinial(HomeHealthRecord_IF
     * record, HomeHealthRecordValidatorIF validator, int scoreOrder[][])
     */
    public int scoreClinical(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int scoreOrder[][],
            Collection<HomeHealthEventListenerIF> listeners) {
        DiagnosisCodeIF diagCode;
        DiagnosisCodeIF pairedDiagCode;
        int totalScore;
        final DiagnosisScoringGridIF scoringGrid = new DiagnosisScoringGrid();

        ScoringEventFormatter.fireScoringSectionStart(listeners, grouper, this, "Clinical Scoring");

        // make two passes over the codes and only score the code if its
        // score order matches the currentPass indicator
        // pass 1 will score the stand alone etiology codes
        // pass 2 will score the manifestation/etiology codes
        for (int currentPass = 1; currentPass <= 2; currentPass++) {
            // loop through the column 2 codes - if the code is a VCode
            // then move to the 3rd and 4th columns to score
            for (int diagIdx = 0; diagIdx < 6; diagIdx++) {
                diagCode = record.getDiagnosisCode(diagIdx);

                /*
                 * There is a new set of payment codes in this version: primary code
                 * pairings as well as payment code pairing and standard scoring
                 * payment codes
                 */
                if (useOptionalLogic && diagCode.isVCode() && diagCode.isOptionalPaymentCode()) {
                    /*
                     * for optional payment codes go across the row to columns 3 & 4
                     * column 3 is now paired with payment code in column 1, so check
                     * that the pairing is correct before scoring. The payment codes
                     * are mutually exclusive in that a payment code can not be both a
                     * primary Awarding code and a Payment code at the same
                     * time.
                     */
                    if (diagCode.isEtiologyInPairingList(record.getOptionalDiagnosisCode3(diagIdx))) {
                        scoreClinicalColumn3_4(record, validator, diagIdx, scoringGrid,
                                scoreOrder, currentPass, listeners);
                    }
                } else {
                    /*
                     * Determine if this code's position is scored on this pass
                     * 
                     * These codes may not be recognized for scoring even if
                     * the score order marks them as scoring in a specific
                     * order
                     *
                     * Codes that are secondary codes that can not be pair
                     * with an etiology, or are excluded due to
                     *
                     */
                    if (currentPass == scoreOrder[diagIdx][0]
                            && diagCode.isValidForScoring()) {
                        // Now score the code
                        scoreClinicalCode(record, diagCode, validator, diagIdx,
                                scoringGrid, listeners);

                        // if the current code is a secondary only, determine
                        // whether it earns points or the etiology earns points
                        if (diagCode.isSecondaryOnly()) {
                            /*
                             * August 2011 - changed the way to get the the
                             * paired code
                             */
                            pairedDiagCode = ManifestationUtils.getPairedCode(grouper, record, diagCode, diagIdx);
                            if (pairedDiagCode != null && pairedDiagCode.isValidForScoring()) {
                                /*
                                 * if the current score for the etiology is
                                 * 0, then score it again to ensure that the
                                 * 0 was not due to a previous
                                 * etiology/manifestation contention
                                 */
                                if (scoringGrid.getTotalScoreForDiagnosis(pairedDiagCode.getOffset()) == 0) {
                                    scoreClinicalCode(record, pairedDiagCode,
                                            validator, pairedDiagCode.getOffset(),
                                            scoringGrid, listeners);
                                }

                                // now compare the scores
                                ScoringModelUtils.resolveEtiologyManifestationContention(
                                        this.grouper, this,
                                        pairedDiagCode, pairedDiagCode.getOffset(),
                                        diagCode, diagIdx, scoringGrid,
                                        listeners);
                            }
                        }
                    }
                }
            }
        }

        // add up the score and return it
        totalScore = scoringGrid.getTotalScore();

        // score the remaining Clinical related variables
        totalScore += scoreRemainingVariables(record, validator, totalScore, listeners);

        ScoringEventFormatter.fireScoringSectionFinished(listeners, grouper, this, "Clinical Scoring", totalScore);

        return totalScore;
    }

    /**
     * This overrides in order to check for primary awarding for Dx in position
     * 2, with the appropriate payment code in position 1
     *
     * @param record
     * @param diagCode
     * @param validator
     * @param diagIdx
     * @param scoringGrid
     * @param listeners
     */
    public void scoreClinicalCode(HomeHealthRecordIF record,
            DiagnosisCodeIF diagCode, HomeHealthRecordValidatorIF validator,
            int diagIdx, DiagnosisScoringGridIF scoringGrid,
            Collection<HomeHealthEventListenerIF> listeners) {

        ScoringEventFormatter.fireScoringGeneral(listeners, this.grouper, this, "Attempting to score Diagnosis ",
                diagCode.getCode(), " (", diagCode.getDiagnosticGroup().getDescription(),
                ") at position ", Integer.toString(diagIdx + 1));

        try {
            /*
             * determine if this is a primary code position which defined as:
             *   1) non-manifestion DX codes in position 1 
             *   2) non-manifestion DX in position 7 - deprecated due to no Optional Payment codes in I-10
             *   3) a manifestation in position 2
             *   4) a manifestation in postition 12 - deprecated due to no Optional Payment codes in I-10 
             * 
             *  New for this version are:
             *   5) non-manifestion DX codes in position 2 if the code in 
             *      position 1 is an awarding payment code
             *   6) a manifestation in position 3 if the code in position 2 is
             *      a paired etiology and awarded primary scoring because 
             *      their is an awarding payment code in position 1
             *   7) a manifestation in position 3 if the code in position 2 is
             *      a paired etiology and the manifestation in position 3 is
             *      awarded primary scoring because their is an awarding payment code 
             *      in position 1
             */
            if (diagIdx == 0
                    || diagIdx == 6
                    || diagCode.isSecondaryOnly() && (diagIdx == 1 || diagIdx == 12)) {
                // logic items 1 and 4
                scorePrimaryOnly(record, diagCode, diagIdx, scoringGrid, listeners);

            } else if (diagIdx == 1
                    && record.getPRIMARY_DIAG_ICD().isPrimaryAwardableCode()
                    && diagCode.getDiagnosticGroup().isAlternatePrimaryScorable()) {
                // logic item 5
                scorePrimaryOnly(record, diagCode, diagIdx, scoringGrid, listeners);

            } else if (diagIdx == 2
                    && diagCode.isSecondaryOnly()
                    && record.getPRIMARY_DIAG_ICD().isPrimaryAwardableCode()
                    && record.getOTH_DIAG1_ICD().getDiagnosticGroup().isAlternatePrimaryScorable()) {
                // logic item 6
                scorePrimaryOnly(record, diagCode, diagIdx, scoringGrid, listeners);

            } else if (diagIdx == 2
                    && diagCode.isSecondaryOnly()
                    && record.getPRIMARY_DIAG_ICD().isPrimaryAwardableCode()
                    && diagCode.getDiagnosticGroup().isAlternatePrimaryScorable()) {
                // logic item 7
                scorePrimaryOnly(record, diagCode, diagIdx, scoringGrid, listeners);

            } else {
                // all other code conditions
                scoreOtherOnly(record, diagCode, diagIdx, scoringGrid, listeners);
            }
            // all codes are scored as "any"
            scoreAny(record, diagCode, validator, diagIdx, scoringGrid, listeners);

        } catch (AlreadyScoredException e) {
            ScoringEventFormatter.fireDxGroupAlreadyScored(listeners, this.grouper, this, diagCode, diagIdx + 1, e);
        }
    }

    /**
     * After the first column (historically column 2) has determined that it is
     * an Optional payment code that can have a column 3 & 4, this method will
     * score those codes on the same row, and resolve any manifestation/
     * etiology contentions.
     *
     * @param record
     * @param validator
     * @param rowIdx
     * @param scoringGrid
     * @param scoreOrder
     * @param currentPass
     * @param listeners
     */
    public void scoreClinicalColumn3_4(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator,
            int rowIdx,
            DiagnosisScoringGridIF scoringGrid,
            int scoreOrder[][],
            int currentPass,
            Collection<HomeHealthEventListenerIF> listeners) {
        DiagnosisCodeIF diagCodeCol_3 = null;

        if (currentPass == scoreOrder[rowIdx][1]) {
            // score column 3 code if valid for scoring
            diagCodeCol_3 = record.getOptionalDiagnosisCode3(rowIdx);
            if (diagCodeCol_3.isValidForScoring()) {
                scoreClinicalCode(record, diagCodeCol_3, validator, rowIdx + 6, scoringGrid, listeners);
            }
        }

        if (currentPass == scoreOrder[rowIdx][2]) {
            // score column 4 code if a valid secondary code
            final DiagnosisCodeIF diagCodeCol_4 = record.getOptionalDiagnosisCode4(rowIdx);
            if (diagCodeCol_4.isValidForScoring()
                    && diagCodeCol_4.isSecondaryOnly()) {

                // make sure that the etiology is valid for this secondary.
                scoreClinicalCode(record, diagCodeCol_4, validator, rowIdx + 12, scoringGrid, listeners);
                ScoringModelUtils.resolveEtiologyManifestationContention(
                        this.grouper, this,
                        diagCodeCol_3, rowIdx + 6,
                        diagCodeCol_4, rowIdx + 12, scoringGrid, listeners);
            }
        }
    }

    /**
     * Score the functional part of the model, basically things such as CUR_?
     * and dressing.
     *
     * Pseudo code lines: 1492 thru 1517
     *
     * @param record
     * @param validator
     * @param listeners
     * @return the functional score
     */
    public int scoreFunctional(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator,
            Collection<HomeHealthEventListenerIF> listeners) {
        int score = 0;
        int tmpScore;
        int tmpInt;
        int tmpInt2;

        ScoringEventFormatter.fireScoringSectionStart(listeners, this.grouper, this, "Functional scoring");

        if (validator.isCUR_DRESS_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getCRNT_DRESS_UPPER(), Integer.MAX_VALUE);
            tmpInt2 = IntegerUtils.parseInt(record.getCRNT_DRESS_LOWER(), Integer.MAX_VALUE);
            if ((tmpInt >= 1 && tmpInt <= 3) || (tmpInt2 >= 1 && tmpInt2 <= 3)) {
            tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 46, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "For CUR_DRESS", 46, tmpScore);
        }
        }

        // MODIFIED FOR OASIS-C BATHING VALUES
        if (validator.isCUR_BATHING_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getCRNT_BATHG(), Integer.MAX_VALUE);
            if (tmpInt >= 2 && tmpInt <= 6) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 47, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "For CUR_BATHING", 47, tmpScore);
            }
        }

        if (validator.isCUR_TOILETING_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getCRNT_TOILTG(), Integer.MAX_VALUE);
            if (tmpInt >= 2 && tmpInt <= 4) {   
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 48, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "For CUR_TOILETING", 48, tmpScore);
            }
        }

        if (validator.isCUR_TRANSFER_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getCRNT_TRNSFRNG(), Integer.MAX_VALUE);
            if (tmpInt >= 2 && tmpInt <= 5) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 49, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "For CUR_TRANSFER", 49, tmpScore);
            }
        }

        if (validator.isCUR_AMBULATION_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getCRNT_AMBLTN(), Integer.MAX_VALUE);
            if (tmpInt >= 1 && tmpInt <= 3) {
                // MODIFIED FOR OASIS-C AMBULATION VALUES
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 50, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "For CUR_AMBULATION", 50, tmpScore);
            } else if (tmpInt >= 4 && tmpInt <= 6) {
                // MODIFIED FOR OASIS-C AMBULATION VALUES
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 51, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "For CUR_AMBULATION", 51, tmpScore);
            }
        }
        ScoringEventFormatter.fireScoringSectionFinished(listeners, this.grouper, this, "Functional scoring", score);

        return score;
    }

    /**
     * Scores only those codes that can not be in the primary position
     *
     * @param record
     * @param diagCode
     * @param diagIdx
     * @param scoringGrid
     * @param listeners
     */
    public void scoreOtherOnly(HomeHealthRecordIF record,
            DiagnosisCodeIF diagCode, int diagIdx,
            DiagnosisScoringGridIF scoringGrid,
            Collection<HomeHealthEventListenerIF> listeners) throws AlreadyScoredException {
        int tmpScore;

        // score the Dx Groups that have specific Other diagnosis
        // scoring values from their Primary code scoring values.
        // In all cases, the Group can not be scored twice when
        // the code is in an Other position
        switch (diagCode.getDiagnosticGroup().getId()) {
            case 4:  // Diabetes
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 5, id);
                scoringGrid.addScore(diagCode, diagIdx, 5,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners, this.grouper, this,
                        diagCode, diagIdx + 1, 5, tmpScore);

                // no other scoring is require for this other code
                break;

            case 19: // Skin 1 -Traumatic wounds, burns and post-operative complications
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 26, id);
                scoringGrid.addScore(diagCode, diagIdx, 26,
                        tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(listeners, this.grouper, this,
                        diagCode, diagIdx + 1, 26, tmpScore);

                // this Dx Group has some primary or other code related scoring
                break;
        }
    }

    /**
     * Scores the Primary only code with Primary only CaseMix values.
     *
     * @param record
     * @param diagCode
     * @param diagIdx
     * @param scoringGrid
     * @param listeners
     * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
     */
    public void scorePrimaryOnly(HomeHealthRecordIF record,
            DiagnosisCodeIF diagCode, int diagIdx,
            DiagnosisScoringGridIF scoringGrid,
            Collection<HomeHealthEventListenerIF> listeners) throws AlreadyScoredException {

        //----------------------------------------------------
        // Primary codes
        //
        // if the code is a
        // potential primary code (an etiology in position index 0 or 6, or a
        // manifestation in position 2 or 12), then we can sometimes score the
        // code differently then when it is in an Other position.
        // Because of the 2 pass logic, in some cases, we
        // can score some Diagnostic Groups twice, but with a different
        // case mix adjustment row. When scoring the same Group twice,
        // the previous score from pass 1, must be substracted from the
        // the primary code score, so as not to over count the scoring.
        // This effects only a couple Dx Groups that have Primary code specific
        // values.
        int tmpScore;

        // score the code as a primary code
        switch (diagCode.getDiagnosticGroup().getId()) {
            case 4:  // Diabetes
                tmpScore = ScoringModelUtils.calculatePrimaryAdjustment(this.grouperDataManager, true, id, scoringGrid, 4, 5);
                scoringGrid.addScore(diagCode, diagIdx, 4, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(listeners, this.grouper, this, diagCode,
                        diagIdx + 1, 4, tmpScore);

                // this Dx Group has some primary or other code related scoring
                break;

            case 10: // Neuro 1 - Brain disorders and paralysis
                // Neuro 1 does not have an explicit "Other" row, so there
                // is nothing to adjust, just use the primary code as is
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 12, id);
                scoringGrid.addScore(diagCode, diagIdx, 12, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(listeners, this.grouper, this, diagCode,
                        diagIdx + 1, 12, tmpScore);

                // no other scoring is require for this primary code
                break;

            case 19: // // Skin 1 -Traumatic wounds, burns and post-operative complications
                tmpScore = ScoringModelUtils.calculatePrimaryAdjustment(this.grouperDataManager, true, id, scoringGrid, 25, 26);
                scoringGrid.addScore(diagCode, diagIdx, 25, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(listeners, this.grouper, this, diagCode,
                        diagIdx + 1, 25, tmpScore);

                // this Dx Group has some primary or other code related scoring
                break;
        }
    }

    /**
     * This scores the remaining clinical variables.
     *
     * @param record
     * @param validator
     * @return
     */
    public int scoreRemainingVariables(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int currentScore,
            Collection<HomeHealthEventListenerIF> listeners) {

        int score = 0;
        int tmpScore;
        int tmpInt;
        
        if (validator.isINTERNAL_LOGIC_Valid()
                && validator.isTHERAPIES_Valid()) {

            if ("1".equals(record.getTHH_IV_INFUSION())
                    || "1".equals(record.getTHH_PAR_NUTRITION())) {

                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 31, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item THH_IV_INFUSION or THH_PAR_NUTRITION", 31, tmpScore);
            }

            if ("1".equals(record.getTHH_ENT_NUTRITION())) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 32, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item THH_ENT_NUTRITION", 32, tmpScore);
            }
        }

        tmpInt = IntegerUtils.parseInt(record.getVISION(), Integer.MAX_VALUE);
        if (tmpInt == 1 || tmpInt == 2) {
            tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 33, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item VISION", 33, tmpScore);
        }

        // MODIFIED FOR OASIS-C FREQUENCY PAIN VALUES
        tmpInt = IntegerUtils.parseInt(record.getPAIN_FREQ_ACTVTY_MVMT(), Integer.MAX_VALUE);
        if (tmpInt == 3 || tmpInt == 4) {
            tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 34, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item FREQ_PAIN", 34, tmpScore);
        }

        if (validator.isNPRSULC3_Valid()
                && validator.isNPRSULC4_Valid()) {
            int tmpInt2 = 0;
            String tmpStr;

            tmpInt = 0;
            tmpStr = record.getNBR_PRSULC_STG3();
            if (!ValidateUtils.isEmpty(tmpStr)) {
                tmpInt = IntegerUtils.parseInt(tmpStr, 0);
            }
            tmpStr = record.getNBR_PRSULC_STG4();
            if (!ValidateUtils.isEmpty(tmpStr)) {
                tmpInt2 = IntegerUtils.parseInt(tmpStr, 0);
            }

            if (tmpInt + tmpInt2 >= 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 35, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item NBR_PRSULC_STG3 + NBR_PRSULC_STG4", 35, tmpScore);
            }
        }

        if (validator.isSTGPRSUL_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getSTG_PRBLM_ULCER(), Integer.MAX_VALUE);
            if (tmpInt == 1 || tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 36, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item STG_PRBLM_ULCER = 01 or 02", 36, tmpScore);

            } else if (tmpInt == 3 || tmpInt == 4) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 37, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item STG_PRBLM_ULCER = 03 or 04", 37, tmpScore);
            }
        }

        if (validator.isSTATSTASIS_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getSTUS_PRBLM_STAS_ULCR(), Integer.MAX_VALUE);
            if (tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 38, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item STAT_PRB_STASULC = 02", 38, tmpScore);
            } else if (tmpInt == 3) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 39, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item STAT_PRB_STASULC = 03", 39, tmpScore);
            }
        }

        if (validator.isSTATSURG_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getSTUS_PRBLM_SRGCL_WND(), Integer.MAX_VALUE);
            if (tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 40, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item STAT_PRB_SURGWND = 02", 40, tmpScore);
            } else if (tmpInt == 3) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 41, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item STAT_PRB_SURGWND = 03", 41, tmpScore);
            }
        }

        if (validator.isDYSPNEIC_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getWHEN_DYSPNEIC(), Integer.MAX_VALUE);
            if (tmpInt >= 2 && tmpInt <= 4) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 42, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item WHEN_DYSPNEIC = 02,03 or 04", 42, tmpScore);
            }
        }

        if (validator.isBWLINCONT_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getBWL_INCONT(), Integer.MAX_VALUE);
            if (tmpInt >= 2 && tmpInt <= 5) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 43, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item BWL_INCONT = 02,03,04 or 05", 43, tmpScore);
            }
        }

        if (validator.isOSTOMY_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getOSTOMY(), Integer.MAX_VALUE);
            if (tmpInt == 1 || tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 44, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item OSTOMY = 01 or 02", 44, tmpScore);
            }
        }

        if (validator.isCUR_INJECT_MEDS_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getCRNT_MGMT_INJCTN_MDCTN(), Integer.MAX_VALUE);
            if (tmpInt >= 0 && tmpInt <= 3) { //OVK: 6/16/16, PBI: 144769
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, true, 45, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, "Special Item CUR_INJECT_MEDS = 00, 01, 02 or 03", 45, tmpScore);
            }
        }

        return score;
    }

    @Override
    public void setGrouper(HomeHealthGrouperIF grouper) {
        this.grouper = grouper;
    }

    @Override
    public void setName(String name) {
        // can't really do this
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        // can't really do this
    }

}
