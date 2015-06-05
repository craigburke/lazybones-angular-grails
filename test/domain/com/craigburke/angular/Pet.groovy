package com.craigburke.angular

/**
 * Simple domain object representing a pet.
 *
 * @author Graeme Rocher
 */
class Pet {

	String name
	Date birthDate
	PetType type
	Owner owner

	static hasMany = [visits: Visit]

	static constraints = {
		name blank: false, validator: { name, pet ->
			if (!pet.id && pet.owner?.pets?.find { it.name == name })  {
				return 'pet.duplicate'
			}
		}
	}
	
	String toString() {
		"${name}"
	}
}