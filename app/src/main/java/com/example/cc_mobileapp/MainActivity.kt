package com.example.cc_mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cc_mobileapp.R.string
import com.example.cc_mobileapp.client.Client_Main
import com.example.cc_mobileapp.product.Product_Main
import com.example.cc_mobileapp.rack.SiteMap
import com.example.cc_mobileapp.report.Report_Main
import com.example.cc_mobileapp.stock.Stock_Main
import com.example.cc_mobileapp.stock.stockIn.StockInActivity
import com.example.cc_mobileapp.stock.stockIn.StockIn_Fragment_Main
import com.example.cc_mobileapp.stock.stockOut.StockOutActivity
import com.example.cc_mobileapp.stock.stockOut.StockOut_Fragment_Main
import com.example.cc_mobileapp.supplier.Supplier_Main
import com.example.cc_mobileapp.user.Login
import com.example.cc_mobileapp.user.Usermgmt
import com.google.android.material.navigation.NavigationView


//var uri = MongoClientURI("mongodb+srv://CYC:cyc1234@cluster0.zyrbf.mongodb.net/myFirstDatabase?retryWrites=true&w=majority")
//var mongoClient = MongoClient(uri)
//var db = mongoClient.getDatabase(uri.database)

class MainActivity : AppCompatActivity() {

    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.main_drawer)
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
        val clientBtn: Button = findViewById(R.id.btn_client)

        clientBtn.setOnClickListener {
            val clientIntent = Intent(this, Client_Main::class.java)
            startActivity(clientIntent)
        }

        val supplierBtn: Button = findViewById(R.id.btn_supplier)

        supplierBtn.setOnClickListener {
            val supplierIntent = Intent(this, Supplier_Main::class.java)
            startActivity(supplierIntent)
        }

        val sitemapBtn: Button = findViewById(R.id.btn_sitemap)

        sitemapBtn.setOnClickListener {
            val sitemapIntent = Intent(this, SiteMap::class.java)
            startActivity(sitemapIntent)
        }

        val reportBtn: Button = findViewById(R.id.btn_visualizeData)

        reportBtn.setOnClickListener {
            val reportIntent = Intent(this, Report_Main::class.java)
            startActivity(reportIntent)
        }

        val productBtn: Button = findViewById(R.id.btn_product)

        productBtn.setOnClickListener {
            val productIntent = Intent(this, Product_Main::class.java)
            startActivity(productIntent)
        }

        val stockBtn: Button = findViewById(R.id.btn_product)

        stockBtn.setOnClickListener {
            val stockIntent = Intent(this, Stock_Main::class.java)
            startActivity(stockIntent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            true
        }
        return super.onOptionsItemSelected(item)
    }


}