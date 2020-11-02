package com.fmohammadi.instagram.model

class User() {
    var bio: String? = null
    var email: String? = null
    var imageUrl: String? = null
    var name: String? = null
    var uid: String? = null
    var username: String? = null

    constructor(
        bio: String,
        email: String,
        imageUrl: String,
        name: String,
        uid: String,
        username: String
    ) : this() {
        this.bio = bio
        this.email = email
        this.imageUrl = imageUrl
        this.name = name
        this.uid = uid
        this.username = username
    }
}