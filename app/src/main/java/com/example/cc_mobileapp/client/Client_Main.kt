package com.example.cc_mobileapp.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.product.Product_Main
import com.example.cc_mobileapp.rack.SiteMap
import com.example.cc_mobileapp.report.Report_Main
import com.example.cc_mobileapp.stock.stockIn.StockInActivity
import com.example.cc_mobileapp.stock.stockIn.StockIn_Fragment_Main
import com.example.cc_mobileapp.stock.stockOut.StockOutActivity
import com.example.cc_mobileapp.stock.stockOut.StockOut_Fragment_Main
import com.example.cc_mobileapp.supplier.Supplier_Main
import com.example.cc_mobileapp.user.Login
import com.example.cc_mobileapp.user.Usermgmt
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_client__main.*
import kotlinx.android.synthetic.main.activity_main.*

class Client_Main : AppCompatActivity() {

    lateinit var clientRepoRef: DatabaseReference
    lateinit var clientList: MutableList<Client>
    lateinit var listView: ListView
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client__main)
        navController = Navigation.findNavController(this,R.id.fragmentClient)
        drawerLayout = findViewById(R.id.client_drawer)
        navigationView = findViewById(R.id.client_nav_view)
        NavigationUI.setupWithNavController(navigationView,navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout )
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

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,drawerLayout)
    }
}