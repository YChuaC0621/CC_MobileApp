package com.example.cc_mobileapp.client

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.google.android.gms.common.api.Api
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.android.synthetic.main.activity_client__main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.android.synthetic.main.activity_main.*

class Client_Main : AppCompatActivity() {

    lateinit var clientRepoRef: DatabaseReference
    lateinit var clientList: MutableList<Client>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client__main)

//        clientRepoRef = FirebaseDatabase.getInstance().getReference("clients")
////
////        plusBtn.setOnClickListener{
////            val clientIntent = Intent(this, Client_Add::class.java)
////            startActivity(clientIntent)
////        }
//
//        clientList = mutableListOf()
//
//        listView = lv_client
//        val adapter = ClientAdapter(this@Client_Main, R.layout.activity_client__main, clientList)
//
//        listView.adapter = adapter
//
//
//        clientRepoRef.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // read value from firebase database
//                // datasnapshot contains all the data value of stores nodes
//                if(snapshot!!.exists()){
//                    for(client in snapshot.children){
//                        val clientFromRepo = client.getValue(Client::class.java)
//                        clientList.add(clientFromRepo!!)
//                    }
//
//                    val adapter = ClientAdapter(applicationContext, R.layout.activity_client__main, clientList)
//                    listView.adapter = adapter
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        });
    }


    //        val repo = ClientRepositoryImp()
//        repo.getClients().observe(this, Observer{
//            Log.d("Check", "Got change in data stream: $it")
//        })
//
//        var myListView = lv_client
//
//        val adapter = ClientAdapter(this@Client_Retrieve, R.layout.activity_client__main, clientList)
//        //instantiate and set adapter
//        myListView.adapter = adapter

//        observeClients()
//    }
//
//    private fun observeClients(){
//        clientViewModel.getClients().observe(this,Observer{
//            var myListView = lv_client
//            var clientsList = it
//            val adapter = ClientAdapter(this@Client_Retrieve, R.layout.client_display_item, clientsList)
//            myListView.adapter = adapter
//        })
//    }

}