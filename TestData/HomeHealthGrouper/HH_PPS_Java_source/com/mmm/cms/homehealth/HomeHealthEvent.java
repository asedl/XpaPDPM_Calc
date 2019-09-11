/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.*;
import java.util.EventObject;

/**
 * The Home Health Event object used to notify Listener objects of what is
 * happening during record scoring and validation
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthEvent extends EventObject implements HomeHealthEventIF {

	private EventId_EN eventId;
	private HomeHealthRecordIF record;
	private String message;
	private HomeHealthScoringModelIF model;
	private Exception exception;

	/**
	 * Constructor with all the required information in one call
	 *
	 * @param eventId
	 * @param source
	 * @param record
	 * @param message
	 * @param model
	 */
	public HomeHealthEvent(EventId_EN eventId, HomeHealthGrouperIF source, HomeHealthRecordIF record,
			String message, HomeHealthScoringModelIF model) {
		super(source);
		this.eventId = eventId;
		this.record = record;
		this.message = message;
		this.model = model;
	}

	/**
	 * Constructor with the basic information of the Home Health Grouper as the
	 * source object
	 *
	 * @param source
	 */
	public HomeHealthEvent(HomeHealthGrouperIF source) {
		super(source);
	}

	/**
	 * gets the message
	 *
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * sets the message
	 *
	 * @param message
	 */
	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * gets the scoring model, if any
	 *
	 * @return the scoring model, can be null
	 */
	@Override
	public HomeHealthScoringModelIF getModel() {
		return model;
	}

	/**
	 * sets the scoring model
	 *
	 * @param model
	 */
	@Override
	public void setModel(HomeHealthScoringModelIF model) {
		this.model = model;
	}

	/**
	 * gets the related Home Health Record, if any
	 *
	 * @return the Home Health record, may be null
	 */
	@Override
	public HomeHealthRecordIF getRecord() {
		return record;
	}

	/**
	 * Sets the Home Health record
	 *
	 * @param record
	 */
	@Override
	public void setRecord(HomeHealthRecordIF record) {
		this.record = record;
	}

	/**
	 * get the event id
	 *
	 * @return the event id
	 */
	@Override
	public EventId_EN getEventId() {
		return eventId;
	}

	/**
	 * Sets the event id
	 *
	 * @param eventId
	 */
	@Override
	public void setEventId(EventId_EN eventId) {
		this.eventId = eventId;
	}

	/**
	 * gets any Exception generated, if any
	 *
	 * @return the Exception, may be null
	 */
	@Override
	public Exception getException() {
		return exception;
	}

	/**
	 * Sets the Exception
	 *
	 * @param exception
	 */
	@Override
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * The Home Health Grouper that cause the event
	 *
	 * @return the Home Health Grouper
	 */
	@Override
	public HomeHealthGrouperIF getGrouper() {
		return (HomeHealthGrouperIF) getSource();
	}

	/**
	 * A formatted String
	 *
	 * @return formatted String
	 */
	@Override
	public String toString() {
		StringBuilder buffer;

		buffer = new StringBuilder(CommonMessageText.EVENT_MSG_HEADER + " Event: (");
		buffer.append(eventId);
		buffer.append(")");

		if (getSource() != null) {
			buffer.append("- Version: ");
			buffer.append(((HomeHealthGrouperIF) getSource()).getVersion());
			buffer.append(" ");
		}

		if (this.model != null) {
			buffer.append(this.model.getName());
			buffer.append(", Eq = ");
			buffer.append(this.model.getId());
			buffer.append(" ");
		}

		switch (eventId) {
			case GENERAL:
				break;

			case WARNING:
				break;

			case EXCEPTION:
				if (exception != null) {
					buffer.append(" - Exception: '");
					buffer.append(exception.toString());
					buffer.append("'");
				}

				break;

			case SCORING_STARTING:
			case SCORING_FINISHED:
			case SCORING_INCREASED:
			case SCORING_SECTION_STARTING:
			case SCORING_SECTION_FINISHED:
			case VALIDATION_ISSUE:
				break;

			default:
		}

		if (message != null) {
			buffer.append("- Message: '");
			buffer.append(message);
			buffer.append("' ");
		}

		return buffer.toString();
	}
}
