package com.example.mapa




   class Produkt(var name: String, var cost: Double, var quantity: Int, var buy: Boolean){

       var key : String = ""
       constructor(name: String, cost: Double, quantity: Int, buy: Boolean, key: String) : this(name,cost,quantity,buy){
           this.key = key
       }


    }

