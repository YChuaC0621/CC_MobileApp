package com.example.cc_mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.client.Client_Main


//var uri = MongoClientURI("mongodb+srv://CYC:cyc1234@cluster0.zyrbf.mongodb.net/myFirstDatabase?retryWrites=true&w=majority")
//var mongoClient = MongoClient(uri)
//var db = mongoClient.getDatabase(uri.database)

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clientBtn: Button = findViewById(R.id.btn_client)

        clientBtn.setOnClickListener {
            val clientIntent = Intent(this, Client_Main::class.java)
            startActivity(clientIntent)
        }
    }
}