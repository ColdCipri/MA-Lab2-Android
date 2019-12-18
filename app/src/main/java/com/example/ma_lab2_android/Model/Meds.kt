package com.example.ma_lab2_android.Model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Meds(

    @PrimaryKey
    var id: Int? = 0,
    var name: String? = "name",
    var dataExp: String? = "dataExp",
    var pieces: Int? = 0,
    var baseSubst: String? = "baseSubst",
    var quantityBaseSubst: String? = "quantity",
    var description: String? = "description"
) : RealmObject(){

}