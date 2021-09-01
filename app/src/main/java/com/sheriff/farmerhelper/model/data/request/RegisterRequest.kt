package com.sheriff.farmerhelper.model.data.request

data class RegisterRequest(
    var name: String,
    var phone: String,
    var email: String,
    var password: String,
    var address: String,
){
    constructor(): this("","","","","")
}