package com.mmm.cms.homehealth.vut.proto;

import java.io.Serializable;
import java.util.logging.Level;

/**
 * Defines the edit types defined a enums, but allows extension of the edit
 * by add on 3rd party libs without being tied to enum issues
 * 
 * @author GDIT in collaboration with 3M for CMS Home Health
 *
 */
public interface OasisEditIF extends Serializable {
	
    /**
     * Gets the id - this is different from the ordinal value
     *
     * @return The status's id, which may or may not be equivalent to the ordinal
     * value
     */
    public long getId();

    /**
     * Gets the description
     *
     * @return The status's description - this can not be set externally
     */
    public String getDescription();

    /**
     * gets the edit type
     * 
     * @return non-null String
     */
    public OasisEditTypeEN getType();

    /**
     * Gets the severity level of the edit, which uses Logging Level values
     * @return
     */
	public Level getServerityLevel();
	
	/**
	 * Gets the OASIS (or system) version identifier that the edit is valid until 
	 * @return
	 */
	public String getVersionActiveTo();

	

}
