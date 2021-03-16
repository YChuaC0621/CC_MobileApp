package com.example.cc_mobileapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI


//var uri = MongoClientURI("mongodb+srv://CYC:cyc1234@cluster0.zyrbf.mongodb.net/myFirstDatabase?retryWrites=true&w=majority")
//var mongoClient = MongoClient(uri)
//var db = mongoClient.getDatabase(uri.database)

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Toast.makeText(this, "firebase connection success", Toast.LENGTH_LONG ).show()
    }
}