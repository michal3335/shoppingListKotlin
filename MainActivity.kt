package com.example.mapa

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mapa.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

   lateinit var binding : ActivityMainBinding
    private lateinit var database: FirebaseDatabase


    companion object {
        var listaSklepow = arrayListOf<Shop>()
    }
    override fun onResume() {
        super.onResume()

        val mPrefs = getSharedPreferences("pref", 0)
        val int = mPrefs.getInt("color", Color.blue(500))



        binding.root.setBackgroundColor(int)

        val user = mPrefs.getString("user", "Uzytkowniku")

        binding.nameView.text = "Witaj " + user

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET

        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.INTERNET
                ) != PackageManager.PERMISSION_GRANTED

        ) {
            requestPermissions(permissions, 0)
        }


        database = FirebaseDatabase.getInstance()
        val mPrefs = getSharedPreferences("pref", 0)
        val int = mPrefs.getInt("color", Color.green(240))
        binding.root.setBackgroundColor(int)

        val user = mPrefs.getString("user", "Uzytkowniku")

        binding.nameView.text = "Witaj " + user


        val listbutton = findViewById<Button>(R.id.button_list)
        listbutton.setOnClickListener{
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        val listbutton2 = findViewById<Button>(R.id.button_list2)
        listbutton2.setOnClickListener{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        val mapbutton = findViewById<Button>(R.id.bt_mapa)
        mapbutton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        val shopbutton = findViewById<Button>(R.id.bt_sklepy)
        shopbutton.setOnClickListener {
            val intent = Intent(this, SklepyLista::class.java)
            startActivity(intent)
        }


    }

    private fun readFromdb(){
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
                listaSklepow.clear()

                for (messageSnapshot in dataSnapshot.children) {
                    name = messageSnapshot.child("name").value.toString()
                    lat = messageSnapshot.child("lat").value.toString().toDouble()
                    lon = messageSnapshot.child("lon").value.toString().toDouble()
                    des = messageSnapshot.child("desc").value.toString()
                    rad = messageSnapshot.child("radius").value.toString().toFloat()
                    key = messageSnapshot.key.toString()

                    var shop = Shop(name = name, lat = lat, lon = lon, desc = des,radius = rad,key = key)

                    listaSklepow.add(shop)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("readDB-error", databaseError.details)
            }

        })

    }

    override fun onStart() {
        super.onStart()
        readFromdb()


    }



}