package com.example.cc_mobileapp.rack

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.Rack
import com.example.cc_mobileapp.model.Supplier
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RackViewModel : ViewModel() {

    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _racks = MutableLiveData<List<Rack>>()
    val racks: LiveData<List<Rack>>
        get() = _racks

    private val _rack = MutableLiveData<Rack>()
    val rack: LiveData<Rack>
        get() = _rack

    fun addRack(rack: Rack)
    {
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

    fun fetchRacks(){
        dbRack.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch racks")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val racks = mutableListOf<Rack>()
                    for(rackSnapshot in snapshot.children){
                        val rack = rackSnapshot.getValue(Rack::class.java)
                        rack?.rackId = rackSnapshot.key
                        rack?.let {racks.add(it)}
                    }
                    _racks.value = racks
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchSingleRack(rack_name : String){
        dbRack.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch racks")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    for(rackSnapshot in snapshot.children){
                        val rack = rackSnapshot.getValue(Rack::class.java)
                        if(rack?.rackName == rack_name)
                        {
                            rack?.rackId =  rackSnapshot.key
                            _rack.value = rack!!
                            Log.d("Check", "has snapshot ${_rack.value?.rackName.toString()}")
                            Log.d("Check", "has snapshot ${_rack.value?.startLot.toString()}")
                            Log.d("Check", "has snapshot ${_rack.value?.endLot.toString()}")
                            Log.d("Check", "has snapshot ${_rack.value?.row_num.toString()}")
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}