/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.v8119;

import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DataManagerIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_D_IF;
import com.mmm.cms.homehealth.v5115.NRS_ScoringModel_v5115;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ScoringModelUtils;
import com.mmm.cms.util.ValidateUtils;
import java.util.Collection;

/**
 *
 * @author US340646
 */
public class NRS_ScoringModel_v8119 extends NRS_ScoringModel_v5115{
    
    public NRS_ScoringModel_v8119(HomeHealthGrouperIF grouper, DataManagerIF dataManager) {
        super(grouper, dataManager);
    }

    @Override
    public String getName() {
        return "Non-Routine Supplies Scoring Model V8119";
    }

    @Override
    public int scoreRemainingVariables(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int currentScore,
            Collection<HomeHealthEventListenerIF> listeners) {

        int score = 0;
        String tmpStr;
        int tmpInt;
        int tmpScore;

        ScoringEventFormatter.fireScoringSectionStart(listeners, grouper, this, CommonMessageText.CALCULATE_SPECIAL_ITEMS);

        //---------------------------------------------
        // Score the skin condition related items first
        //---------------------------------------------
        if (validator.isNPRSULC1_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getNBR_PRSULC_STG1(), Integer.MAX_VALUE);
            
            if (tmpInt == 1 || tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 20, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG1, 20, tmpScore);
            } else if (tmpInt == 3 || tmpInt == 4) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 21, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG1, 21, tmpScore);
            }
        }

        if (validator.isNPRSULC2_Valid()) {
            tmpStr = record.getNBR_PRSULC_STG2();
            if (!ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES, ValidateUtils.ARRAY_DASH_VALUES)) {
                tmpInt = IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE);
                if (1 == tmpInt) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 22, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 22, tmpScore);
                } else if (2 == tmpInt) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 23, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 23, tmpScore);
                } else if (3 == tmpInt) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 24, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 24, tmpScore);
                } else if (4 <= tmpInt) {
                    // any value 4 or above 
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 25, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 25, tmpScore);
                }
            }

        }

        if (validator.isNPRSULC3_Valid()) {
            // MODIFIED FOR OASIS-C NUMBER PRESSURE ULCER STAGE 3 VALUES
            tmpStr = record.getNBR_PRSULC_STG3();
            if (!ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES, ValidateUtils.ARRAY_DASH_VALUES)) {
                tmpInt = IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE);
                if (1 == tmpInt) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 26, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 26, tmpScore);
                } else if (2 == tmpInt) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 27, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 27, tmpScore);
                } else if (3 == tmpInt) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 28, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 28, tmpScore);
                } else if (4 <= tmpInt) {
                    // any value 4 or above 
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 29, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 29, tmpScore);
                }
            }
        }

        if (validator.isNPRSULC4_Valid()) {
            // MODIFIED FOR OASIS-C NUMBER PRESSURE ULCER STAGE 4 VALUES
            tmpStr = record.getNBR_PRSULC_STG4();
            if (!ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES, ValidateUtils.ARRAY_DASH_VALUES)) {
                tmpInt = IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE);
                if (1 == tmpInt) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 30, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 30, tmpScore);
                } else if (2 == tmpInt) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 31, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 31, tmpScore);
                } else if (3 <= tmpInt) {
                    // any value 3 or above 
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 32, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 32, tmpScore);
                }
            }
        }

        /*
         * Non-staging pressure ulcer is concerned with Dressing and Covering
         */
        if (validator.isUNOBS_PRSULC_Valid()) {
            tmpInt = IntegerUtils.parseInt(((HomeHealthRecord_D_IF) record).getNSTG_DRSG(), Integer.MIN_VALUE);
            int tmpInt2 = IntegerUtils.parseInt(((HomeHealthRecord_D_IF) record).getNSTG_CVRG(), Integer.MIN_VALUE);
            if (tmpInt > 0 || tmpInt2 > 0) {
              tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 33, getId());
              score += tmpScore;
              ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NSTG_DRSG_or_NSTG_CVRG, 33, tmpScore);
            }
        }

        if (validator.isNBR_STASULC_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getNBR_STAS_ULCR(), Integer.MAX_VALUE);
            if (tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 34, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 34, tmpScore);
            } else if (tmpInt == 3) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 35, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 35, tmpScore);
            } else if (tmpInt == 4) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 36, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 36, tmpScore);
            }
        }

        /* 
         * STAS_ULCR_PRSNT
         * 0 - No
         * 1 - Yes, patient has BOTH observable and unobservable stasis ulcers
         * 2 - Yes, patient has observable stasis ulcers ONLY
         * 3 - Yes, patient has unobservable stasis ulcers ONLY (known but not 
         *      observable due to non-removable dressing)
         */
        if (validator.isUNOBS_STASULC_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getSTAS_ULCR_PRSNT(), Integer.MAX_VALUE);
            if (tmpInt == 1 || tmpInt == 3) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 37, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAS_ULCR_PRSNT, 37, tmpScore);
            }
        }

        if (validator.isSTATSTASIS_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getSTUS_PRBLM_STAS_ULCR(), Integer.MAX_VALUE);
            if (tmpInt == 1) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 38, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 38, tmpScore);
            } else if (tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 39, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 39, tmpScore);
            } else if (tmpInt == 3) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 40, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 40, tmpScore);
            }
        }

        if (validator.isSTATSURG_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getSTUS_PRBLM_SRGCL_WND(), Integer.MAX_VALUE);
            if (tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 41, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_SURGWND, 41, tmpScore);
            } else if (tmpInt == 3) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 42, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_SURGWND, 42, tmpScore);
            }
        }

        if (validator.isOSTOMY_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getOSTOMY(), Integer.MAX_VALUE);
            if (tmpInt == 1) {
		// only score row 45 if there have already been scores
                // for rows 1-42
                if (currentScore > 0 || score > 0) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 45, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_OSTOMY_SKIN, 45, tmpScore);
                }
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 43, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_OSTOMY, 43, tmpScore);
            } else if (tmpInt == 2) {
		// only score row 46 if there have already been scores
                // for rows 1-42
                if (currentScore > 0 || score > 0) {
                    tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 46, getId());
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_OSTOMY_SKIN, 46, tmpScore);
                }
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 44, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_OSTOMY, 44, tmpScore);
            }
        }

        if (validator.isTHERAPIES_Valid()
                && validator.isINTERNAL_LOGIC_Valid()
                && "1".equals(record.getTHH_IV_INFUSION())) {
            tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 47, getId());
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_THH_IV_INFUSION, 47, tmpScore);
        }

         
        if (validator.isUR_INCONT_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getUR_INCONT(), Integer.MAX_VALUE);   
            if (tmpInt == 2) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 48, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_UR_INCONT, 48, tmpScore);
            }
        }

        if (validator.isBWLINCONT_Valid()) {
            tmpInt = IntegerUtils.parseInt(record.getBWL_INCONT(), Integer.MAX_VALUE);   
            if (tmpInt == 4 || tmpInt == 5) {
                tmpScore = ScoringModelUtils.getCaseMixAdjustmentEquation(this.grouperDataManager, false, 49, getId());
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(listeners, this.grouper, this, CommonMessageText.EVENT_SCORE_MSG_BWL_INCONT, 49, tmpScore);
            }
        }

        ScoringEventFormatter.fireScoringSectionFinished(listeners, grouper, this,
                CommonMessageText.CALCULATE_SPECIAL_ITEMS, score);

        return score;
    }
    
}
