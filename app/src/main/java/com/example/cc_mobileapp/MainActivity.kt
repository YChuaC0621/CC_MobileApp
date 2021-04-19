package com.example.cc_mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cc_mobileapp.client.Client_Main
import com.example.cc_mobileapp.product.Product_Main
import com.example.cc_mobileapp.rack.SiteMap
import com.example.cc_mobileapp.report.Report_Main
import com.example.cc_mobileapp.staff.Staff_Main
import com.example.cc_mobileapp.stock.Stock_Main
import com.example.cc_mobileapp.stock.stockIn.StockInActivity
import com.example.cc_mobileapp.stock.stockOut.StockOutActivity
import com.example.cc_mobileapp.supplier.Supplier_Main
import com.example.cc_mobileapp.user.Login
import com.example.cc_mobileapp.user.Usermgmt
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    // variable declaration
    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigation drawer
        drawerLayout = findViewById(R.id.main_drawer)
        navigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val user_pos = intent.getStringExtra("user_position")
        if(user_pos.equals("1"))
        {
            navigationView.menu.findItem(R.id.item_report).isVisible = false
            navigationView.menu.findItem(R.id.item_manageStaff).isVisible = false
        }
        // set navigation drawer
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

        // button navigation on click
        val clientBtn: Button = findViewById(R.id.btn_client)

        clientBtn.setOnClickListener {
            var intent: Intent = Intent(this, Client_Main::class.java)
            intent.putExtra("user_position", user_pos)
            startActivity(intent)
        }

        val supplierBtn: Button = findViewById(R.id.btn_supplier)

        supplierBtn.setOnClickListener {
            var intent: Intent = Intent(this, Supplier_Main::class.java)
            intent.putExtra("user_position", user_pos)
            startActivity(intent)
        }

        val sitemapBtn: Button = findViewById(R.id.btn_sitemap)

        sitemapBtn.setOnClickListener {
            var intent: Intent = Intent(this, SiteMap::class.java)
            intent.putExtra("user_position", user_pos)
            startActivity(intent)
        }

        val reportBtn: Button = findViewById(R.id.btn_stockOut)

        reportBtn.setOnClickListener {
            var intent: Intent = Intent(this, StockOutActivity::class.java)
            intent.putExtra("user_position", user_pos)
            startActivity(intent)
        }

        val productBtn: Button = findViewById(R.id.btn_product)

        productBtn.setOnClickListener {
            var intent: Intent = Intent(this, Product_Main::class.java)
            intent.putExtra("user_position", user_pos)
            startActivity(intent)
        }

        val stockInBtn: Button = findViewById(R.id.btn_stocksIn)

        stockInBtn.setOnClickListener {
            var intent: Intent = Intent(this, StockInActivity::class.java)
            intent.putExtra("user_position", user_pos)
            startActivity(intent)
        }

    }

    // toggle to the specific module when the item on the navigation bar is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            true
        }
        return super.onOptionsItemSelected(item)
    }


}