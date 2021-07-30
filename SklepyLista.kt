package com.example.mapa

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SklepyLista : AppCompatActivity() {

        private lateinit var database: FirebaseDatabase
        private var id = 0
        val list = arrayListOf<Shop>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            val binding = com.example.mapa.databinding.ActivitySklepyListaBinding.inflate(layoutInflater)
            setContentView(binding.root)

            database = FirebaseDatabase.getInstance()
            var reference = database.getReference("users/keyy/shops")

            binding.listView.layoutManager = LinearLayoutManager(this)

            binding.listView.addItemDecoration(
                    DividerItemDecoration(this,
                            DividerItemDecoration.VERTICAL)
            )

            binding.listView.adapter = SklepyAdapter(this, list,reference)
            }

        private suspend fun readFromdb(){
            var name: String
            var lat: Double
            var lon: Double
            var des: String
            var rad: Float
            var key: String

            //var reference = database.getReference("users/${Login.user}/shops")
            var reference = database.getReference("users/keyy/shops")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                   list.clear()
                    Log.i("maaaaaaaaaaaaaarkeraaaaaaaaa", MainActivity.listaSklepow.size.toString())

                    for (messageSnapshot in dataSnapshot.children) {
                        name = messageSnapshot.child("name").value.toString()
                        lat = messageSnapshot.child("lat").value.toString().toDouble()
                        lon = messageSnapshot.child("lon").value.toString().toDouble()
                        des = messageSnapshot.child("desc").value.toString()
                        rad = messageSnapshot.child("radius").value.toString().toFloat()
                        key = messageSnapshot.key.toString()

                        var shop = Shop(name = name, lat = lat, lon = lon, desc = des,radius = rad,key = key)

                        list.add(shop)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("readDB-error", databaseError.details)
                }

            })
        }

        override fun onStart() {
            super.onStart()
            CoroutineScope(Dispatchers.IO).launch {
                readFromdb()
            }



        }
    }