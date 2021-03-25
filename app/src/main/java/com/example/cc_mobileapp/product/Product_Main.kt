package com.example.cc_mobileapp.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cc_mobileapp.R


class Product_Main : AppCompatActivity(R.layout.activity_product__main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.product_nav_host_fragment)as NavHostFragment

        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

    }
}