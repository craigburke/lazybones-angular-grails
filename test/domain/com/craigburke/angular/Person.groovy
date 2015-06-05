package com.craigburke.angular

/**
 * Simple domain object representing a person.
 *
 * @author Graeme Rocher
 */
class Person {

	String firstName
	String lastName

	static constraints = {
		firstName blank: false
		lastName blank: false
	}
	
	String toString() {
		"${firstName} ${lastName}"
	}
}