package com.example.cc_mobileapp.stock

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.stock.stockIn.StockInActivity
import com.example.cc_mobileapp.stock.stockOut.StockOutActivity
import kotlinx.android.synthetic.main.activity_stock__main.*

class Stock_Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock__main)

        val stockIn: Button = findViewById(R.id.btn_stockIn)
        val stockOut: Button = findViewById(R.id.btn_stockOut)

        stockIn.setOnClickListener{
            val stockInIntent = Intent(this, StockInActivity::class.java)
            startActivity(stockInIntent)
        }

        stockOut.setOnClickListener{
            val stockOutIntent = Intent(this, StockOutActivity::class.java)
            startActivity(stockOutIntent)
        }
    }
}