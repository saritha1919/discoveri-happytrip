package com.happytrip.model;

import java.io.Serializable;

/**
 * The persistent class for the classes database table.
 * 
 */
public class FlightClass implements Serializable {
	private static final long serialVersionUID = 1L;

	private long classId;

	private String classType;

	public FlightClass() {
    }

	public FlightClass(long classid) {
		this.classId = classid;
    }

	public long getClassId() {
		return this.classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getClassType() {
		return this.classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}
	
	/* code modified for Collections-bug14-3
	 */
	//modified code starts here for Collections-bug14-3
	
	public boolean equals(Object obj) {
		FlightClass flightClass=(FlightClass)obj;
		if(getClassType().equalsIgnoreCase(flightClass.getClassType())){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
	
		return getClassType().toUpperCase().hashCode();
	}
	//modified code ends here for Collections-bug14-3
}