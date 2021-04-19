package com.example.cc_mobileapp.supplier

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.Client
import com.example.cc_mobileapp.model.Supplier
import com.google.firebase.database.*

class SupplierViewModel(): ViewModel() {

    private val dbSupplier = FirebaseDatabase.getInstance().getReference(Constant.NODE_SUPPLIER)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _suppliers = MutableLiveData<List<Supplier>>()
    val suppliers: LiveData<List<Supplier>>
        get() = _suppliers

    private val _supplier = MutableLiveData<Supplier>()
    val supplier: LiveData<Supplier>
        get() = _supplier

    fun addSupplier(supplier: Supplier) {
        //create unique key
        supplier.supId = dbSupplier.push().key

        // save inside the unique key
        dbSupplier.child(supplier.supId.toString()).setValue(supplier)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    private val childEventListener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("Check", "childListener$snapshot")
            val sup = snapshot.getValue(Supplier::class.java)
            sup?.supId = snapshot.key
            _supplier.value = sup!!
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val sup = snapshot.getValue(Supplier::class.java)
            sup?.supId = snapshot.key
            _supplier.value = sup!!
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d("Check", "onchildremove $snapshot")

            val sup = snapshot.getValue(Supplier::class.java)
            sup?.supId = snapshot.key
            sup?.supStatus = false
            _supplier.value = sup!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getRealtimeUpdates(){
        Log.d("Check", "testgetRealtimeupdate")
        dbSupplier.addChildEventListener(childEventListener)
    }

    fun fetchSuppliers(){
        dbSupplier.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch suppliers")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val sups = mutableListOf<Supplier>()
                    for(supSnapshot in snapshot.children){
                        val sup = supSnapshot.getValue(Supplier::class.java)
                        sup?.supId = supSnapshot.key
                        if(sup?.supStatus == true)
                        {
                            sup?.let {sups.add(it)}
                        }

                    }
                    _suppliers.value = sups
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateSuppliers(supplier: Supplier){
        // save inside the unique key
        Log.d("Check", "Update view model $supplier")
        dbSupplier.child(supplier.supId.toString()).setValue(supplier)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    fun deleteSupplier(supplier: Supplier){
        Log.d("Check", "Delete view model $supplier")
        dbSupplier.child(supplier.supId.toString()).setValue(null)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    override fun onCleared() {
        super.onCleared()
        dbSupplier.removeEventListener(childEventListener)
    }
}