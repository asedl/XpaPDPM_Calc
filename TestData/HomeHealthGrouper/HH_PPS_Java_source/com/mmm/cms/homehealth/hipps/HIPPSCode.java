/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.hipps;

import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.util.ValidateUtils;

/**
 * This holds the 5 character HIPPS code based on the 5 elements of:
 * <ul>
 * <li>episode timing</li>
 * <li>Grouping steps</li>
 * <li>clinical Score</li>
 * <li>functionalScore</li>
 * <li>nrsScore</li>
 *
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HIPPSCode implements HIPPSCodeIF {

	private static final char BLANK = ' ';
	private static final boolean DEBUG;

	static {
		String str = System.getenv("home_health_hipps_debug");
		DEBUG = Boolean.parseBoolean(str);
	}
	protected int groupingStep;
	protected int hippsScoringEquation;
	protected char clinicalSeverity;
	protected char functionalSeverity;
	protected char servicesUtilization;
	protected char nonRoutineSupplies;
	protected String timing;
	protected int therapyNeedNumber;
	

	/**
	 * This constructor creates a blank HIPPS code.
	 */
	public HIPPSCode() {
		setToBlank();
	}

	/**
	 * This method will populate the HIPPS code based on the parameters. The
	 * record provided should be valid for the Grouper version, because this
	 * method does not check for that validity.
	 *
	 * @param record
	 * @param validator
	 * @param clinicalScore
	 * @param functionalScore
	 * @param nrsScore
	 * @since version v4414
	 */
	public HIPPSCode(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator,
			PointsScoringEquationsIF clinicalScore,
			PointsScoringEquationsIF functionalScore,
			int nrsScore) {

		this.therapyNeedNumber = record.getTHER_NEED_NBR();
		this.timing = record.getEPISODE_TIMING();

		//-----------------------------------
		// Check for valid record
		//-----------------------------------
		if (!validator.isINFO_COMPLETED_DT_Valid()
				|| !validator.isASSMT_REASON_Valid()
				|| !validator.isTHER_NEED_Valid()
				|| !ValidateUtils.isValidValue(timing, ValidateUtils.ARRAY_DOUBLE_ONE_TWO) && !ValidateUtils.UNKNOWN_UK.equals(timing)) {
			setToBlank();

			if (DEBUG) {
				if (validator.isTHER_NEED_Valid()) {
					clinicalSeverity = 'W';
				}
				if (validator.isASSMT_REASON_Valid()) {
					functionalSeverity = 'X';
				}
				if (!ValidateUtils.isValidValue(timing, ValidateUtils.ARRAY_DOUBLE_ONE_TWO) && !ValidateUtils.UNKNOWN_UK.equals(timing)) {
					servicesUtilization = 'Y';
				}
				if (validator.isINFO_COMPLETED_DT_Valid()) {
					nonRoutineSupplies = 'Z';
				}
			}
			return;
		}

		evaluateRecord(record, clinicalScore, functionalScore, nrsScore);

	}

	protected void evaluateRecord(HomeHealthRecordIF record,
			PointsScoringEquationsIF clinicalScore,
			PointsScoringEquationsIF functionalScore,
			int nrsScore) {
		int tmpInt;

		// determehe Grouping step and the equation value
		if (determineStepEquation()) {
			//-----------------------------------
			// Clinical Domain
			//-----------------------------------
			determineClinicalSeverity(clinicalScore.getEquationValue(hippsScoringEquation - 1));

			//-----------------------------------
			// Functional Domain
			//-----------------------------------
			determineFunctionalSeverity(functionalScore.getEquationValue(hippsScoringEquation - 1));

			//-----------------------------------
			// Service Domain
			//-----------------------------------
			determineServiceDomain();

			//-----------------------------------
			// NRS Group
			//-----------------------------------
			determineNrs(nrsScore);
		} else {
			setToBlank();
		}
	}

	/**
	 * gets the Clinical severity code
	 *
	 * @return the Clinical Severity character
	 */
	@Override
	public char getClinicalSeverity() {
		return clinicalSeverity;
	}

	/**
	 * Sets the Clinical Severity character
	 *
	 * @param clinicalSeverity
	 */
	@Override
	public void setClinicalSeverity(char clinicalSeverity) {
		this.clinicalSeverity = clinicalSeverity;
	}

	/**
	 * gets the Functional Severity code
	 *
	 * @return the Functional Severity character
	 */
	@Override
	public char getFunctionalSeverity() {
		return functionalSeverity;
	}

	/**
	 * Sets the Functional Severity character
	 *
	 * @param functionalSeverity
	 */
	@Override
	public void setFunctionalSeverity(char functionalSeverity) {
		this.functionalSeverity = functionalSeverity;
	}

	/**
	 * gets the Grouping step
	 *
	 * @return the Grouping step number
	 */
	@Override
	public int getGroupingStep() {
		return groupingStep;
	}

	/**
	 * Sets the Grouping step number
	 *
	 * @param groupingStep
	 */
	@Override
	public void setGroupingStep(int groupingStep) {
		this.groupingStep = groupingStep;
	}

	/**
	 * gets the NRS Supplied code
	 *
	 * @return the Non Routine Supplies character
	 */
	@Override
	public char getNonRoutineSupplies() {
		return nonRoutineSupplies;
	}

	/**
	 * Sets the Non Routine Supplies character
	 *
	 * @param nonRoutineSupplies
	 */
	@Override
	public void setNonRoutineSupplies(char nonRoutineSupplies) {
		this.nonRoutineSupplies = nonRoutineSupplies;
	}

	/**
	 * gets the Services Utilization code
	 *
	 * @return the Services Utilization character
	 */
	@Override
	public char getServicesUtilization() {
		return servicesUtilization;
	}

	/**
	 * Sets the Services Utilization character
	 *
	 * @param servicesUtilization
	 */
	@Override
	public void setServicesUtilization(char servicesUtilization) {
		this.servicesUtilization = servicesUtilization;
	}

	/**
	 * gets the HIPPS code
	 *
	 * @return The combined 5 character code. Will never be null.
	 */
	@Override
	public String getCode() {
		StringBuilder buffer = new StringBuilder();

		if (groupingStep > 0) {
			buffer.append(groupingStep);
		} else {
			buffer.append(BLANK);
		}

		buffer.append(clinicalSeverity);
		buffer.append(functionalSeverity);
		buffer.append(servicesUtilization);
		buffer.append(nonRoutineSupplies);

		return buffer.toString();
	}

	/**
	 * Sets the code to all blanks
	 */
	protected final void setToBlank() {
		groupingStep = 0;
		hippsScoringEquation = BLANK;
		clinicalSeverity = BLANK;
		functionalSeverity = BLANK;
		servicesUtilization = BLANK;
		nonRoutineSupplies = BLANK;
	}

	@Override
	final public String toString() {
		return getCode();
	}

	protected boolean determineStepEquation() {
		//-----------------------------------
		// check the timing of the record to set up the 
		// HIPPS equation and the Grouping step
		//-----------------------------------
		if ("01".equals(timing) || "UK".equals(timing)) {
			// check the number of therapies received
			if (therapyNeedNumber <= 13) {
				hippsScoringEquation = 1;
				groupingStep = 1;

			} else {
				hippsScoringEquation = 2;

				// grouping step has a different value for therapies
				// betweeen 14 and 19 inclusive
				if (therapyNeedNumber <= 19) {
					groupingStep = 2;
				} else {
					groupingStep = 5;
				}
			}
			return true;
		} else if ("02".equals(timing)) {
			// check the number of therapies received
			if (therapyNeedNumber <= 13) {
				hippsScoringEquation = 3;
				groupingStep = 3;

			} else {
				hippsScoringEquation = 4;

				// grouping step has a different value for therapies
				// betweeen 14 and 19 inclusive
				if (therapyNeedNumber <= 19) {
					groupingStep = 4;
				} else {
					groupingStep = 5;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	protected void determineClinicalSeverity(int equationValue) {
		switch (groupingStep) {
			case 1:
				if (equationValue <= 4) {
					clinicalSeverity = 'A';
				} else if (equationValue <= 8) {
					clinicalSeverity = 'B';
				} else // >= 9
				{
					clinicalSeverity = 'C';
				}
				break;

			case 2:
				if (equationValue <= 6) {
					clinicalSeverity = 'A';
				} else if (equationValue <= 14) {
					clinicalSeverity = 'B';
				} else // >= 15
				{
					clinicalSeverity = 'C';
				}
				break;

			case 3:
				if (equationValue <= 2) {
					clinicalSeverity = 'A';
				} else if (equationValue <= 5) {
					clinicalSeverity = 'B';
				} else // >= 6
				{
					clinicalSeverity = 'C';
				}
				break;

			case 4:
				if (equationValue <= 8) {
					clinicalSeverity = 'A';
				} else if (equationValue <= 16) {
					clinicalSeverity = 'B';
				} else // >= 17
				{
					clinicalSeverity = 'C';
				}
				break;

			case 5:
				if (equationValue <= 7) {
					clinicalSeverity = 'A';
				} else if (equationValue <= 14) {
					clinicalSeverity = 'B';
				} else // >= 15
				{
					clinicalSeverity = 'C';
				}
				break;
		}
	}

	public void determineFunctionalSeverity(int equationValue) {
		//-----------------------------------
		// Functional Domain
		//-----------------------------------
		switch (groupingStep) {
			case 1:
				if (equationValue <= 5) {
					functionalSeverity = 'F';
				} else if (equationValue == 6) {
					functionalSeverity = 'G';
				} else {
					functionalSeverity = 'H';
				}
				break;

			case 2:
				if (equationValue <= 6) {
					functionalSeverity = 'F';
				} else if (equationValue == 7) {
					functionalSeverity = 'G';
				} else {
					functionalSeverity = 'H';
				}
				break;

			case 3:
				if (equationValue <= 8) {
					functionalSeverity = 'F';
				} else if (equationValue == 9) {
					functionalSeverity = 'G';
				} else {
					functionalSeverity = 'H';
				}
				break;

			case 4:
				if (equationValue <= 7) {
					functionalSeverity = 'F';
				} else if (equationValue == 8) {
					functionalSeverity = 'G';
				} else {
					functionalSeverity = 'H';
				}
				break;

			case 5:
				if (equationValue <= 6) {
					functionalSeverity = 'F';
				} else if (equationValue == 7) {
					functionalSeverity = 'G';
				} else {
					functionalSeverity = 'H';
				}
				break;
		}
	}

	protected void determineServiceDomain() {
		switch (groupingStep) {
			case 1:
			case 3:
				if (therapyNeedNumber <= 5) {
					servicesUtilization = 'K';
				} else if (therapyNeedNumber == 6) {
					servicesUtilization = 'L';
				} else if (therapyNeedNumber <= 9) {
					servicesUtilization = 'M';
				} else if (therapyNeedNumber == 10) {
					servicesUtilization = 'N';
				} else if (therapyNeedNumber <= 13) {
					servicesUtilization = 'P';
				} else {
					servicesUtilization = BLANK;
				}
				break;

			case 2:
			case 4:
				if (therapyNeedNumber <= 15) {
					servicesUtilization = 'K';
				} else if (therapyNeedNumber <= 17) {
					servicesUtilization = 'L';
				} else if (therapyNeedNumber <= 19) {
					servicesUtilization = 'M';
				} else {
					servicesUtilization = BLANK;
				}
				break;
				
			case 5:
				servicesUtilization = 'K';
				break;
		}
	}

	protected void determineNrs(int nrsScore) {
		if (nrsScore > -1) {
			if (nrsScore == 0) {
				nonRoutineSupplies = 'S';
			} else if (nrsScore <= 14) {
				nonRoutineSupplies = 'T';
			} else if (nrsScore <= 27) {
				nonRoutineSupplies = 'U';
			} else if (nrsScore <= 48) {
				nonRoutineSupplies = 'V';
			} else if (nrsScore <= 98) {
				nonRoutineSupplies = 'W';
			} else {
				nonRoutineSupplies = 'X';
			}
		}

	}
}
