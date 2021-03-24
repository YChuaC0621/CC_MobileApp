package com.example.cc_mobileapp.client

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant.NODE_CLIENT
import com.example.cc_mobileapp.model.Client
import com.google.firebase.database.*


class ClientViewModel(): ViewModel() {

    private val dbClient = FirebaseDatabase.getInstance().getReference(NODE_CLIENT)


    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _clients = MutableLiveData<List<Client>>()
        val clients: LiveData<List<Client>>
        get() = _clients

    private val _client = MutableLiveData<Client>()
    val client: LiveData<Client>
        get() = _client

    fun addClient(client: Client) {
        //create unique key
        client.clientId = dbClient.push().key

        // save inside the unique key
        dbClient.child(client.clientId.toString()).setValue(client)
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
            val client = snapshot.getValue(Client::class.java)
            client?.clientId = snapshot.key
            _client.value = client
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val client = snapshot.getValue(Client::class.java)
            client?.clientId = snapshot.key
            _client.value = client
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d("Check", "onchildremove $snapshot")

            val client = snapshot.getValue(Client::class.java)
            client?.clientId = snapshot.key
            client?.isDeleted = true
            _client.value = client
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getRealtimeUpdates(){
        Log.d("Check", "testgetRealtimeupdate")
        dbClient.addChildEventListener(childEventListener)
    }

    fun fetchClients(){
        dbClient.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Check", "fetch clients")
                if(snapshot.exists()){
                    Log.d("Check", "has snapshot $snapshot")
                    val clients = mutableListOf<Client>()
                    for(clientSnapshot in snapshot.children){
                        val client = clientSnapshot.getValue(Client::class.java)
                        client?.clientId = clientSnapshot.key
                        client?.let {clients.add(it)}
                    }
                    _clients.value = clients
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateClient(client: Client){
    // save inside the unique key
        Log.d("Check", "Update view model $client")
        dbClient.child(client.clientId.toString()).setValue(client)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    fun deleteClient(client:Client){
        Log.d("Check", "Delete view model $client")
        dbClient.child(client.clientId.toString()).setValue(null)
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
        dbClient.removeEventListener(childEventListener)
    }
}