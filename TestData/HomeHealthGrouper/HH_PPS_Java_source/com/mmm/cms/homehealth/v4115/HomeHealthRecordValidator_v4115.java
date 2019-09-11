/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v4115;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditsEN;
import com.mmm.cms.homehealth.DataValidityFlag;
import com.mmm.cms.homehealth.RecordValidatorFactory;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C1_IF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.proto.RecordItemValidator_HH_PPS_IF;
import com.mmm.cms.homehealth.proto.RecordType;
import com.mmm.cms.homehealth.proto.RecordValidatorFactoryIF;
import com.mmm.cms.homehealth.vut.CollectionValidationEdits;
import com.mmm.cms.homehealth.vut.pps.HhPpsValidatorItems;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Validator is different from the previous due to the OASIS-C1 record
 * specs defining the edit/error numbers and the use of the VUT for the actual
 * validation. For historical reasons, the similar methods are used for "valid"
 * indicator, however, the "is...INVLD" methods (which have been deprecated)
 * have been disabled in favor of the "is...Valid" methods.
 *
 * In order to determine valid items, this will check the
 * CollectionValidationEditsIF items to determine the error id and the
 * associated OASIS-C1 item.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthRecordValidator_v4115 implements HomeHealthRecordValidatorIF {

    /**
     * The list of RecordItemValidatorIF items used to validate the OASIS
     * defined, but HH-PPS related items.
     */
    protected RecordItemValidatorIF[] oasisRecordItemValidators;

    /**
     * The list of RecordItemValidator_HH_PPS_IF items use to validate the Home
     * Health record based diagnosis codes specific to the grouper
     */
    protected RecordItemValidator_HH_PPS_IF[] hhPpsRecordItemValidators;

    /**
     * the overall data validity information
     */
    protected DataValidityFlagIF dataValidityFlag;

    protected int[] diagnosisScoringStatus;
    /**
     * VUT compatible edit information
     */
    protected CollectionValidationEditsIF edits;
    protected transient HomeHealthGrouperIF grouper;

    protected boolean skipOasisRecordValidation;

    /**
     * Constructor with the required Grouper reference
     *
     * @param grouper
     */
    public HomeHealthRecordValidator_v4115(HomeHealthGrouperIF grouper) {
        this.grouper = grouper;
        this.dataValidityFlag = new DataValidityFlag();
        this.diagnosisScoringStatus = new int[18];
        this.edits = new CollectionValidationEdits();
    }

    /**
     * Constructor with the required Grouper reference
     *
     * @param grouper
     * @param edits - this validator uses the supplied edits instead of
     * re-validating
     *
     */
    public HomeHealthRecordValidator_v4115(HomeHealthGrouperIF grouper,
            CollectionValidationEditsIF edits) {
        this.grouper = grouper;
        this.dataValidityFlag = new DataValidityFlag();
        this.diagnosisScoringStatus = new int[18];
        if (edits == null) {
            this.edits = new CollectionValidationEdits();
        } else {
            this.edits = edits;
            this.skipOasisRecordValidation = true;
        }
    }

    /**
     * gets the list of Oasis record item validators
     *
     * @param record - non-null record
     * @return non-null list of RecordItemValidatorIF
     */
    public RecordItemValidatorIF[] getOasisRecordItemValidators(HomeHealthRecordIF record) {
        if (this.oasisRecordItemValidators == null && !this.skipOasisRecordValidation) {
            RecordValidatorFactoryIF valFactory;
            try {
                valFactory = RecordValidatorFactory.getInstance(null);
                this.oasisRecordItemValidators = valFactory.getValidationItems(record);
            } catch (Exception ex) {
                Logger.getLogger(HomeHealthRecordValidator_v4115.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // create a blank array
            this.oasisRecordItemValidators = new RecordItemValidatorIF[0];
        }
        return this.oasisRecordItemValidators;
    }

    /**
     * sets the list of Oasis record item validators
     *
     * @param oasisRecordItemValidators - non-null RecordItemValidatorIF list
     */
    public void setOasisRecordItemValidators(RecordItemValidatorIF[] oasisRecordItemValidators) {
        this.oasisRecordItemValidators = oasisRecordItemValidators;
    }

    /**
     * get the list of HH-PPS validators
     *
     * @param record
     * @return
     */
    public RecordItemValidator_HH_PPS_IF[] getHhPpsRecordItemValidators(HomeHealthRecordIF record) {
        if (this.hhPpsRecordItemValidators == null) {
            final HhPpsValidatorItems validator = HhPpsValidatorItems.getInstance();
            this.hhPpsRecordItemValidators = validator.getValidationItems(this.grouper, record);
        }
        return hhPpsRecordItemValidators;
    }

    /**
     * sets the list of HH-PPS validators
     *
     * @param hhPpsRecordItemValidators
     */
    public void setHhPpsRecordItemValidators(RecordItemValidator_HH_PPS_IF[] hhPpsRecordItemValidators) {
        this.hhPpsRecordItemValidators = hhPpsRecordItemValidators;
    }

    /**
     * gets if assessment reason is valid
     *
     * @return true if there are no edit for ASSMT REASON
     */
    @Override
    public boolean isASSMT_REASON_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M0100_ASSMT_REASON);
    }

    /**
     * This is not really necessary since the record has already been converted,
     * therefore the date was valid.
     *
     * @return true is there are no edit items for INFO COMPLETE
     */
    @Override
    public boolean isINFO_COMPLETED_DT_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M0090_INFO_COMPLETED_DT);
    }

    @Override
    public boolean isTHERAPIES_Valid() {

        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_IV_INFUSION)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_PAR_NUTRITION)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_ENT_NUTRITION)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_NONE_ABOVE);
    }

    @Override
    public boolean isINTERNAL_LOGIC_Valid() {
        return !this.edits.isEditPresent(OasisEditsEN.EDIT_3880)
                && !this.edits.isEditPresent(OasisEditsEN.EDIT_3890);
    }

    /**
     * @return
     */
    @Override
    public boolean isVISION_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1200_VISION);
    }

    @Override
    public boolean isPAIN_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1242_PAIN_FREQ_ACTVTY_MVMT);
    }

    @Override
    public boolean isLESION_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1350_LESION_OPEN_WND);
    }

    @Override
    public boolean isNPRSULC1_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1322_NBR_PRSULC_STG1);
    }

    @Override
    public boolean isNPRSULC2_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2);
    }

    @Override
    public boolean isNPRSULC3_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3);
    }

    @Override
    public boolean isNPRSULC4_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4);
    }

    @Override
    public boolean isUNOBS_PRSULC_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DRSG)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE);
    }

    @Override
    public boolean isSTGPRSUL_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1324_STG_PRBLM_ULCER);
    }

    @Override
    public boolean isNBR_STASULC_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1332_NBR_STAS_ULCR);
    }

    @Override
    public boolean isUNOBS_STASULC_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1330_STAS_ULCR_PRSNT);
    }

    @Override
    public boolean isSTATSTASIS_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1334_STUS_PRBLM_STAS_ULCR);
    }

    @Override
    public boolean isSTATSURG_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1340_SRGCL_WND_PRSNT);
    }

    @Override
    public boolean isDYSPNEIC_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1400_WHEN_DYSPNEIC);
    }

    @Override
    public boolean isUR_INCONT_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1610_UR_INCONT)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1615_INCNTNT_TIMING);
    }

    @Override
    public boolean isBWLINCONT_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1620_BWL_INCONT);
    }

    @Override
    public boolean isOSTOMY_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1630_OSTOMY);
    }

    @Override
    public boolean isCUR_DRESS_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1810_CRNT_DRESS_UPPER)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1820_CRNT_DRESS_LOWER);
    }

    @Override
    public boolean isCUR_BATHING_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1830_CRNT_BATHG);
    }

    @Override
    public boolean isCUR_TOILETING_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1840_CRNT_TOILTG);
    }

    @Override
    public boolean isCUR_TRANSFER_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1850_CRNT_TRNSFRNG);
    }

    @Override
    public boolean isCUR_AMBULATION_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1860_CRNT_AMBLTN);
    }

    /**
     *
     * @return @deprecated
     */
    @Override
    public boolean isCUR_INJECT_MEDS_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN);
    }

    @Override
    public boolean isTHER_NEED_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2200_THER_NEED_NBR);
    }

    /**
     * gets the DataValidityFlag information
     *
     * @return the data validity flag object - this will never be null
     */
    @Override
    public DataValidityFlagIF getDataValidityFlag() {
        return dataValidityFlag;
    }

    /**
     * sets the data validity flag object - should not be null
     *
     * @param dataValidityFlag
     */
    @Override
    public void setDataValidityFlag(DataValidityFlagIF dataValidityFlag) {
        if (dataValidityFlag == null) {
            throw new IllegalArgumentException("dataValidityFlag parameter can not be null");
        }
        this.dataValidityFlag = dataValidityFlag;
    }

    /**
     * gets the ClinicalDomainDataIssueFlag
     *
     * @return
     */
    @Override
    public final boolean isClinicalDomainDataIssueFlag() {
        return dataValidityFlag.isClinicalIssue();
    }

    /**
     * gets the FunctionalDomainDataIssueFlag
     *
     * @return
     */
    @Override
    public final boolean isFunctionalDomainDataIssueFlag() {
        return dataValidityFlag.isFunctionalIssue();
    }

    /**
     * gets the ManifestationSequenceIssueFlag
     *
     * @return
     */
    @Override
    public final boolean isManifestationSequenceIssueFlag() {
        return dataValidityFlag.isManifestationSequenceIssue();
    }

    /**
     * gets the ServiceDomainDataIssueFlag
     *
     * @return
     */
    @Override
    public final boolean isServiceDomainDataIssueFlag() {
        return dataValidityFlag.isServiceIssue();
    }

    /**
     * Get the value of diagnosisScoringStatus
     *
     * @return the value of diagnosisScoringStatus
     */
    @Override
    public int[] getDiagnosisScoringStatus() {
        return diagnosisScoringStatus;
    }

    public CollectionValidationEditsIF getEdits() {
        return edits;
    }

    /**
     * Set the value of diagnosisScoringStatus
     *
     * @param diagnosisScoringStatus new value of diagnosisScoringStatus
     */
    @Override
    public void setDiagnosisScoringStatus(int[] diagnosisScoringStatus) {
        this.diagnosisScoringStatus = diagnosisScoringStatus;
    }

    @Override
    public boolean validate(HomeHealthRecordIF record) {
        validate(record, null);
        return edits.isEmpty();
    }

    @Override
    public CollectionValidationEditsIF validate(HomeHealthRecordIF record,
            Collection<HomeHealthEventListenerIF> listeners) {
        DiagnosisCodeIF code;
        int idx;

        getOasisRecordItemValidators(record);
        for (RecordItemValidatorIF validatorItem : oasisRecordItemValidators) {
            validatorItem.validate(record, this.edits, this.dataValidityFlag);
        }

        getHhPpsRecordItemValidators(record);
//        if (hhPpsRecordItemValidators != null) { // DEBUG
            for (RecordItemValidator_HH_PPS_IF validatorItem : hhPpsRecordItemValidators) {
                validatorItem.validate(this.grouper, record, this.edits, this.dataValidityFlag);
            }
//        }

        //---------------------------------------------------
        // set the scoring information from record
        //---------------------------------------------------
        // reset the scoring status array based on the record type
        if (record.getRecordType() == RecordType.OASIS_C1_ICD_10
                || record.getRecordType() == RecordType.OASIS_C2
                || record.getRecordType() == RecordType.OASIS_D) {
            this.diagnosisScoringStatus = new int[6];
        }

        for (idx = 0; idx < diagnosisScoringStatus.length; idx++) {
            code = record.getDiagnosisCode(idx);
            if (code.isValidForScoring()) {
                this.diagnosisScoringStatus[idx] = 2;
            } else if (code.isValidCode() || code.isOptionalPaymentCode()) {
                this.diagnosisScoringStatus[idx] = 1;
            } else {
                this.diagnosisScoringStatus[idx] = 0;
            }
        }

        return edits;
    }

}
