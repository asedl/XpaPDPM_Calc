/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.proto.edits;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C2_IF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditTypeEN;
import java.util.logging.Level;

/**
 * Defines edits used internally by the HH-PPS for validating OASIS-C2 records
 * an a manner consistent with the VUT (another CMS contract).
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public enum HH_PPS_OasisC2EditsEN implements OasisEditIF {

    EDIT_70000(70000, Level.SEVERE, OasisEditTypeEN.CONSISTENCY, HomeHealthRecord_C2_IF.VALUE_SPEC_VRSN_CD, "Invalid Diagnosis Code."),
    EDIT_70010(70010, Level.SEVERE, OasisEditTypeEN.CONSISTENCY, HomeHealthRecord_C2_IF.VALUE_SPEC_VRSN_CD, "Unacceptable Primary Diagnosis code."),
    EDIT_70020(70020, Level.SEVERE, OasisEditTypeEN.CONSISTENCY, HomeHealthRecord_C2_IF.VALUE_SPEC_VRSN_CD, "Unacceptable Primary Diagnosis code due to Manifestation as Primary Diagnosis."),
    EDIT_70030(70030, Level.WARNING, OasisEditTypeEN.CONSISTENCY, HomeHealthRecord_C2_IF.VALUE_SPEC_VRSN_CD, "Manifestation code without valid Etiology pairing."),
    /*
     * new for v4115
    */
    EDIT_70040(70040, Level.WARNING, OasisEditTypeEN.CONSISTENCY, HomeHealthRecord_C2_IF.VALUE_SPEC_VRSN_CD, "Missing optional payment pairing."),
    EDIT_70050(70050, Level.WARNING, OasisEditTypeEN.CONSISTENCY, HomeHealthRecord_C2_IF.VALUE_SPEC_VRSN_CD, "Not valid manifestation code.");

    
    final private int id;
    final private String description;
    final private OasisEditTypeEN type;
    final private Level serverityLevel;
    final private String versionActiveTo;

    private HH_PPS_OasisC2EditsEN(int id, Level serverityLevel, OasisEditTypeEN type, String versionActiveTo, String description) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.serverityLevel = serverityLevel;
        this.versionActiveTo = versionActiveTo;
    }

    /**
     * Gets the id - this is different from the ordinal value
     *
     * @return The status's id, which may or may not be equivalent to the
     * ordinal value
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * Gets the description
     *
     * @return The status's description - this can not be set externally
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * gets the edit type
     *
     * @return non-null String
     */
    @Override
    public OasisEditTypeEN getType() {
        return type;
    }

    @Override
    public Level getServerityLevel() {
        return serverityLevel;
    }

    @Override
    public String getVersionActiveTo() {
        return versionActiveTo;
    }

    @Override
    public String toString() {
        return id + ", " + description + ", " + type.getName() + ", " + serverityLevel.getName();
    }

    /**
     * Given a description, returns the OasisValiationErrorEN. The comparison is
     * case insensitive.
     *
     * @param description
     * @return if description is not null, and the description matches one of
     * the OasisValiationErrorEN, then the OasisValiationErrorEN, otherwise,
     * null
     */
    public static HH_PPS_OasisC2EditsEN getType(String description) {
        final HH_PPS_OasisC2EditsEN values[] = HH_PPS_OasisC2EditsEN.values();
        HH_PPS_OasisC2EditsEN value = null;

        if (description != null) {
            for (int idx = values.length - 1; idx >= 0; idx--) {
                if (description.equalsIgnoreCase(values[idx].getDescription())) {
                    value = values[idx];
                    break;
                }
            }
        }
        return value;
    }

    /**
     * Given an ID, returns the OasisValiationErrorEN.
     *
     * @param id
     * @return if id matches one of the OasisValiationErrorEN, then the
     * OasisValiationErrorEN, otherwise, null
     */
    public static HH_PPS_OasisC2EditsEN getType(int id) {
        final HH_PPS_OasisC2EditsEN values[] = HH_PPS_OasisC2EditsEN.values();
        HH_PPS_OasisC2EditsEN value = null;

        for (int idx = values.length - 1; idx >= 0; idx--) {
            if (values[idx].getId() == id) {
                value = values[idx];
                break;
            }
        }
        return value;
    }
}
