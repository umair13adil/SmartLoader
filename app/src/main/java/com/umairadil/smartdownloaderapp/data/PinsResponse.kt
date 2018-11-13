package com.umairadil.smartdownloaderapp.data

data class PinsResponse(
        var id: String?,
        var width: Int?,
        var height: Int?,
        var color: String?,
        var likes: Int?,
        var user: User?,
        var urls: URLs?
)

data class User(
        var id: String?,
        var name: String?,
        var profile_image: ProfileImage?
)

data class ProfileImage(
        var small: String?,
        var medium: String?,
        var large: String?
)

data class URLs(
        var raw: String?,
        var full: String?,
        var regular: String?,
        var small: String?,
        var thumb: String?
)