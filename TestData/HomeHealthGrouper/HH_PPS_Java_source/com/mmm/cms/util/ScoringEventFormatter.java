/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.util;

import com.mmm.cms.homehealth.HomeHealthEvent;
import com.mmm.cms.homehealth.proto.AlreadyScoredException;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.EventId_EN;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.HomeHealthScoringModelIF;
import java.util.Collection;

/**
 *
 * @author 3M Clinical & Economic Research for CMS Home Health
 */
public class ScoringEventFormatter {

	private static void wrapFieldValue(StringBuilder buffer, String fieldName, String value) {
		buffer.append(fieldName);
		buffer.append(CommonMessageText.WITH_VALUE_SINGLE_QUOTE);
		buffer.append(value);
		buffer.append("'");
	}

	/**
	 * Converts the data to a event and sends the event to any listeners
	 *
	 * @param fieldName
	 * @param value
	 */
	public static void fireClinicalIssueEvent(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String fieldName, String value) {

		if (listeners != null && !listeners.isEmpty()) {
			StringBuilder buffer = new StringBuilder();

			buffer.append("Clinical Issue: field = ");

			wrapFieldValue(buffer, fieldName, value);
			fireIssueEvent(listeners, grouper, scoringModel, buffer.toString());
		}
	}

	/**
	 * Converts the data to a event and sends the event to any listeners
	 *
	 * @param fieldName
	 * @param value
	 */
	public static void fireFunctionalIssueEvent(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String fieldName, String value) {

		if (listeners != null && !listeners.isEmpty()) {
			StringBuilder buffer = new StringBuilder();

			buffer.append("Functional Issue: field = ");

			wrapFieldValue(buffer, fieldName, value);
			fireIssueEvent(listeners, grouper, scoringModel, buffer.toString());
		}
	}

	/**
	 * Converts the data to a event and sends the event to any listeners
	 *
	 * @param fieldName
	 * @param value
	 */
	public static void fireManifestationIssueEvent(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String fieldName, String value) {

		if (listeners != null && !listeners.isEmpty()) {
			StringBuilder buffer = new StringBuilder();

			buffer.append("Manifestation Issue: field = ");

			wrapFieldValue(buffer, fieldName, value);
			fireIssueEvent(listeners, grouper, scoringModel, buffer.toString());
		}
	}

	/**
	 * Converts the data to a event and sends the event to any listeners
	 *
	 * @param message
	 */
	public static void fireIssueEvent(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String message) {
		HomeHealthEvent event;

		event = new HomeHealthEvent(EventId_EN.VALIDATION_ISSUE, grouper, null, message, scoringModel);
		fireScoringEvent(listeners, event);
	}

	/**
	 * Converts the data to a event and sends the event to any listeners
	 *
	 * @param fieldName
	 * @param value
	 */
	public static void fireServiceIssueEvent(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String fieldName, String value) {
		
		if (listeners != null && !listeners.isEmpty()) {
			StringBuilder buffer = new StringBuilder();

			buffer.append("Service Issue: field = ");

			wrapFieldValue(buffer, fieldName, value);
			fireIssueEvent(listeners, grouper, scoringModel, buffer.toString());
		}
	}

	/**
	 * This method, along with the other fire... methods, and a liberal amount
	 * of calls to these methods allows a developer to "watch" the scoring
	 * process in detail. These are not necessary for actual scoring, but
	 * provides an in depth view of the process; helpful for debugging and
	 * understanding the scoring process.
	 *
	 * @see
	 * com.mmm.cms.homehealth.proto.HomeHealthEventNotifierIF#notifyEventListeners(HomeHealthEventIF
	 * event)
	 * @param event
	 */
	public static void fireScoringEvent(Collection<HomeHealthEventListenerIF> listeners, HomeHealthEvent event) {
		if (listeners != null && !listeners.isEmpty()) {
			for (HomeHealthEventListenerIF listener : listeners) {
				listener.homeHealthEvent(event);
			}
		}
	}

        /**
         * Scoring event for primary codes only
         * 
         * @param listeners
         * @param grouper
         * @param scoringModel
         * @param code
         * @param diagnosisPosition
         * @param rowId
         * @param score 
         */
	public static void fireScoringIncreasePrimaryCodeEvent(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			DiagnosisCodeIF code,
			int diagnosisPosition, int rowId, int score) {
		
		if (listeners != null && !listeners.isEmpty()) {
			final DiagnosticGroupIF group;
			final StringBuilder buffer = new StringBuilder();

			buffer.append("Primary Diagnosis code '");
			buffer.append(code.getCode());
			buffer.append("'");
			group = code.getDiagnosticGroup();
			if (group != null && group.getId() > 0) {
				buffer.append(" (");
				buffer.append(group.getDescription());
				buffer.append(")");
			}
			fireScoringIncreaseEvent(listeners, grouper, scoringModel, buffer.toString(), rowId, score);
		}
	}
        
