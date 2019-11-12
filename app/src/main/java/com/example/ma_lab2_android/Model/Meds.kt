package com.example.ma_lab2_android.Model

class Meds{
    var id: Int = 0
    var name: String? = null
    var dataExp: String? = null
    var pieces: Int? = 0
    var baseSubst: String? = null
    var quantityBaseSubst: String? = null
    var description: String? = null


    constructor(){}

    constructor(id: Int, name: String, dataexp: String, pieces: Int, baseSusbt: String, quantityBaseSubst: String, descrption: String)
    {
        this.id = id
        this.name = name
        this.dataExp = dataexp
        this.pieces = pieces
        this.baseSubst = baseSusbt
        this.quantityBaseSubst = quantityBaseSubst
        this.description = descrption
    }

}