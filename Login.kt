package com.example.mapa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapa.databinding.ActivityLogin2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.math.BigInteger
import java.security.MessageDigest


class Login : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    companion object {
        var user = "key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var name = "uzytkownik"
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")

        mAuth = FirebaseAuth.getInstance();

        binding.log.setOnClickListener {

            mAuth.signInWithEmailAndPassword(binding.login.text.toString(), binding.password.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){

                        user = md5(binding.login.text.toString())
                        Toast.makeText(this,"Zalogowano",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                    }else{
                        Toast.makeText(this,"Niepowodzenie",Toast.LENGTH_SHORT).show()

                    }
                }

        }

        binding.register.setOnClickListener {
            mAuth.createUserWithEmailAndPassword(binding.login.text.toString(), binding.password.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Rejestracja przebiegła pomyślnie",Toast.LENGTH_SHORT).show()
                        reference.child(md5(binding.login.text.toString())).setValue(name)
                    }else{
                        Toast.makeText(this,"Niepowodzenie rejestracji",Toast.LENGTH_SHORT).show()

                    }
                }
        }

    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}