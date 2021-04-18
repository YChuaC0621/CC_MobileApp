package com.example.cc_mobileapp.staff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.model.Users
import com.google.firebase.database.*

class StaffViewModel (): ViewModel() {
    private val dbUser = FirebaseDatabase.getInstance().getReference(Constant.NODE_USERS)

    private val _staffs = MutableLiveData<List<Users>>()
    val staffs: LiveData<List<Users>>
        get() = _staffs

    private val _staff = MutableLiveData<Users>()
    val staff: LiveData<Users>
        get() = _staff

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val childEventListener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("Check", "childListener$snapshot")
            val staff = snapshot.getValue(Users::class.java)
            staff?.userId = snapshot.key
            _staff.value = staff!!
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val staff = snapshot.getValue(Users::class.java)
            staff?.userId = snapshot.key
            _staff.value = staff!!
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d("Check", "onchildremove $snapshot")

            val staff = snapshot.getValue(Users::class.java)
            staff?.userId = snapshot.key
            staff?.workingStatus = 0
            _staff.value = staff!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getRealtimeUpdates(){
        Log.d("Check", "testgetRealtimeupdate")
        dbUser.addChildEventListener(childEventListener)
    }

    fun fetchStaff(){
        dbUser.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch staffs")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val staffs = mutableListOf<Users>()
                    for(staffSnapshot in snapshot.children){
                        val staff = staffSnapshot.getValue(Users::class.java)
                        staff?.userId = staffSnapshot.key
                        if(staff?.workingStatus == 1)
                        {
                            staff?.let {staffs.add(it)}
                        }

                    }
                    _staffs.value = staffs
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateStaff(staff: Users){
        // save inside the unique key
        Log.d("Check", "Update view model $staff")
        dbUser.child(staff.userId.toString()).setValue(staff)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _result.value = null
                    } else {
                        _result.value = it.exception
                    }
                }
    }

    fun deleteStaff(staff: Users){
        Log.d("Check", "Delete view model $staff")
        staff.workingStatus = 0
        dbUser.child(staff.userId.toString()).setValue(staff)
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
        dbUser.removeEventListener(childEventListener)
    }
}