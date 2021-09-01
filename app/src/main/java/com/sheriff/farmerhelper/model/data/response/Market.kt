package com.sheriff.farmerhelper.model.data.response

data class Market(
    var image_url: String,
    var title: String,
    var address: String,
    var redirect_url: String
) {
    constructor()
            : this("", "",  "", "") {
    }
}