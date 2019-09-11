package com.mmm.cms.homehealth.vut.proto;

/**
 * This defines the standard edit type, such as formatting, config, etc
 * 
 * @author GDIT in collaboration with 3M for CMS Home Health
 *
 */
public enum OasisEditTypeEN {

	CONSISTENCY("Consistency"),
	FORMAT("Format"),
	INFORMATION("Information"),
	SKIP("Skip pattern");
	
    private String name;

    private OasisEditTypeEN(String name) {
    	this.name = name;
    }
    
	public String getName() {
		return name;
	}
    
    /**
     * Given a name, returns the OasisEditTypeEN. The comparison is
     * case insensitive.
     * 
     * @param name
     * @return if name is not null, and the name matches one of
     * the OasisEditTypeEN, then the OasisEditTypeEN, otherwise, null
     */
    public static OasisEditTypeEN getType(String name) {
    	OasisEditTypeEN values[] = OasisEditTypeEN.values();

        if (name != null) {
            for (int idx = values.length - 1; idx >= 0; idx--) {
                if (name.equalsIgnoreCase(values[idx].getName())) {
                    return values[idx];
                }
            }
        }
        return null;
    }
    
}
