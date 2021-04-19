package com.example.cc_mobileapp.staff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.cc_mobileapp.MainActivity
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.Client_Main
import com.example.cc_mobileapp.product.Product_Main
import com.example.cc_mobileapp.rack.SiteMap
import com.example.cc_mobileapp.report.Report_Main
import com.example.cc_mobileapp.stock.stockIn.StockInActivity
import com.example.cc_mobileapp.stock.stockOut.StockOutActivity
import com.example.cc_mobileapp.supplier.Supplier_Main
import com.example.cc_mobileapp.user.Login
import com.example.cc_mobileapp.user.Usermgmt
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class Staff_Main: AppCompatActivity() {

    //data declaration for navigation drawer
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_main)

        //navigation drawer part
        navController = Navigation.findNavController(this, R.id.fragmentStaff)
        drawerLayout = findViewById(R.id.staff_drawer)
        navigationView = findViewById(R.id.staff_nav_view)
        NavigationUI.setupWithNavController(navigationView,navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //hide report and manage staff func from staff
        var user_pos = intent.getIntExtra("user_position",1)
        if(user_pos == 1)
        {
            navigationView.menu.findItem(R.id.item_report).isVisible = false
            navigationView.menu.findItem(R.id.item_manageStaff).isVisible = false
        }

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