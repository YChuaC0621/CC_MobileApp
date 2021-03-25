package com.example.cc_mobileapp.client

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_client__main.*
import kotlinx.android.synthetic.main.activity_main.*

class Client_Main : AppCompatActivity() {

    lateinit var clientRepoRef: DatabaseReference
    lateinit var clientList: MutableList<Client>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client__main)
    }

    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            Log.i("MainActivity", "popping backstack")
            fm.popBackStack()
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super")
            super.onBackPressed()
        }
    }
}