package com.example.cc_mobileapp.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.Client_Main
import com.example.cc_mobileapp.product.Product_Main
import com.example.cc_mobileapp.rack.SiteMap
import com.example.cc_mobileapp.report.Report_Main
import com.example.cc_mobileapp.stock.stockIn.StockInActivity
import com.example.cc_mobileapp.stock.stockIn.StockIn_Fragment_Main
import com.example.cc_mobileapp.stock.stockOut.StockOutActivity
import com.example.cc_mobileapp.stock.stockOut.StockOut_Fragment_Main
import com.example.cc_mobileapp.supplier.Supplier_Main
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_usermgmt_display.*
import kotlinx.android.synthetic.main.fragment_edit_supplier.*

class Usermgmt : AppCompatActivity() {
    lateinit var mDatabase : DatabaseReference
    var mAuth = FirebaseAuth.getInstance()
    var user = FirebaseAuth.getInstance().currentUser
    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usermgmt_display)
        drawerLayout = findViewById(R.id.supplier_drawer)
        navigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_userMgmt -> startActivity(Intent(this, Usermgmt::class.java))
                R.id.item_product -> startActivity(Intent(this, Product_Main::class.java))
                R.id.item_stockIn -> startActivity(Intent(this, StockInActivity::class.java))
                R.id.item_stockOut -> startActivity(Intent(this, StockOutActivity::class.java))
                R.id.item_sitemap -> startActivity(Intent(this, SiteMap::class.java))
                R.id.item_client -> startActivity(Intent(this, Client_Main::class.java))
                R.id.item_supplier-> startActivity(Intent(this, Supplier_Main::class.java))
                R.id.item_report -> startActivity(Intent(this, Report_Main::class.java))
                R.id.item_logout -> startActivity(Intent(this, Login::class.java))

            }
            true
        }

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