package ${group}

import grails.rest.RestfulController

class PagedRestfulController<T> extends RestfulController<T> {

    static responseFormats = ['json']

    PagedRestfulController(Class<T> resource) {
        super(resource)
    }

    def index(Integer page) {
        page = page ?: 1
        int max = grailsApplication.config.angular.pageSize ?: 25
		int offset = ((page - 1) * max)
        def results = loadPagedResults([max: max, offset: offset, sort: params.sort], params.filter)

        response.setHeader('Content-Range', getContentRange((int)results.totalCount, offset, max))
        respond results, formats: ['json', 'html']
    }

    protected def loadPagedResults(def params, def filter) {
        resource.createCriteria().list(max: params.max, offset: params.offset) {
            filter?.each { key, value ->
                ilike(key, "\${value}%")
            }
            if (params.sort) {
                order(params.sort)
            }
        }
    }

    private String getContentRange(int totalCount, int offset, int max) {
        int startRange = offset + 1;
        int endRange = Math.min(startRange + max, totalCount)

        "\${startRange}-\${endRange}/\${totalCount}"
    }
}
