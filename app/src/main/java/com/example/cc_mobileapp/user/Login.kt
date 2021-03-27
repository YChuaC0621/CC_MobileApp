package com.example.cc_mobileapp.user

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.MainActivity
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.Client_Main
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance();
        val registerBtn: TextView = findViewById(R.id.txtRegister)

        registerBtn.setOnClickListener {
            val registerIntent = Intent(this, Registration::class.java)
            startActivity(registerIntent)
        }

        val loginBtn: Button = findViewById(R.id.btn_continue)

        loginBtn.setOnClickListener {
            loginUser();
        }

        val viewPsw : CheckBox = findViewById(R.id.chk_showPsw)

        viewPsw.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                txtPsw.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                txtPsw.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        val forgtPswBtn: TextView = findViewById(R.id.txtForgtPsw)

        forgtPswBtn.setOnClickListener {
            val resetPswIntent = Intent(this, ResetPsw::class.java)
            startActivity(resetPswIntent)
        }


    }

    private fun loginUser()
    {
        val emailTxt = findViewById<View>(R.id.txtEmail) as EditText
        val passwordTxt = findViewById<View>(R.id.txtPsw) as EditText
        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()

        if(!email.isEmpty() && !password.isEmpty())
        {
            Toast.makeText(applicationContext, "Valid User Information",
                    Toast.LENGTH_SHORT).show()
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Login Successfully:)", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this, "Error Login, try again later :(", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}