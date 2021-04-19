package com.example.cc_mobileapp.user

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.MainActivity
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.Client_Main
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.Users
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_display_rackdetails.*

class Login : AppCompatActivity() {

    //data declaration
    private lateinit var mAuth : FirebaseAuth
    private val dbUser = FirebaseDatabase.getInstance().getReference(Constant.NODE_USERS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //link to firebase authentication
        mAuth = FirebaseAuth.getInstance()

        //assign intent to every button
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

        //hide or show password
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
        //data initialization
        val emailTxt = findViewById<View>(R.id.txtEmail) as EditText
        val passwordTxt = findViewById<View>(R.id.txtPsw) as EditText
        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()

        //validation
        if(!email.isEmpty() && !password.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(applicationContext, "Valid User Information",
                    Toast.LENGTH_SHORT).show()

            //retrieve all user information from database
            dbUser.get().addOnSuccessListener {
                if (it.exists()) {
                    for (user in it.children) {
                        var user: Users? =
                                user.getValue(Users::class.java)
                        if (user?.userEmail!!.equals(email)) {
                            //check the user is resigned alr or not
                                //if yes no login is allowed
                            if(user?.workingStatus == 1)
                            {
                                var user_pos = user?.workingPosition

                                //sign in firebase by using email and password
                                //validate with the authentication in firebase
                                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, OnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Login Successfully:)", Toast.LENGTH_LONG).show()
                                        var intent:Intent = Intent(this, MainActivity::class.java)
                                        intent.putExtra("user_position", user_pos)
                                        Log.d("Check", "intent ${intent.extras}")
                                        startActivity(intent)

                                    }else {
                                        Toast.makeText(this, "Error Login, try again later :(", Toast.LENGTH_LONG).show()
                                    }
                                })
                            }
                            else
                                Toast.makeText(this, "This account is no longer exist.", Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }

        }
    }
}