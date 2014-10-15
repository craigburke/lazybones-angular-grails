package ${group}

import ${group}.pages.${moduleName}.*
import geb.spock.GebReportingSpec
<% 
	def getTestValueFromProperty = { property -> 
		switch(property.type) {
			case [Integer, Float, BigDecimal]:
				"99"
				break
			case Date:
				"\"01/01/2001\""
				break
			case String:
				"\"Foo\""
				break
			default:
				"${property.name}Field.find('option').value()"
		}	
	} 
%>
class ${resourceName}FunctionalSpec extends GebReportingSpec {

	def "should be able to view list page"() {
		when:
		to ${resourceName}ListPage

		then:
		at ${resourceName}ListPage
	}
	
	def "should be able to create a valid ${resourceName}"() {
		when:
		to ${resourceName}ListPage

		and:
		createButton.click()

		then:
		at ${resourceName}CreatePage

		when:
		<%= domainProperties.collect{ "${it.name}Field = ${getTestValueFromProperty(it)}" }.join("${NEWLINE}${TAB*2}") %>
			
		and:
		saveButton.click()

		then:
		at ${resourceName}ShowPage

		and:
		successMessage.displayed

		and:
		successMessage.text().contains "${resourceName} was saved"
	}
	
	def "should be able to delete the first ${resourceName}"() {
		when:
		to ${resourceName}ListPage

		and:
		rows.first().deleteButton.click()

		then:
		at ${resourceName}ListPage

		and:
		successMessage.displayed

		and:
		successMessage.text().contains "${resourceName} was successfully deleted"
      }
	
}