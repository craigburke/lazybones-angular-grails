package ${group}

import ${group}.pages.${moduleName}.*
import geb.spock.GebReportingSpec
import grails.test.mixin.integration.Integration

<% 
	def getTestValueFromProperty = { property, pageElementName, initialValue -> 
		switch(property.type) {
			case [Integer, Float, BigDecimal]:
				initialValue ? "99" : "100"
				break
			case Date:
				initialValue ? "\"01/01/2001\"" :  "\"02/01/2001\""
				break
			case String:
				initialValue ? "\"Foo\"" : "\"Foo!\""
				break
			default:
				"${pageElementName}.find('option').value()"
		}	
	} 
%>
@Integration
class ${domainClassName}FunctionalSpec extends GebReportingSpec {

	def "should be able to view list page"() {
		when:
		to ${domainClassName}ListPage

		then:
		at ${domainClassName}ListPage
	}
	
	def "should be able to create a valid ${domainClassName}"() {
		when:
		to ${domainClassName}ListPage

		and:
		createButton.click()

		then:
		at ${domainClassName}CreatePage

		when:
<%= domainProperties.collect{ "\t\t${it.name}Field = ${getTestValueFromProperty(it, it.name + 'Field', true)}" }.join('\n') %>
			
		and:
		saveButton.click()

		then:
		at ${domainClassName}ShowPage

		and:
		successMessage.displayed

		and:
		successMessage.text().contains "${domainClassName} was successfully created"
	}
	
	def "should be able to sort the ${domainClassName} List"() {
		given:
		to ${domainClassName}ListPage
<%= domainProperties.take(4).collect{ ["\n\t\twhen:", "${it.name}Sort.click()", '', 'then:', "${it.name}Sort.classes().contains(\"asc\")"].join("\n\t\t")}.join('\n') %>
	
	}
	
	def "should be able to filter the ${domainClassName} List"() {
		given:
		to ${domainClassName}ListPage
<%= domainProperties.take(4).collect{ ["\n\t\twhen:", "${it.name}Filter = ${getTestValueFromProperty(it, it.name + 'Filter', true)}", '', 'then:', "waitFor { rows.size() > 0 }"].join("\n\t\t")}.join('\n') %>
	
	}
	
	def "should be able to edit the first ${domainClassName}"() {
		when:
		to ${domainClassName}ListPage

		and:
		rows.first().editButton.click()

		then:
		at ${domainClassName}EditPage
		
		when:
<%= domainProperties.collect{ "\t\t${it.name}Field = ${getTestValueFromProperty(it, it.name + 'Field', false)}" }.join('\n') %>
		
		and:
		saveButton.click()
		
		then:
		at ${domainClassName}ShowPage

		and:
		successMessage.displayed

		and:
		successMessage.text().contains "${domainClassName} was successfully updated"
	}
	
	def "should be able to delete the first ${domainClassName}"() {
		when:
		to ${domainClassName}ListPage

		and:
		rows.first().deleteButton.click()

		then:
		at ${domainClassName}ListPage

		and:
		successMessage.displayed

		and:
		successMessage.text().contains "${domainClassName} was successfully deleted"
      }
	
}