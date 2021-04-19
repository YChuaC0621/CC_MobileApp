package com.example.cc_mobileapp.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class ResetPsw : AppCompatActivity() {

    //data declaration
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpsw)

        mAuth = FirebaseAuth.getInstance()

        //assign activity to button
        val resetBtn: TextView = findViewById(R.id.btn_continue)

        resetBtn.setOnClickListener {
            resetpsw()
        }

        val backBtn: TextView = findViewById(R.id.txtBack)

        backBtn.setOnClickListener {
            finish()
        }

        val registerBtn: TextView = findViewById(R.id.txtRegister)

        registerBtn.setOnClickListener {
            val registerIntent = Intent(this, Registration::class.java)
            startActivity(registerIntent)
        }
    }

    private fun resetpsw()
    {
            val emailTxt = findViewById<View>(R.id.txtVerification) as EditText
            var email = emailTxt.text.toString()

        //validation
            if (email.isEmpty()) {
                Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT).show()
            } else {
                //send email to user
                    //user required to change the password through the email
                mAuth!!.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@ResetPsw, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@ResetPsw, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }

    }

