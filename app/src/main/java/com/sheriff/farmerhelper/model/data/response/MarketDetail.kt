package com.sheriff.farmerhelper.model.data.response

data class MarketDetail(
    var image_url: String,
    var name: String,
    var price: String,
) {
    constructor()
            : this("", "", "") {
    }
}