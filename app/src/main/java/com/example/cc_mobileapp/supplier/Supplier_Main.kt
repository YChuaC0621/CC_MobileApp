package com.example.cc_mobileapp.supplier

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.Supplier
import com.google.firebase.database.DatabaseReference

class Supplier_Main : AppCompatActivity() {

    lateinit var supDB: DatabaseReference
    lateinit var supList: MutableList<Supplier>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_main)
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