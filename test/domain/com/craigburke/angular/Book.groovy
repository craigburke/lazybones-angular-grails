package com.craigburke.angular

class Book {

    String title
    Author author
    Date publishDate
    Integer pageCount
    Float price

    static constraints = {
        title(required: true, blank: false)
        author(required: true)
        publishDate(required: true)
        pageCount(required: true)
        price(required: true)
    }

}