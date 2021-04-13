package com.example.cc_mobileapp.user

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_usermgmt_display.*
import kotlinx.android.synthetic.main.fragment_edit_supplier.*

class Usermgmt : AppCompatActivity() {
    lateinit var mDatabase : DatabaseReference
    var mAuth = FirebaseAuth.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usermgmt_display)

        val nameTxt = findViewById<View>(R.id.editTxt_userName) as TextView
        val hpTxt = findViewById<View>(R.id.editTxt_userHpNum) as TextView
        val emailTxt = findViewById<View>(R.id.editTxt_userEmail) as TextView
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

        val saveBtn: Button = findViewById(R.id.btn_Save)

        saveBtn.setOnClickListener {
            val userName = editTxt_userName.text.toString().trim()
            val userEmail = editTxt_userEmail.text.toString().trim()
            val userHpNum = editTxt_userHpNum.text.toString().trim()
            var uid = user!!.uid
            mDatabase = FirebaseDatabase.getInstance().getReference("Users")
            when{
                userName.isEmpty() -> {
                    txtInputLayout_userName.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                userEmail.isEmpty() -> {
                    txtInputLayout_userEmail.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() -> {
                    txtInputLayout_userEmail.error = getString(R.string.errorEmail)
                    return@setOnClickListener
                }
                userHpNum.isEmpty() -> {
                    txtInputLayout_userHpNum.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                !android.util.Patterns.PHONE.matcher(userHpNum).matches() -> {
                    txtInputLayout_userHpNum.error = getString(R.string.errorPhoneNum)
                    return@setOnClickListener
                }
                else -> {
                    mDatabase.child(uid).child("Name").setValue(userName)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Update User Name Successfully:)", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this, "Failed to update User Name", Toast.LENGTH_LONG).show()
                                }
                            }
                    mDatabase.child(uid).child("Phone").setValue(userHpNum)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Update Phone Number Successfully:)", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this, "Failed to update phone number", Toast.LENGTH_LONG).show()
                                }
                            }
                    user.updateEmail(userEmail).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Update Email Successfully:)", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            Toast.makeText(this, "Failed to update email", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    }
}