	/**
	 * Scoring Event support method
	 *
	 * @param diagCode
	 * @param diagnosisPosition
	 * @param rowId
	 * @param score
	 */
	public static void fireScoringIncreaseCodeEvent(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			DiagnosisCodeIF code,
			int diagnosisPosition, int rowId, int score) {
		
		if (listeners != null && !listeners.isEmpty()) {
			final DiagnosticGroupIF group;
			final StringBuilder buffer = new StringBuilder();

			buffer.append("Diagnosis code '");
			buffer.append(code.getCode());
			buffer.append("'");
			group = code.getDiagnosticGroup();
			if (group != null && group.getId() > 0) {
				buffer.append(" (");
				buffer.append(group.getDescription());
				buffer.append(")");
			}
			fireScoringIncreaseEvent(listeners, grouper, scoringModel, buffer.toString(), rowId, score);
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @param message
	 * @param rowId
	 * @param score
	 */
	public static void fireScoringIncreaseEvent(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String message, int rowId, int score) {
		if (listeners != null && !listeners.isEmpty()) {
			
			final StringBuilder buffer = new StringBuilder();
			buffer.append(message);
			buffer.append(": adding row ");
			buffer.append(rowId);
			buffer.append(", value = ");
			buffer.append(score);

			fireScoringEvent(listeners, new HomeHealthEvent(EventId_EN.SCORING_INCREASED, grouper,
					null, buffer.toString(), scoringModel));
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @see #fireScoringEvent(HomeHealthEvent)
	 * @param position
	 * @param code
	 */
	public static void fireScoringCodeEligible(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel, int position,
			DiagnosisCodeIF code) {
		
		if (listeners != null && !listeners.isEmpty()) {
			HomeHealthEvent event;
			DiagnosticGroupIF group;
			
			final StringBuilder buffer = new StringBuilder();
			buffer.append("Diagnosis code '");
			buffer.append(code.getCode());
			buffer.append("'");

			group = code.getDiagnosticGroup();
			if (group != null && group.getId() > 0) {
				buffer.append(" (");
				buffer.append(group.getDescription());
				buffer.append(")");
			}

			buffer.append(" at position ");
			buffer.append(position);

			buffer.append(" is valid for scoring");
			if (code.isPrimary()) {
				buffer.append(" as Primary");
			}

			if (code.isSecondaryOnly()) {
				buffer.append(" as Secondary");
			}

			event = new HomeHealthEvent(EventId_EN.GENERAL, grouper, null, buffer.toString(), scoringModel);
			fireScoringEvent(listeners, event);
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @see #fireScoringEvent(HomeHealthEvent)
	 * @param message
	 */
	public static void fireScoringGeneral(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String... messages) {
		
		if (listeners != null && !listeners.isEmpty()) {
			final StringBuilder buffer = new StringBuilder();

			if (messages != null) {
//				if (messages.length == 1) {
//					buffer.append(" ");
//					buffer.append(messages[0]);
//				} else {
					for (int idx = 0; idx < messages.length; idx++) {
//						buffer.append(" ");
						buffer.append(messages[idx]);
					}
//				}
			}

			fireScoringEvent(listeners, new HomeHealthEvent(EventId_EN.GENERAL,
					grouper, null, buffer.toString(), scoringModel));
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @see #fireScoringEvent(HomeHealthEvent)
	 * @param message
	 */
	public static void fireScoringWarning(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String message) {

		if (listeners != null && !listeners.isEmpty()) {
			final StringBuilder buffer = new StringBuilder();
			buffer.append(message);

			fireScoringEvent(listeners, new HomeHealthEvent(EventId_EN.WARNING, grouper,
					null, buffer.toString(), scoringModel));
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @see #fireScoringEvent(HomeHealthEvent)
	 * @param message
	 */
	public static void fireScoringSectionStart(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String message) {

		if (listeners != null && !listeners.isEmpty()) {
			final StringBuilder buffer = new StringBuilder();
			buffer.append(message);
			buffer.append(": Starting... ");

			fireScoringEvent(listeners, new HomeHealthEvent(EventId_EN.SCORING_SECTION_STARTING, grouper,
					null, buffer.toString(), scoringModel));
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @see #fireScoringEvent(HomeHealthEvent)
	 * @param message
	 * @param score
	 */
	public static void fireScoringSectionFinished(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			String message, int score) {
		if (listeners != null && !listeners.isEmpty()) {
			final StringBuilder buffer = new StringBuilder();
			buffer.append(message);
			buffer.append(": Finished: score ");
			buffer.append(score);

			fireScoringEvent(listeners, new HomeHealthEvent(EventId_EN.SCORING_SECTION_FINISHED, grouper,
					null, buffer.toString(), scoringModel));
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @see #fireScoringEvent(HomeHealthEvent)
	 * @param message
	 * @param scores
	 */
	public static void fireScoringSectionFinished(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel, String message, int scores[]) {

		if (listeners != null && !listeners.isEmpty()) {
			HomeHealthEvent event;
			final StringBuilder buffer = new StringBuilder();
			buffer.append(message);
			buffer.append(": Finished");
			if (scores != null) {
				buffer.append(": score ");
				buffer.append(HomeHealthUtils.sumIntArray(scores));
			}

			event = new HomeHealthEvent(EventId_EN.SCORING_SECTION_FINISHED, grouper, null, buffer.toString(), scoringModel);
			fireScoringEvent(listeners, event);
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @see #fireScoringEvent(HomeHealthEvent)
	 * @param code
	 * @param position
	 * @param isValid
	 */
	public static void fireValidCodeWarning(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			DiagnosisCodeIF code, int position,
			boolean isValid) {
		
		if (listeners != null && !listeners.isEmpty() && !code.isEmpty()) {
			DiagnosticGroupIF group;
			final StringBuilder buffer = new StringBuilder();

			buffer.append("Diagnosis code '");
			buffer.append(code.getCode());
			buffer.append("'");

			group = code.getDiagnosticGroup();
			if (group != null && group.getId() > 0) {
				buffer.append(" (");
				buffer.append(group.getDescription());
				buffer.append(")");
			}

			buffer.append(" at position ");
                        if (position == 1) {
                            buffer.append("PRIMARY_DIAG_ICD");
                        } else if (position <= 6) {
                            buffer.append("OTH_DIAG");
                            buffer.append(position - 1);
                            buffer.append("_ICD");
                        } else if (position <= 12) {
                            buffer.append("DIAG_ICD_");
                            buffer.append((char) ('A' + (position - 7)));
                            buffer.append("3");
                        } else {
                            buffer.append("DIAG_ICD_");
                            buffer.append((char) ('A' + (position - 13)));
                            buffer.append("4");
                        }

			if (isValid) {
				buffer.append(" is valid code BUT NOT valid for scoring.");
			} else {
				buffer.append(" is NOT a valid code.");
			}

			fireScoringWarning(listeners, grouper, scoringModel, buffer.toString());
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @param code
	 * @param position
	 * @param e
	 */
	public static void fireDxGroupAlreadyScored(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper, HomeHealthScoringModelIF scoringModel,
			DiagnosisCodeIF code, int position,
			AlreadyScoredException e) {

		if (listeners != null && !listeners.isEmpty()) {
			final StringBuilder buffer = new StringBuilder();

			buffer.append(" Diagnosis code '");
			buffer.append(code.getCode());
			buffer.append("' at position ");
			buffer.append(position);
			buffer.append(" - (");
			buffer.append(code.getDiagnosticGroup().getDescription());
			buffer.append(") already scored due to: ");
			if (e.isDiagnosticGroupAlreadyScored()) {
				buffer.append("Diagnostic Group already scored.");
			}
			if (e.isRowAlreadyScored()) {
				buffer.append("Case Mix Row already scored.");
			}

			fireScoringWarning(listeners, grouper, scoringModel, buffer.toString());
		}
	}

	/**
	 * Scoring Event support method
	 *
	 * @param message
	 * @param record
	 */
	public static void fireScoringRecord(Collection<HomeHealthEventListenerIF> listeners,
			HomeHealthGrouperIF grouper,
			String message, HomeHealthRecordIF record) {

		if (listeners != null && !listeners.isEmpty()) {
			final StringBuilder buffer = new StringBuilder(2000);
			
			buffer.append(CommonMessageText.EVENT_MSG_HEADER);
			buffer.append(message);
			buffer.append("- Record: ");
			buffer.append(record.toString());

			final HomeHealthEvent event = new HomeHealthEvent(EventId_EN.GENERAL, grouper,record, buffer.toString(), null);
			fireScoringEvent(listeners, event);
		}
	}
}
