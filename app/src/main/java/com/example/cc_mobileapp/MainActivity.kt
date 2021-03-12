package com.example.cc_mobileapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI


var uri = MongoClientURI("mongodb+srv://CYC:cyc1234@cluster0.zyrbf.mongodb.net/myFirstDatabase?retryWrites=true&w=majority")
var mongoClient = MongoClient(uri)
var db = mongoClient.getDatabase(uri.database)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}