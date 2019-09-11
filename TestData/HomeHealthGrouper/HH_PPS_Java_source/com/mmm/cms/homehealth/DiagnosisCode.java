/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CodeType_EN;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.EtiologyPairingListIF;
import java.util.List;

/**
 * Diagnosis Code extends the Abstract class to add primary, secondary, etiology
 * pairing and ulcer related information.
 *
 * September 2012 - removed old/commented out code. Added toStringDebug().
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class DiagnosisCode extends AbstractDiagnosisCode implements
		DiagnosisCodeIF, Cloneable {

	/**
	 * Flag identifying code as a secondary or manifestation only code. Default
	 * is false
	 */
	protected boolean secondaryOnly;
	/**
	 * Flag identifying code as the primary code for scoring. Identified at
	 * runtime during the scoring logic
	 */
	protected transient boolean primary;
	/**
	 * Holds a list of Etiology codes used to pair with this code. This should
	 * only be used if the code is flagged as Secondary Only or is a Primary
	 * Awardable related code
	 */
	protected transient EtiologyPairsList etiologyPairCodes;
	/**
	 * Indicates the offset position of the code within the record's Diagnosis
	 * grid. The default is 0
	 */
	protected int offset;
	/**
	 * This flag indicates that this Code allows for the code in position 2 to
	 * be set a primary code for scoring.
	 *
	 * @since V3413
	 */
	protected boolean primaryAwardableCode;

	/**
	 * Constructor for blank code
	 */
	public DiagnosisCode() {
		// defaults to the abstract code which is blank and ICD-9
	}

	/**
	 * Constructor with a code value
	 *
	 * @param code
	 * @param codetype
	 */
	public DiagnosisCode(String code, CodeType_EN codeType) {
		super(code, codeType);
	}
	
	/**
	 * Constructor with a code value
	 *
	 * @param code
	 * @deprecated - in 2014, because it defaults to ICD-9 type
	 */
	public DiagnosisCode(String code) {
		super(code, CodeType_EN.ICD_9);
	}

	/**
	 * Constructor with code value, valid Code indicator, and valid for scoring
	 * indicator
	 *
	 * @param code
	 * @param validCode
	 * @param validForScoring
	 * @deprecated - in 2014, because it defaults to ICD-9 type
	 */
	public DiagnosisCode(String code, boolean validCode, boolean validForScoring) {
		super(code, CodeType_EN.ICD_9, validCode, validForScoring);
	}

	/**
	 * Constructor with code value, valid Code indicator, and valid for scoring
	 * indicator
	 *
	 * @param code
	 * @param codetype
	 * @param validCode
	 * @param validForScoring
	 */
	public DiagnosisCode(String code, CodeType_EN codetype, boolean validCode, boolean validForScoring) {
		super(code,  codetype, validCode, validForScoring);
	}
	
	/**
         * The code is cloned to allow the app to make changes to the code
         * on a per-record basis without effecting the Grouper's base list
         * of codes.
         * 
	 * @return an exact copy of this Diagnosis code.
	 * @throws java.lang.CloneNotSupportedException
	 */
	@Override
	public DiagnosisCodeIF clone() throws CloneNotSupportedException {
		return (DiagnosisCodeIF) super.clone();
	}


	/**
	 * gets a List of valid etiologies that are used in conjuction with the
	 * Secondary only code. Has no meaning if this code is not a Secondary only
	 * code.
	 *
	 * @return a List of Diagnosis codes indicating the related Etiology pairing
	 * - will never be null, but may be empty
	 */
	@Override
	public EtiologyPairingListIF getEtiologyPairCodes() {
		if (etiologyPairCodes == null) {
			etiologyPairCodes = new EtiologyPairsList();
		}

		return etiologyPairCodes;
	}

	/**
	 * Gets the offset of the code within the record's Diagnosis grid
	 *
	 * @return
	 */
	@Override
	public int getOffset() {
		return offset;
	}

	/**
	 * gets the secondary only indicator
	 *
	 * @return true if the code is marked as secondary only
	 */
	@Override
	public boolean isSecondaryOnly() {
		return secondaryOnly;
	}

	/**
	 * gets the primary code indicator
	 *
	 * @return true if the code is marked as primary
	 */
	@Override
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * gets the flag indicating that this V-Code allows the Diabetes, Neuro 1
	 * and Skin 1 primary code in position 2
	 *
	 * Introduced for V3413
	 *
	 * @return
	 */
	@Override
	public boolean isPrimaryAwardableCode() {
		return this.primaryAwardableCode;
	}

	/**
	 * gets the Diabetic Ulcer related indicator
	 *
	 * @return true if the code is related to diabetic ulcers
	 */
	@Override
	public boolean isDiabeticUlcer() {
		final DiagnosticGroupIF group = getDiagnosticGroup();
		return group != null && "Diabetic Ulcers".equals(group.getDescription());
	}

	/**
	 * gets the Ulcer related indicator
	 *
	 * @return true if the code is related to diabetic ulcers
	 */
	@Override
	public boolean isUlcer() {
		final DiagnosticGroupIF group = getDiagnosticGroup();
		return group != null && "Non-pressure and non-stasis ulcers (other than diabetic)".equals(group.getDescription());
	}

	/**
	 * determines if the codes is part of the etiology pair listing
	 *
	 * @param code
	 * @return true if the supplied code is on the Etiology pair list
	 */
	@Override
	public boolean isEtiologyInPairingList(DiagnosisCodeIF code) {
		// check for a pairs listing and an actual code to check against
		return etiologyPairCodes != null && etiologyPairCodes.size() > 0
				&& code != null && !code.isEmpty() &&
				etiologyPairCodes.contains(code);
	}

	/**
	 * determines if the etiology code can be paired with this code
	 *
	 * @param etiologyCode
	 * @return true if the codes can be paired
	 */
	@Override
	public boolean isValidEtiologyPairing(DiagnosisCodeIF etiologyCode) {
		return etiologyPairCodes != null ? etiologyPairCodes.isValidEtiologyPairing(etiologyCode) : false;
	}

	/**
	 * Sets the List of Etiology paired codes
	 *
	 * @param pairs
	 */
	@Override
	public void setEtiologyPairCodes(List<DiagnosisCodeIF> pairs) {
		etiologyPairCodes.addAll(pairs);
	}

	/**
	 * Sets the diabetic ulcer related indicator
	 *
	 * @param diabeticUlcer
	 */
	@Override
	public void setDiabeticUlcer(boolean diabeticUlcer) {
		throw new UnsupportedOperationException("Diabetic Ulcer is determined by the Diagnostic Group.");
	}

	/**
	 * Sets the codes offset in the record's Diagnosis grid. Must be >=0 and
	 * <=17
	 *
	 * @param offset
	 */
	@Override
	public void setOffset(int offset) {
		if (offset >= 0 && offset <= 17) {
			this.offset = offset;
		} else {
			throw new IllegalArgumentException("setOffset() - value must be >= 0 and <= 17. Value is " + offset);
		}
	}

	/**
	 * Sets the primary code indicator
	 *
	 * @param primary
	 */
	@Override
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * if the code is null or the code is not a V-Code, and the parameter value
	 * is true, then this throws an IllegalStateException because the value can
	 * not be set to true unless it is a V-Code.
	 *
	 * Introduced for V3413
	 *
	 * @param primaryAwardableCode
	 * @throws IllegalStateException
	 */
	@Override
	public void setPrimaryAwardableCode(boolean primaryAwardableVCode) throws IllegalStateException {
		this.primaryAwardableCode = primaryAwardableVCode;
	}

	/**
	 * Sets the secondary flag
	 *
	 * @param bool
	 */
	@Override
	public void setSecondaryOnly(boolean bool) {
		secondaryOnly = bool;
	}

	/**
	 * Sets the ulcer related indicator
	 *
	 * @param ulcer
	 */
	@Override
	public void setUlcer(boolean ulcer) {
		throw new UnsupportedOperationException("Ulcer is determined by the Diagnostic Group.");
	}

	@Override
	public String toStringFinest() {
		return "DiagnosisCode{{" + super.toStringFinest()
				+ "}, secondaryOnly=" + secondaryOnly
				+ ", primary=" + primary
				+ ", etiologyPairCodes=" + etiologyPairCodes + '}';
	}
}
