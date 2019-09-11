/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;

/**
 * Each valid code for a version belongs to a Diagnosis Group. This represents
 * the Group Id and description. Though the group id is required for scoring,
 * the description is only useful for debugging or GUIs.
 *
 * It also provides a non-changable default Group with ID = 0 for use with codes
 * that don't require a Group but makes programming easier by assigning an
 * "empty" Group
 *
 * October 2012 - added alternate scoring methods for use in versions V3413 and
 * later
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class DiagnosticGroup implements DiagnosticGroupIF {

    /**
     * The default "unknown" Group object. The id is always 0
     */
    public final static DiagnosticGroupIF GROUP_UNKNOWN;

    static {
        // create the default, No operation, non Group object
        GROUP_UNKNOWN = new DiagnosticGroupIF() {

            @Override
            public int getId() {
                return 0;
            }

            @Override
            public String getDescription() {
                return "Unknown Diagnostic Group";
            }

            @Override
            public boolean isAlternatePrimaryScorable() {
                return false;
            }

            @Override
            public void setAlternatePrimaryScorable(boolean alternatePrimaryScorable) {
                // does nothing
            }

            @Override
            public void setDescription(String arg0) {
                // does nothing
            }

            @Override
            public void setId(int arg0) {
                // does nothing
            }

            public int compareTo(Object obj) {
                final int retval;

                if (obj != null && obj instanceof DiagnosticGroup) {
                    final DiagnosticGroup other = (DiagnosticGroup) obj;
                    retval = 0 - other.id;
                } else {
                    retval = 1;
                }
                return retval;
            }

            @Override
            public String toString() {
                return "Diagnostic Group 0";
            }
        };
    }

    /**
     * The Group id
     */
    private int id;

    /**
     * the Group description
     */
    private String description;

    /**
     * Flag introduced in V3413 to identify a Group as participating in
     * promoting or awarding a Dx code in posistion 2 primary scoring points
     */
    private boolean alternatePrimaryScorable;

    /**
     * Constructor with default Id
     *
     * @param id
     */
    public DiagnosticGroup(int id) {
        this.id = id;
    }

    /**
     * Compares the objects based on the id
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }

    /**
     * Compares this group with another based on the ID
     *
     * @param obj
     * @return 0 if equal
     */
    @Override
    public int compareTo(Object obj) {
        final int retval;

        if (obj != null && obj instanceof DiagnosticGroup) {
            final DiagnosticGroup other = (DiagnosticGroup) obj;
            retval = this.id - other.id;
        } else {
            retval = 1;
        }
        return retval;
    }

    /**
     * Get the value of description
     *
     * @return the value of description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * The hash code based on the id
     *
     * @return
     */
    @Override
    public int hashCode() {
        return (int) id;
    }

    /**
     * Gets the alternate primary code scorable flag
     *
     * @return true if this group has alternate scoring for primary and/or other
     * code positions
     */
    @Override
    public boolean isAlternatePrimaryScorable() {
        return this.alternatePrimaryScorable;
    }

    /**
     * Sets the alternate primary code scorable flag
     *
     * @param alternatePrimaryScorable
     */
    @Override
    public void setAlternatePrimaryScorable(boolean alternatePrimaryScorable) {
        this.alternatePrimaryScorable = alternatePrimaryScorable;
    }

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Diagnostic Group " + id;
    }
}
