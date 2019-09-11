/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3413;

import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.v3312.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * This version is valid between January 2013 to Sept 30, 2013, with a grace
 * period window of October 1 through December 31, 2013. The grace period is
 * used instead of just extending the valid period in order to ensure that the
 * version is associated with a valid ICD-9 distribution.
 *
 * Changes to logic in this version includes the following adjustments to the
 * use of V-Codes: <ol> <li>V-Code / Dx code Pairing for Payment Dx Field –
 * V-Code (i.e. Payment Codes) have allowed any Dx coding the payment fields.
 * This version introduces V-Code pairing, in which Payment V-Codes will be
 * paired with specific Dx codes.</li> <li>	V-Codes in position 1 indicating
 * position 2 as Primary code – These V-Codes are not paired with a Dx code for
 * the payment field, but instead these V-Codes indicate that the code following
 * in position 2, if part of the DM, Neuro1 or Skin1 code Group should be
 * considered as Primary code for scoring purposes.</li> </ol>
 *
 *
 * @see com.mmm.cms.homehealth.v3312.GrouperVersion_v3312
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v3413 extends GrouperVersion_v3312 implements
		HomeHealthGrouperIF {

	Calendar gracePeriodThruDate;

	/**
	 * Constructor that sets the effective start and thru date to Jan 1, 2013
	 * thru Sept 30, 2013.
	 */
	public GrouperVersion_v3413() {
		this(new GregorianCalendar(2013, 0, 1), new GregorianCalendar(2013, 8, 30),
				null, "V3413");
	}

	/**
	 * This constructor allows extended classes to set dates in the constructor
	 *
	 * @param start
	 * @param thru
	 * @param window
	 * @param versionName
	 */
	public GrouperVersion_v3413(Calendar start, Calendar thru, Calendar window, String versionName) {
		super(start, thru, window, versionName);
		this.gracePeriodThruDate = new GregorianCalendar(2013, 11, 31);
	}

	@Override
	public HomeHealthRecordValidatorIF getClinicalValidator() {
		return new HomeHealthRecordClinicalValidator_v3413(this);
	}

	/**
	 * Get the name of this version
	 *
	 * @return the name of this version
	 */
	@Override
	public String getName() {
		return "HHA PPS Grouper - January 2013 Logic updates, "
				+ getVersion();
	}

	@Override
	public HomeHealthRecordValidatorIF getNRSValidator() {
		return new HomeHealthRecordClinicalValidator_v3413(this);
	}

	/**
	 * Sets up this version by loading the related Diagnosis code / Group data,
	 * and initializing the scoring models: 4 clinical/functional models (one
	 * for each equation), and one Non-Routine Supplies model
	 *
	 * This version uses it is own version of the scoring models and the grouper
	 * data manager
	 *
	 * @param props
	 * @throws java.lang.Exception
	 */
	@Override
	public void init(Properties props) throws Exception {
		GrouperDataManager grouperManager;

		grouperManager = new GrouperDataManager_V3413(this);
		setGrouperDataManager(grouperManager);
		grouperManager.init(props);

		clinicalModel_1 = new ClinicalFunctional_ScoringModel_v3413(this, grouperManager, 1);
		clinicalModel_2 = new ClinicalFunctional_ScoringModel_v3413(this, grouperManager, 2);
		clinicalModel_3 = new ClinicalFunctional_ScoringModel_v3413(this, grouperManager, 3);
		clinicalModel_4 = new ClinicalFunctional_ScoringModel_v3413(this, grouperManager, 4);

		nrsScoringModel = new NRS_ScoringModel_v3413(this, grouperManager);
	}

	/**
	 * This checks that the info complete date is either within the standard
	 * version period or is within the ending grace period. If so, it returns
	 * true, otherwise false.
	 *
	 * @param record
	 * @return
	 */
	@Override
	public boolean isValidForVersion(HomeHealthRecordIF record) {
		boolean valid;

		valid = super.isValidForVersion(record);
		if (!valid) {
			Calendar infoComplete = record.getINFO_COMPLETED_DT();
			if (infoComplete != null && infoComplete.after(super.getEffectiveDateThru())) {
				valid = !infoComplete.after(this.gracePeriodThruDate);
			}
		}
		return valid;
	}
}
