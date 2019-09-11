/*
 *  This is an unpublished work containing 3M confidential and
 *  proprietary information. Disclosure or reproduction without the
 *  written  authorization of 3M is prohibited.  If publication occurs,
 *  the following notice applies:
 *
 *  Copyright (C) 1998-2003, 3M All rights reserved.
 *
 */

package com.mmm.cms.homehealth.proto;

/**
 * This exception will not contain any stack trace.
 *
 * @author  3M HIS, Tim Gallagher
 */
public class AlreadyScoredException extends Exception {

    int diagnosticGroupId;
    int scoreRowId;
    DiagnosisCodeIF code;

    public AlreadyScoredException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyScoredException(String message) {
        super(message);
    }

    public AlreadyScoredException() {
    }

    public AlreadyScoredException(String message, int diagnosticGroupId,
            int scoreRowId, DiagnosisCodeIF code) {
        super(message);
        this.diagnosticGroupId = diagnosticGroupId;
        this.scoreRowId = scoreRowId;
        this.code = code;
    }

    public AlreadyScoredException(int diagnosticGroupId, int scoreRowId,
            DiagnosisCodeIF code) {
        this.diagnosticGroupId = diagnosticGroupId;
        this.scoreRowId = scoreRowId;
        this.code = code;
    }

    public DiagnosisCodeIF getCode() {
        return code;
    }

    public int getDiagnosticGroupId() {
        return diagnosticGroupId;
    }

    public int getScoreRowId() {
        return scoreRowId;
    }

    public boolean isDiagnosticGroupAlreadyScored() {
        return diagnosticGroupId > 0;
    }

    public boolean isRowAlreadyScored() {
        return scoreRowId > 0;
    }

}
