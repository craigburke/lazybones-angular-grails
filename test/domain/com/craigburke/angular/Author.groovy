package com.craigburke.angular

class Author {
    String firstName
    String lastName

    static constraints = {
        firstName(required: true, blank: false)
        lastName(required: true, blank: false)
    }

	public String toString() {
		"${firstName} ${lastName}"
	}
	
}