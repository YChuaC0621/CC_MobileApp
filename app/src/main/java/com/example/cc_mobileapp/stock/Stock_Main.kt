package com.example.cc_mobileapp.stock

import android.content.res.Resources
import android.os.Bundle
import android.widget.TabHost
import androidx.appcompat.app.AppCompatActivity
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.stock.stockIn.StockIn_Fragment_Main
import com.example.cc_mobileapp.stock.stockOut.StockOut_Fragment_Main
import kotlinx.android.synthetic.main.activity_stock__main.*

class Stock_Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock__main)

        setUpTabs()
    }

    private fun setUpTabs(){
        val adapter = StockPagerAdapter(supportFragmentManager)
        adapter.addFragment(StockIn_Fragment_Main(),"Stock In")
        adapter.addFragment(StockOut_Fragment_Main(), "Stock Out")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}