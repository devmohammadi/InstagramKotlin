package com.fmohammadi.instagram.model

class Post() {
    var postid: String? = null
    var imageurl: String? = null
    var description: String? = null
    var publisher: String? = null

    constructor(
        postid: String,
        imageurl: String,
        description: String,
        publisher: String
    ) : this() {
        this.postid = postid
        this.imageurl = imageurl
        this.description = description
        this.publisher = publisher
    }
}
