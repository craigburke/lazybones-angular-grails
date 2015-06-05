package com.craigburke.angular

/**
 * @author Graeme Rocher
 */
class PetType {
	
	String name
	
	static constraints = {
		name blank: false, minSize: 3, maxSize: 20, unique: true
	}
	
	String toString() {
		"${name}"
	}
}