package com.example.cc_mobileapp.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cc_mobileapp.MainActivity
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.Client_Main
import com.example.cc_mobileapp.product.Product_Main
import com.example.cc_mobileapp.rack.SiteMap
import com.example.cc_mobileapp.report.Report_Main
import com.example.cc_mobileapp.staff.Staff_Main
import com.example.cc_mobileapp.stock.stockIn.StockInActivity
import com.example.cc_mobileapp.stock.stockIn.StockIn_Fragment_Main
import com.example.cc_mobileapp.stock.stockOut.StockOutActivity
import com.example.cc_mobileapp.stock.stockOut.StockOut_Fragment_Main
import com.example.cc_mobileapp.supplier.Supplier_Main
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_usermgmt_display.*
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_add_supplier.*
import kotlinx.android.synthetic.main.fragment_edit_supplier.*
import kotlinx.android.synthetic.main.fragment_edit_supplier.txtInputLayout_supHpNum

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

        val nameTxt = findViewById<View>(R.id.editTxt_userName) as TextView
        val hpTxt = findViewById<View>(R.id.editTxt_userHpNum) as TextView
        val emailTxt = findViewById<View>(R.id.editTxt_userEmail) as TextView
        val statusTxt = findViewById<View>(R.id.editTxt_workingp_status) as TextView
        val positionTxt = findViewById<View>(R.id.editTxt_working_position) as TextView

        var uid = user!!.uid
        emailTxt.text = user!!.email.toString();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        mDatabase.child(uid).child("userName").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nameTxt.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        mDatabase.child(uid).child("userHpNum").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hpTxt.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        mDatabase.child(uid).child("workingPosition").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value.toString().equals("1")) {
                    positionTxt.text = "Staff"
                } else if (snapshot.value.toString().equals("2")) {
                    positionTxt.text = "Top Management"
                }

                positionTxt.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        mDatabase.child(uid).child("workingStatus").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value.toString().equals("1")) {
                    statusTxt.text = "Employed"
                } else if (snapshot.value.toString().equals("0")) {
                    statusTxt.text = "Resigned"
                }
                statusTxt.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        /*// Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.themeSpinner, android.R.layout.simple_spinner_item)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        ddlTheme.adapter = adapter
*/
        val saveBtn: Button = findViewById(R.id.btn_Save)

        saveBtn.setOnClickListener {
            val userName = editTxt_userName.text.toString().trim()
            val userEmail = editTxt_userEmail.text.toString().trim()
            val userHpNum = editTxt_userHpNum.text.toString().trim()
            var uid = user!!.uid
            var valid = true
            mDatabase = FirebaseDatabase.getInstance().getReference("Users")
            if (userName.isEmpty()) {
                txtInputLayout_userName.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else {
                txtInputLayout_userName.error = null
            }
            if (userEmail.isEmpty()) {
                txtInputLayout_userEmail.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else {
                txtInputLayout_userEmail.error = null
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                txtInputLayout_userEmail.error = getString(R.string.errorEmail)
                valid = false
                return@setOnClickListener
            } else {
                txtInputLayout_userEmail.error = null
            }
            if (userHpNum.isEmpty()) {
                txtInputLayout_userHpNum.error = getString(R.string.error_field_required)
                valid = false
                return@setOnClickListener
            } else if(!checkRegexhpNum(userHpNum)){
                txtInputLayout_supHpNum.error = getString(R.string.phone_format_error)
                valid = false
                return@setOnClickListener
            }  else {
                txtInputLayout_userHpNum.error = null
            }


            if (valid) {
                mDatabase.child(uid).child("userName").setValue(userName)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Update User Name Successfully:)", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "Failed to update User Name", Toast.LENGTH_LONG).show()
                            }
                        }
                mDatabase.child(uid).child("userHpNum").setValue(userHpNum)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Update Phone Number Successfully:)", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "Failed to update phone number", Toast.LENGTH_LONG).show()
                            }
                        }
                mDatabase.child(uid).child("userEmail").setValue(userEmail)
                user.updateEmail(userEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Update Email Successfully:)", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to update email", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }


        val forgtPswBtn: TextView = findViewById(R.id.txtForgtPsw)

        forgtPswBtn.setOnClickListener {
            val resetPswIntent = Intent(this, ResetPsw::class.java)
            startActivity(resetPswIntent)
        }

        drawerLayout = findViewById(R.id.userMgmt_drawer)
        navigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(positionTxt.text.equals("Staff"))
        {
            navigationView.menu.findItem(R.id.item_report).isVisible = false
            navigationView.menu.findItem(R.id.item_manageStaff).isVisible = false
        }

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_homepage -> startActivity(Intent(this, MainActivity::class.java))
                R.id.item_userMgmt -> startActivity(Intent(this, Usermgmt::class.java))
                R.id.item_product -> startActivity(Intent(this, Product_Main::class.java))
                R.id.item_stockIn -> startActivity(Intent(this, StockInActivity::class.java))
                R.id.item_stockOut -> startActivity(Intent(this, StockOutActivity::class.java))
                R.id.item_sitemap -> startActivity(Intent(this, SiteMap::class.java))
                R.id.item_client -> startActivity(Intent(this, Client_Main::class.java))
                R.id.item_supplier-> startActivity(Intent(this, Supplier_Main::class.java))
                R.id.item_manageStaff -> startActivity(Intent(this, Staff_Main::class.java))
                R.id.item_report -> startActivity(Intent(this, Report_Main::class.java))
                R.id.item_logout -> startActivity(Intent(this, Login::class.java))

            }
            true
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkRegexhpNum(hpNum: String): Boolean {
        var hpNum: String = hpNum
        var regex: Regex = Regex(pattern = """\d+""")
        return regex.matches(input = hpNum) && hpNum.startsWith("01")
    }
}