package com.example.cc_mobileapp.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.databinding.ActivityClientAddBinding

class Client_Add : AppCompatActivity() {

    private lateinit var binding: ActivityClientAddBinding
    private lateinit var companyName: String
    private lateinit var email: String
    private lateinit var contactNo: String
    private lateinit var location: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientAddBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_client__add)




//        var viewModel = ViewModelProvider(this).get(ClientViewModel::class.java)
//
//        val saveBtn: Button = findViewById(R.id.btn_save)
//
//        saveBtn.setOnClickListener {
//
//        }
    }
}