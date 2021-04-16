package com.example.cc_mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.client.Client_Main
import com.example.cc_mobileapp.product.Product_Main
import com.example.cc_mobileapp.rack.SiteMap
import com.example.cc_mobileapp.report.Report_Main
import com.example.cc_mobileapp.stock.Stock_Main
import com.example.cc_mobileapp.supplier.Supplier_Main


//var uri = MongoClientURI("mongodb+srv://CYC:cyc1234@cluster0.zyrbf.mongodb.net/myFirstDatabase?retryWrites=true&w=majority")
//var mongoClient = MongoClient(uri)
//var db = mongoClient.getDatabase(uri.database)

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}