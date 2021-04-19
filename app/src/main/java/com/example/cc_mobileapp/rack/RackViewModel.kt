package com.example.cc_mobileapp.rack

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.Product
import com.example.cc_mobileapp.model.Rack
import com.example.cc_mobileapp.model.StockDetail
import com.example.cc_mobileapp.model.Supplier
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_display_rackdetails.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RackViewModel () : ViewModel() {

    //data declaration
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _rack = MutableLiveData<Rack>()
    val rack: LiveData<Rack>
        get() = _rack

    private val _racks = MutableLiveData<List<Rack>>()
    val racks: LiveData<List<Rack>>
        get() = _racks

    //retrieve product data from product database
    fun fetchRackDetails() {
        dbRack.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch rack")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val racks = mutableListOf<Rack>()
                    for(Snapshot in snapshot.children){
                        val rack = Snapshot.getValue(Rack::class.java)
                        rack?.rackId = Snapshot.key
                        rack?.let {racks.add(it)}
                    }
                    Log.d("Check", "rack checking in view model")
                    _racks.value = racks
                    Log.d("Check", "rack checking in view model2")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


    //add the rack into database
    fun addRack(rack: Rack)
    {
        //retrieve a new key from rack database
        //assign value under the new key
        rack.rackId = dbRack.push().key
        dbRack.child(rack.rackId.toString()).setValue(rack)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }



}