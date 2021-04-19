package com.example.cc_mobileapp.stock.stockIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.cc_mobileapp.MainActivity
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.Client_Main
import com.example.cc_mobileapp.product.Product_Main
import com.example.cc_mobileapp.rack.SiteMap
import com.example.cc_mobileapp.report.Report_Main
import com.example.cc_mobileapp.staff.Staff_Main
import com.example.cc_mobileapp.stock.stockOut.StockOutActivity
import com.example.cc_mobileapp.supplier.Supplier_Main
import com.example.cc_mobileapp.user.Login
import com.example.cc_mobileapp.user.Usermgmt
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StockInActivity: AppCompatActivity() {
    // variable declaration
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_in)
        // Navigation drawer
        navController = Navigation.findNavController(this,R.id.stockin_nav_host_fragment)
        drawerLayout = findViewById(R.id.stock_in_drawer)
        navigationView = findViewById(R.id.stockin_nav_view)
        NavigationUI.setupWithNavController(navigationView,navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // check user position
        val user_pos = intent.getStringExtra("user_position")
        if(user_pos.equals("1"))
        {
            navigationView.menu.findItem(R.id.item_report).isVisible = false
            navigationView.menu.findItem(R.id.item_manageStaff).isVisible = false
        }
        // set the navigation drawer
        navigationView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.item_homepage -> {
                    var intent: Intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_userMgmt ->  {
                    var intent: Intent = Intent(this, Usermgmt::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_product ->  {
                    var intent: Intent = Intent(this, Product_Main::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_stockIn ->  {
                    var intent: Intent = Intent(this, StockInActivity::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_stockOut ->  {
                    var intent: Intent = Intent(this, StockOutActivity::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_sitemap ->  {
                    var intent: Intent = Intent(this, SiteMap::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_client ->  {
                    var intent: Intent = Intent(this, Client_Main::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_supplier ->  {
                    var intent: Intent = Intent(this, Supplier_Main::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_manageStaff ->  {
                    var intent: Intent = Intent(this, Staff_Main::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_report ->  {
                    var intent: Intent = Intent(this, Report_Main::class.java)
                    intent.putExtra("user_position", user_pos)
                    startActivity(intent)
                }
                R.id.item_logout -> startActivity(Intent(this, Login::class.java))

            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,drawerLayout)
    }

}