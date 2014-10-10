package ${group}

import grails.rest.RestfulController
import grails.gorm.PagedResultList

class PagedRestfulController<T> extends RestfulController<T> {

	static responseFormats = ['json']

	PagedRestfulController(Class<T> resource) {
		super(resource)
	}

	def index(Integer page) {
		page = page ?: 1
		params.max = grailsApplication.config.angular.pageSize ?: 25
		params.offset = ((page - 1) * params.max)
		def results = loadPagedResults(params)

		response.setHeader('Content-Range', getContentRange((int)results.totalCount, params.offset, params.max))
		respond results, formats: ['json', 'html']
	}

	protected PagedResultList loadPagedResults(params) {
        resource.createCriteria().list(max: params.max, offset: params.offset) {
            
			params.filter?.each { String name, String value ->
               setDefaultCriteria(delegate, name, value)
            }
         
            if (params.sort) {
              	order(params.sort, params.order == "asc" ? "asc" : "desc")
            }
        }
    }

    private void setDefaultCriteria(criteria, String propertyName, String propertyValue) {
        def field = resource.declaredFields.find { it.name == propertyName }
        if (field && propertyValue) {
        
            switch (field.type) {
                case String:
                    criteria.ilike(propertyName, "%\${propertyValue}%")
                    break
                
                case [Float, Integer, BigDecimal]:
                    if (propertyValue.isNumber()) {
                        criteria.eq(propertyName, propertyValue)
                    }
                    else {
                        criteria.eq(propertyName, null)
                    }
                    break
                
                case Date:
                    def dateFormats = grailsApplication.config.grails.databinding?.dateFormats
                    def dateProperty = params.date("filter.\${propertyName}", dateFormats)
        
                    if (dateProperty) {
                        criteria.ge(propertyName, dateProperty)
                    }
                    else {
                        criteria.eq(propertyName, null)
                    }
                    break
                
                default:
                    criteria.eq(propertyName, propertyValue)
            }
        }
    }

	private String getContentRange(int totalCount, int offset, int max) {
		int startRange = offset + 1;
		int endRange = Math.min(startRange + max, totalCount)

		"\${startRange}-\${endRange}/\${totalCount}"
	}
}
