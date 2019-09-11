/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.util;

import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import java.util.Comparator;

/**
 * Provides a mechanism to compare the diagnosis Code by values only
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class DiagnosisCodeComparator implements Comparator<DiagnosisCodeIF> {

	/**
	 * Static comparator - avoids recreation everytime it is needed
	 */
	public static final DiagnosisCodeComparator codeComparator;

	static {
		codeComparator = new DiagnosisCodeComparator();
	}

	/**
	 * Compares two DiagnosisCodeIF based on the value of getCode()
	 *
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	@Override
	public int compare(DiagnosisCodeIF arg1, DiagnosisCodeIF arg2) {
		final String str1;
		final String str2;

		str1 = arg1 != null ? arg1.getCode() : null;
		str2 = arg2 != null ? arg2.getCode() : null;

		return (str1 != null) ? str1.compareTo(str2)
				: (str2 != null) ? -1 : 0;
	}
}
