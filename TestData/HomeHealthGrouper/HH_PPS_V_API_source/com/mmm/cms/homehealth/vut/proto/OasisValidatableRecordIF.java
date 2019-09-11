package com.mmm.cms.homehealth.vut.proto;

import java.io.Serializable;
import java.util.Map;

/**
 * This defines a collection of OasisDataItemIF objects that represent
 * an OASIS-C record field values.  The iterator does not guarantee any 
 * particular order of the fields.
 * 
 * It is not necessary to include all the OASIS-C1 fields within this record
 * if the client application is not concerned with validating them.
 *
 * Specific notes on each Map methods:
 * 
 * Note that all "keys" are Strings of the OASIS-C1 data element name which includes the "M..." number. So, the Keys
 * "M0090_INFO_COMPLETED_DT" and "INFO_COMPLETED_DT" are different keys.  In certain implementations, they may both work
 * to get the same value, but it can not be guaranteed.  These keys will be referred to as OASIS Data Key. 
 * 
 * public int size() - this should be the number of data elements that this Record is concerned with. For example, if the
 * Record comes from the HH-PPS, this number may be less than the actual number of OASIS-C1 elements defined in the spec
 * 
 * public boolean isEmpty() - this should always be "return size() == 0;
 * 
 * public boolean containsKey(Object key) - returns true if the OASIS Data Key exists in the record.
 * 
 * public boolean containsValue(Object value) - returns true if the String value exists in the record. Be aware that String values
 * in the OASIS record my duplicate and therefore this may be misleading.  If an OasisDataItemIF is supplied as the parameter and 
 * only the value of the OasisDataItemIF is filled, then this should return the same as using a String value as a parameter.
 * However, if an OasisDataItemIF is supplied as the parameter and the key is filled, then this should return the same as
 * containtsKey().
 * 
 * public OasisDataItemIF get(Object key) - returns the OasisDataIF based on the OASIS Data Key. Should return null if the key 
 * does not exist.
 * 
 * public OasisDataItemIF put(String key, OasisDataItemIF value) - adds an OasisDataItemIF using the OASIS Data Key to organized it.
 * OasisDataItemIF can not be null. The Map should not store any duplicate key entries.  If the record is not modifiable, this method is 
 * not guaranteed to do anything and could return null.
 * 
 * 
 * public OasisDataItemIF remove(Object key) - removes the OASIS Data Key and its associated value only if allowed by the underlying
 * implementation. If the record is not modifiable, this should gracefully ignore this request and return null.
 *  
 * public void putAll(Map<? extends String, ? extends OasisDataItemIF> m) - adds a Map of OasisDataItemIF items.
 * OasisDataItemIF can not be null. The Map should not store any duplicate key entries.  If the record is not modifiable, this method is 
 * not guaranteed to do anything.
 * 
 * public void clear() - removes all the keys and associated values. If the record is not modifiable, this should do nothing.
 * 
 * public Set<String> keySet() - gets all the OASIS Data Keys for the record.
 * 
 * public Collection<OasisDataItemIF> values() - returns a collection of OasisDataItemIF elements.  This should not be null if the
 * size() is greater than 0
 * 
 * public Set<Entry<String, OasisDataItemIF>> entrySet() - gets a Set of Key/Value pairs which is a Map.Entry with a String as key
 * and OasisDataItemIF as value.
 * 
 * 
 * @author GDIT in collaboration with 3M for CMS Home Health
 *
 */

public interface OasisValidatableRecordIF extends Map<String, OasisDataItemIF>, Serializable {

	/**
	 * returns the modification flag for the record. If the record can not be 
	 * modified, then the following methods should do nothing: put(), putAll(), remove() and clear()
	 * 
	 * 
	 * @return true if the keys or values in the record can be modified
	 */
	boolean isModifiable();
	
}
