package com.example.mapa

import android.Manifest
import android.content.ComponentName

import android.content.Intent

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapa.Login.Companion.user
import com.example.mapa.databinding.ActivityListBinding
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch


class ListActivity : AppCompatActivity() {


    private lateinit var database: FirebaseDatabase
    private var id = 0
    val list = arrayListOf<Produkt>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       val binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        var reference = database.getReference("users/$user")

        binding.listView.layoutManager = LinearLayoutManager(this)

        binding.listView.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )


       binding.listView.adapter = MyAdapter(this, list,reference)


        binding.button.setOnClickListener {


           var name2 : String = binding.name.text.toString()


            var costs: String = binding.cost.text.toString()
            val cost = costs.toDouble()


           val quantityy: String = binding.quantity.text.toString()
           val quantity = quantityy.toInt()
           var check: Boolean = binding.checkBox2.isChecked

            CoroutineScope(Dispatchers.IO).launch {
                dbAdd(name2,cost,quantity,check)
            }

        }

        }

    private suspend fun readFromdb(){
        var name: String
        var quantity: Int
        var cost: Double
        var buy: Boolean
        var key : String
        var reference = database.getReference("users/$user")
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for( messageSnapshot in dataSnapshot.children){
                    name = messageSnapshot.child("name").value.toString()
                    cost = messageSnapshot.child("cost").value.toString().toDouble()
                    quantity =  messageSnapshot.child("quantity").value.toString().toInt()
                    buy =  messageSnapshot.child("buy").value.toString().toBoolean()
                    key = messageSnapshot.key.toString()
                    var produkt = Produkt(name = name, cost = cost, quantity = quantity, buy = buy, key = key)

                   list.add(produkt)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("readDB-error", databaseError.details)
            }

        })
    }

    private suspend fun dbAdd(name:String, cost: Double, quantity: Int, buy: Boolean){

       var reference = database.getReference("users/$user")
        var produkt = Produkt(name = name, cost = cost, quantity = quantity, buy = buy)

        reference.push().setValue(produkt)

    }


    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            readFromdb()
        }


    }
}