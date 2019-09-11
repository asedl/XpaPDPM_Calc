/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;

/**
 * Holds the validation flags assocaited with a record.  The getDataValidityFlag()
 * does the conversion between the indicators and the alpha numberic representation
 *
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class DataValidityFlag implements DataValidityFlagIF {

    /**
     *
     */
    protected final static String FLAG_VALUES[] = new String[]{ "1", "2", "3", "5", "4", "7", "6", "8", "A", "B", "C", "E", "D", "G", "F", "H" };
    private boolean manifestationSequenceIssue;
    private boolean clinicalIssue;
    private boolean functionalIssue;
    private boolean serviceIssue;

    /**
     * Default constructor
     */
    public DataValidityFlag() {
    }

    /**
     * gets the Clinical Issue flag
     * @return
     */
    @Override
    public boolean isClinicalIssue() {
        return clinicalIssue;
    }

    /**
     * sets the Clinical Issues flag
     * @param clinicalIssue
     */
    @Override
    public void setClinicalIssue(boolean clinicalIssue) {
        this.clinicalIssue = clinicalIssue;
    }

    /**
     * gets the Functional Issues flag
     * @return
     */
    @Override
    public boolean isFunctionalIssue() {
        return functionalIssue;
    }

    /**
     * sets the Functional Issues flag
     * @param functionalIssue
     */
    @Override
    public void setFunctionalIssue(boolean functionalIssue) {
        this.functionalIssue = functionalIssue;
    }

    /**
     * gets the Manifestation Issues flag
     *
     * @return
     */
    @Override
    public boolean isManifestationSequenceIssue() {
        return manifestationSequenceIssue;
    }

    /**
     * sets the Manifestation Issues flag
     * @param manifestationSequenceIssue
     */
    @Override
    public void setManifestationSequenceIssue(boolean manifestationSequenceIssue) {
        this.manifestationSequenceIssue = manifestationSequenceIssue;
    }

    /**
     * gets the Services Issues flag
     * @return
     */
    @Override
    public boolean isServiceIssue() {
        return serviceIssue;
    }

    /**
     * sets the Services Issues flag
     * @param serviceIssue
     */
    @Override
    public void setServiceIssue(boolean serviceIssue) {
        this.serviceIssue = serviceIssue;
    }

    /**
     * Resets the values to false and the flag to 1, allowing for object re-use.
     */
    @Override
    public void reset() {
        manifestationSequenceIssue = false;
        clinicalIssue = false;
        functionalIssue = false;
        serviceIssue = false;
    }

    /**
     * gets the validity flag as a String
     * @return
     */
    @Override
    public String getValidityFlag() {
        int tmpInt = 0;

        if (clinicalIssue) {
            tmpInt++;
        }

        if (functionalIssue) {
            tmpInt += 2;
        }

        if (serviceIssue) {
            tmpInt += 4;
        }

        if (manifestationSequenceIssue) {
            tmpInt += 8;
        }

        return FLAG_VALUES[tmpInt];
    }

    /**
     * Does nothing - you can not set the validity flag as a single string
     * @param val
     */
    @Override
    public void setValidityFlag(String val) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("manifistationIssue=");
        buffer.append(manifestationSequenceIssue);
        buffer.append(",clinicalIssue=");
        buffer.append(clinicalIssue);
        buffer.append(",functionalIssue=");
        buffer.append(functionalIssue);
        buffer.append(",serviceIssue=");
        buffer.append(serviceIssue);
        buffer.append(",validityFlag=");
        buffer.append(getValidityFlag());

        return buffer.toString();
    }

    /**
     * gets the general Issues flag
     * @return
     */
    @Override
    public boolean isIssuePresent() {
            return manifestationSequenceIssue ||
            clinicalIssue ||
            functionalIssue ||
            serviceIssue;
    }


}
