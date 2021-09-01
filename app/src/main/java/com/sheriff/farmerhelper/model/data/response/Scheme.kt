package com.sheriff.farmerhelper.model.data.response

data class Scheme(
    var date_time: String,
    var description: String,
    var image_url: String,
    var title: String
) {
    constructor()
            : this("", "",  "", "") {
    }
}