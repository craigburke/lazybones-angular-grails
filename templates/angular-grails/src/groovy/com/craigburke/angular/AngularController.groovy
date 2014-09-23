package com.craigburke.angular

import grails.rest.RestfulController

class AngularController<T> extends RestfulController<T> {

    static responseFormats = ['json']

    AngularController(Class<T> resource) {
        super(resource)
    }

    def index(Integer page) {
        page = page ?: 1
        params.max = grailsApplication.config.angular.pageSize ?: 25
        params.offset = ((page - 1) * params.int('max'))
        response.setHeader('Content-Range', getContentRange(params.int('offset'), params.int('max')))

        respond listAllResources(params), [excludes: 'class']
    }

    private String getContentRange(int offset, int max) {
        int totalCount = countResources()
        int startRange = offset + 1;
        int endRange = Math.min(startRange + max, totalCount)

        "${startRange}-${endRange}/${totalCount}"
    }
}
