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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.txtPsw
import kotlinx.android.synthetic.main.activity_signup.*

class Registration : AppCompatActivity() {
    lateinit var mDatabase : DatabaseReference
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val continueBtn: Button = findViewById(R.id.btn_continue)

        continueBtn.setOnClickListener {
           registerUser();
        }

        val loginBtn: TextView = findViewById(R.id.txtRegister)

        loginBtn.setOnClickListener {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }

        val viewPsw : CheckBox = findViewById(R.id.chk_showPsw)

        viewPsw.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                txtPsw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                txtRepeatPsw.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                txtPsw.transformationMethod = PasswordTransformationMethod.getInstance()
                txtRepeatPsw.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

    }

    private fun registerUser () {
        val emailTxt = findViewById<View>(R.id.txtEmail) as EditText
        val passwordTxt = findViewById<View>(R.id.txtPsw) as EditText
        val repeatPasswordTxt = findViewById<View>(R.id.txtRepeatPsw) as EditText
        val nameTxt = findViewById<View>(R.id.txtName) as EditText
        val hpNumTxt = findViewById<View>(R.id.txthpNum) as EditText

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()
        var repeatPassword = repeatPasswordTxt.text.toString()
        var name = nameTxt.text.toString()
        var hp = hpNumTxt.text.toString()
        var working_pos = 1
        var working_status = 1


        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()&& !repeatPassword.isEmpty() && !hp.isEmpty()) {
            if(password.equals(repeatPassword) && password.length > 6)
            {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    if(android.util.Patterns.PHONE.matcher(hp).matches())
                    {
                        Toast.makeText(applicationContext, "Valid User Information",
                                Toast.LENGTH_SHORT).show()

                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = mAuth.currentUser
                                    val uid = user!!.uid
                                    mDatabase.child(uid).child("userName").setValue(name)
                                    mDatabase.child(uid).child("userEmail").setValue(email)
                                    mDatabase.child(uid).child("userPsw").setValue(password)
                                    mDatabase.child(uid).child("userHpNum").setValue(hp)
                                    mDatabase.child(uid).child("workingStatus").setValue(working_status)
                                    mDatabase.child(uid).child("workingPosition").setValue(working_pos)
                                    startActivity(Intent(this, MainActivity::class.java))
                                    Toast.makeText(this, "Successfully registered :) Please verify your email.", Toast.LENGTH_LONG).show()
                                    sendEmailVerification();
                                }else {
                                    Toast.makeText(this, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
                                }
                            })
                    }
                    else {
                        Toast.makeText(applicationContext, "Invalid phone number",
                                Toast.LENGTH_SHORT).show()
                    }

                }
                else {
                    Toast.makeText(applicationContext, "Invalid email address",
                            Toast.LENGTH_SHORT).show()
                }

            }
            else {
                Toast.makeText(applicationContext, "Repeat password not same as password and the password should more than 6 characters",
                        Toast.LENGTH_SHORT).show()
            }

        }else {
            Toast.makeText(this,"Please fill up the Credentials :|", Toast.LENGTH_LONG).show()
        }

    }

    private fun sendEmailVerification() {
        val user = mAuth!!.currentUser
        user!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Verification email sent to " + user.email!!, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}