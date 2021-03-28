package com.example.cc_mobileapp.user

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_usermgmt_display.*

class Usermgmt : AppCompatActivity() {
    lateinit var mDatabase : DatabaseReference
    var mAuth = FirebaseAuth.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usermgmt_display)

        val nameTxt = findViewById<View>(R.id.txtName) as TextView
        val hpTxt = findViewById<View>(R.id.txthpNum) as TextView
        val emailTxt = findViewById<View>(R.id.txtEmail) as TextView
        var uid = user!!.uid
        emailTxt.text =  user!!.email.toString();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        mDatabase.child(uid).child("Name").addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nameTxt.text =  snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        mDatabase.child(uid).child("Phone").addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hpTxt.text =  snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.themeSpinner, android.R.layout.simple_spinner_item)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        ddlTheme.adapter = adapter

    }

}