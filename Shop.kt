package com.example.mapa;


class Shop( var name: String, var lat: Double, var lon: Double, var desc: String, var radius: Float){
    var key : String = ""

    constructor( name: String,  lat: Double,  lon: Double, desc: String,radius: Float,key: String) : this(name,lon,lat,desc,radius){
        this.key = key
    }


}

