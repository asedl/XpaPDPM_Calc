/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth.hipps;

import com.mmm.cms.homehealth.HIPPSThreshold;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HIPPSCode_2015 extends HIPPSCode {

	public final static List<HIPPSThreshold> HIPPS_THRESHOLDS_CLINICAL;
	public final static List<HIPPSThreshold> HIPPS_THRESHOLDS_FUNCTIONAL;
	
	static {
		/*
		 * this section sets up the Clinical and Functional limits for 
		 * determining the clinical and functional values
		*/
		
		HIPPSThreshold threshold;
		/*
		 * define the clinical thresdholds
		*/
		HIPPS_THRESHOLDS_CLINICAL = new ArrayList<HIPPSThreshold>();
		// timing = UK
		threshold = new HIPPSThreshold("UK", 13, 1, 1);
		threshold.addSeverityLimit(0, 1, 'A');
		threshold.addSeverityLimit(2, 3, 'B');
		threshold.addSeverityLimit(4, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		threshold = new HIPPSThreshold("UK", 19, 2, 2);
		threshold.addSeverityLimit(0, 1, 'A');
		threshold.addSeverityLimit(2, 7, 'B');
		threshold.addSeverityLimit(8, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		threshold = new HIPPSThreshold("UK", 1000, 5, 2);
		threshold.addSeverityLimit(0, 3, 'A');
		threshold.addSeverityLimit(4, 16, 'B');
		threshold.addSeverityLimit(17, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		
		// timimg = 01
		threshold = new HIPPSThreshold("01", 13, 1, 1);
		threshold.addSeverityLimit(0, 1, 'A');
		threshold.addSeverityLimit(2, 3, 'B');
		threshold.addSeverityLimit(4, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		threshold = new HIPPSThreshold("01", 19, 2, 2);
		threshold.addSeverityLimit(0, 1, 'A');
		threshold.addSeverityLimit(2, 7, 'B');
		threshold.addSeverityLimit(8, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		threshold = new HIPPSThreshold("01", 1000, 5, 2);
		threshold.addSeverityLimit(0, 3, 'A');
		threshold.addSeverityLimit(4, 16, 'B');
		threshold.addSeverityLimit(17, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		
		// timimg = 02
		threshold = new HIPPSThreshold("02", 13, 3, 3);
		threshold.addSeverityLimit(0, 0, 'A');
		threshold.addSeverityLimit(1, 1, 'B');
		threshold.addSeverityLimit(2, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		threshold = new HIPPSThreshold("02", 19, 4, 4);
		threshold.addSeverityLimit(0, 5, 'A');
		threshold.addSeverityLimit(6, 12, 'B');
		threshold.addSeverityLimit(13, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		threshold = new HIPPSThreshold("02", 1000, 5, 4);
		threshold.addSeverityLimit(0, 3, 'A');
		threshold.addSeverityLimit(4, 16, 'B');
		threshold.addSeverityLimit(17, 1000, 'C');
		HIPPS_THRESHOLDS_CLINICAL.add(threshold);
		
		
		/*
		 * define the functional thresdholds
		*/
		HIPPS_THRESHOLDS_FUNCTIONAL = new ArrayList<HIPPSThreshold>();
		// timing = UK
		threshold = new HIPPSThreshold("UK", 13, 1, 1);
		threshold.addSeverityLimit(0, 14, 'F');
		threshold.addSeverityLimit(15, 15, 'G');
		threshold.addSeverityLimit(16, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);
		threshold = new HIPPSThreshold("UK", 19, 2, 2);
		threshold.addSeverityLimit(0, 3, 'F');
		threshold.addSeverityLimit(4, 13, 'G');
		threshold.addSeverityLimit(14, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);
		threshold = new HIPPSThreshold("UK", 1000, 5, 2);
		threshold.addSeverityLimit(0, 2, 'F');
		threshold.addSeverityLimit(3, 5, 'G');
		threshold.addSeverityLimit(6, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);
		
		// timimg = 01
		threshold = new HIPPSThreshold("01", 13, 1, 1);
		threshold.addSeverityLimit(0, 14, 'F');
		threshold.addSeverityLimit(15, 15, 'G');
		threshold.addSeverityLimit(16, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);
		threshold = new HIPPSThreshold("01", 19, 2, 2);
		threshold.addSeverityLimit(0, 3, 'F');
		threshold.addSeverityLimit(4, 13, 'G');
		threshold.addSeverityLimit(14, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);
		threshold = new HIPPSThreshold("01", 1000, 5, 2);
		threshold.addSeverityLimit(0, 2, 'F');
		threshold.addSeverityLimit(3, 5, 'G');
		threshold.addSeverityLimit(6, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);
		
		// timimg = 02
		threshold = new HIPPSThreshold("02", 13, 3, 3);
		threshold.addSeverityLimit(0, 9, 'F');
		threshold.addSeverityLimit(10, 10, 'G');
		threshold.addSeverityLimit(11, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);
		threshold = new HIPPSThreshold("02", 19, 4, 4);
		threshold.addSeverityLimit(0, 0, 'F');
		threshold.addSeverityLimit(1, 7, 'G');
		threshold.addSeverityLimit(8, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);
		threshold = new HIPPSThreshold("02", 1000, 5, 4);
		threshold.addSeverityLimit(0, 2, 'F');
		threshold.addSeverityLimit(3, 5, 'G');
		threshold.addSeverityLimit(6, 1000, 'H');
		HIPPS_THRESHOLDS_FUNCTIONAL.add(threshold);		
		
	}
	
	public HIPPSCode_2015() {
	}

	public HIPPSCode_2015(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, PointsScoringEquationsIF clinicalScore, PointsScoringEquationsIF functionalScore, int nrsScore) {
		super(record, validator, clinicalScore, functionalScore, nrsScore);
	}

	@Override
	protected void evaluateRecord(HomeHealthRecordIF record,
			PointsScoringEquationsIF clinicalScore,
			PointsScoringEquationsIF functionalScore,
			int nrsScore) {

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

	@Override
	protected void determineClinicalSeverity(int equationValue) {
		for (HIPPSThreshold threshold : HIPPS_THRESHOLDS_CLINICAL) {
			if (threshold.getTimingValue().equals(super.timing) && super.therapyNeedNumber <= threshold.getVisitsUpperLimit()) {
				super.clinicalSeverity = threshold.getHippsSeverityValue(equationValue);
				break;
			}
		}
	}
	
	@Override
	public void determineFunctionalSeverity(int equationValue) {
		for (HIPPSThreshold threshold : HIPPS_THRESHOLDS_FUNCTIONAL) {
			if (threshold.getTimingValue().equals(super.timing) && super.therapyNeedNumber <= threshold.getVisitsUpperLimit()) {
				super.functionalSeverity = threshold.getHippsSeverityValue(equationValue);
				break;
			}
		}
	}

	
	
}
