package com.craigburke.angular

/**
 * Simple domain object representing a visit.
 *
 * @author Graeme Rocher
 */
class Visit {

	Date date = new Date()
	String description
	Pet pet

	static constraints = {
		description blank:false
	}
	
	String toString() {
		"${pet} on ${date}"
	}
}