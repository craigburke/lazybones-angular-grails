package ${group}

import spock.lang.Specification
import spock.lang.Shared

class PagedResourceRestfulControllerSpec extends Specification {

	@Shared TestResourceController restfulController

	def setup() {
		restfulController = new TestResourceController()
	}

	def "can get a correct content range"() {
		expect:
		restfulController.getContentRange(total, offset, max) == result
		
		where:
		total | offset | max || result
		0	  | 0	   | 10	 || "0-0/0"
		10	  | 0	   | 5	 || "1-5/10"
		12	  | 10	   | 5	 || "11-12/12"
	}

}

class TestResource {
	String foo
	String bar
	Date myDate
}

class TestResourceController extends PagedRestfulController {
	TestResourceController() {
		super(TestResource)
	}

}