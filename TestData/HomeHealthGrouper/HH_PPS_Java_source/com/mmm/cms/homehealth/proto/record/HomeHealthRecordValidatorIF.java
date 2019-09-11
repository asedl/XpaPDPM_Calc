/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto.record;

import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import java.util.Collection;



/**
 * Provides validation on the Oasis Record and can be performed specific
 * to the version.
 *
 * Once validated, this will indicate what items within the record have
 * issues or are invalid.
 *
 * @since V4115 - This interface is different from the original HomeHealthRecordValidatorIF
 * interface in that the validation methods in the original are "negative" in
 * nature, using the "is...Invalid" type of construct.  This leads to clumsy
 * constructs such as "if (!is...Invalid)" within the scoring modules.  So, this
 * interface uses are "positive" in nature, using the "is...Valid" type of construct.
 * This leads to cleaner code in the scoring modules such as "if (is...Valid) then ...".
 * So, the older "invalid" related methods are deprecated and will be removed
 * in the next version.
 * 
 * @since V5115 - "invalid" method were removed
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HomeHealthRecordValidatorIF {

    /**
     * This is the main entry into the validation of the record.  From here
     * other more detailed validation implementations are built and called.
     * If the value is true, then the scoring of the record should result
     * in a valid, meaningful score.
     *
     * @param record
     * @return true if the record is completely valid
     * 
     */
     boolean validate(HomeHealthRecordIF record);

    /**
     * This is the main entry into the validation of the record.  From here
     * other more detailed validation implementations are built and called.
     * If the value is true, then the scoring of the record should result
     * in a valid, meaningful score.
     *
     * @param record
     * @param listeners - can be null, otherwise list of listeners for notified of events
     * 
     * @return CollectionValidationEditsIF if null, then no edit issues, otherwise
     * non-null with at least one edit
     */
     CollectionValidationEditsIF validate(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners);

    
    /**
     * gets the DataValidityFlag
     * @return the data validity flag created during validation
     */
     DataValidityFlagIF getDataValidityFlag();

    /**
     * sets the data validity flag during validation
     * @param dataValidityFlag
     */
     void setDataValidityFlag(DataValidityFlagIF dataValidityFlag);

    /**
     * gets the ClinicalDomainDataIssueFlag
     * @return true if there was a clinical domain issue on the record
     */
     boolean isClinicalDomainDataIssueFlag();

    /**
     * gets the FunctionalDomainDataIssueFlag
     * @return true if there was a functional domain issue on the record
     */
     boolean isFunctionalDomainDataIssueFlag();

    /**
     * gets the ManifestationSequenceIssueFlag
     * @return true if there was a manifestation sequence issue on the record
     */
     boolean isManifestationSequenceIssueFlag();

    /**
     * gets the ServiceDomainDataIssueFlag
     * @return true if there was a service domain issue on the record
     */
     boolean isServiceDomainDataIssueFlag();

    /**
     * gets the status of a code as it pertains to scoring.
     * <ol>
     *   <li>0 = Invalid</li>
     *   <li>1 = Valid</li>
     *   <li>2 = Valid and scorable - actually scoring my rely on manifestion/contention logic
     * which is not represented here.</li>
     * </ol>
     *
     * @return
     */
     int[] getDiagnosisScoringStatus();

    /**
     * sets the status of a code as it pertains to scoring.
     * 
     * @param diagnosisScoringStatus a non-null array of ints designating the 
     * state of the code in that relative position
     */
     void setDiagnosisScoringStatus(int diagnosisScoringStatus[]);
    
    
    /**
     * gets the M0100_ASSMT_REASON_Valid
     * @return true if the assessment reason field was valid
     */
     boolean isASSMT_REASON_Valid();
   
    /**
     * gets the M0090_INFO_COMPLETED_DT_Valid
     * @return true if the info completed data field was valid
     */
     boolean isINFO_COMPLETED_DT_Valid();

    /**
     * gets the M0250_INTERNAL_LOGIC_Valid
     * @return true if the therapies has no internal logic error
     */
     boolean isINTERNAL_LOGIC_Valid();

    /**
     * gets the M0250_THERAPIES_Valid
     * @return true if the therapies are valid - not sure the reason
     * for this and the "internal logic invalid", but this is part of the
     * version 2.03 logic
     */
     boolean isTHERAPIES_Valid();

    /**
     * gets the M0390_VISION_Valid
     * @return true if the vision field was valid
     */
     boolean isVISION_Valid();

    /**
     * gets the M0420_PAIN_Valid
     * @return true if the pain field was valid
     */
     boolean isPAIN_Valid();

    /**
     * gets the M0440_LESION_Valid
     * @return true if the lesion field was valid
     */
     boolean isLESION_Valid();

    /**
     * gets the M0450_NPRSULC1_Valid
     * @return true if the non pressure ulcer stage 1 field was valid
     */
     boolean isNPRSULC1_Valid();

    /**
     * gets the M0450_NPRSULC2_Valid
     * @return true if the non pressure ulcer stage 2 field was valid
     */
     boolean isNPRSULC2_Valid();

    /**
     * gets the M0450_NPRSULC3_Valid
     * @return true if the non pressure ulcer stage 3 field was valid
     */
     boolean isNPRSULC3_Valid();

    /**
     * gets the M0450_NPRSULC4_Valid
     * @return true if the non pressure ulcer stage 4 field was valid
     */
     boolean isNPRSULC4_Valid();

    /**
     * gets the M0450_UNOBS_PRSULC_Valid
     * @return true if the unobserved ulcer field was valid
     */
     boolean isUNOBS_PRSULC_Valid();

    /**
     * gets the M0460_STGPRSUL_Valid
     * @return true if the stage pressure ulcers field was valid
     */
     boolean isSTGPRSUL_Valid();

    /**
     * gets the M0470_NBR_STASULC_Valid
     * @return true if the number of ulcers field was valid
     */
     boolean isNBR_STASULC_Valid();

    /**
     * gets the M0474_UNOBS_STASULC_Valid
     * @return true if the unobserved stasis ulcer field was valid
     */
     boolean isUNOBS_STASULC_Valid();

    /**
     * gets the M0476_STATSTASIS_Valid
     * @return true if the stat stasis field was valid
     */
     boolean isSTATSTASIS_Valid();

    /**
     * gets the M0488_STATSURG_Valid
     * @return true if the stat surgical field was valid
     */
     boolean isSTATSURG_Valid();

    /**
     * gets the M0490_DYSPNEIC_Valid
     * @return true if the dyspniec field was valid
     */
     boolean isDYSPNEIC_Valid();

    /**
     * gets the M0520_UR_INCONT_Valid
     * @return true if the urine incontinence field was valid
     */
     boolean isUR_INCONT_Valid();

    /**
     * gets the M0540_BWLINCONT_Valid
     * @return true if the bowl incontinence field was valid
     */
     boolean isBWLINCONT_Valid();

    /**
     * gets the M0550_OSTOMY_Valid
     * @return true if the ostomy field was valid
     */
     boolean isOSTOMY_Valid();

    /**
     * gets the M0650_660_CUR_DRESS_Valid
     * @return true if the current dress field was valid
     */
     boolean isCUR_DRESS_Valid();

    /**
     * gets the M0670_CUR_BATHING_Valid
     * @return true if the current bathing field was valid
     */
     boolean isCUR_BATHING_Valid();

    /**
     * gets the M0680_CUR_TOILETING_Valid
     * @return true if the current toileting field was valid
     */
     boolean isCUR_TOILETING_Valid();

    /**
     * gets the M0690_CUR_TRANSFER_Valid
     * @return true if the current transfer field was valid
     */
     boolean isCUR_TRANSFER_Valid();

    /**
     * gets the M0700_CUR_AMBULATION_Valid
     * @return true if the current ambulation field was valid
     */
     boolean isCUR_AMBULATION_Valid();

    /**
     * gets the M0800_CUR_INJECT_MEDS_Valid
     * @return true if the current injection medications field was valid
     */
     boolean isCUR_INJECT_MEDS_Valid();

    /**
     * gets the M0826_THER_NEED_Valid
     * @return true if the therapies needed field was valid
     */
     boolean isTHER_NEED_Valid();


}

