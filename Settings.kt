package com.example.mapa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.WHITE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mapa.databinding.ActivitySettingsBinding


import kotlin.random.Random

class Settings : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = com.example.mapa.databinding.ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        sp = getSharedPreferences("pref",0)
        editor = sp.edit()

        val listbutton = findViewById<Button>(R.id.button_kolor)
        listbutton.setOnClickListener{

            val colors = arrayOf(
                    Color.parseColor("#1771F1"),
                    Color.parseColor("#B5FBDD"),
                    Color.parseColor("#A7E541"),
                    Color.parseColor("#FFC11E"),
                    Color.parseColor("#F5E027"),
                    Color.parseColor("#FF6A61")
            )
            val randomColor = colors.random()

            editor.putInt("color",randomColor)
            editor.apply()

           var view_kolor = findViewById<TextView>(R.id.textView2)
            view_kolor.setText(randomColor.toString())
        }

        binding.buttonName.setOnClickListener{


           var nameuser = binding.editName.text.toString()
            editor.putString("user",nameuser)
            editor.apply()



        }

    }
}