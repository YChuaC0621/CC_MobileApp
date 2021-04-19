package com.example.cc_mobileapp.client

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cc_mobileapp.Constant.NODE_CLIENT
import com.example.cc_mobileapp.model.Client
import com.google.firebase.database.*


class ClientViewModel(): ViewModel() {

    // variable
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

    // add client information
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

    // check for realtime changes
    private val childEventListener = object: ChildEventListener {
        // add client
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("Check", "childListener$snapshot")
            val client = snapshot.getValue(Client::class.java)
            client?.clientId = snapshot.key
            _client.value = client!!
        }

        // edit client
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val client = snapshot.getValue(Client::class.java)
            client?.clientId = snapshot.key
            _client.value = client!!
        }

        // delete client
        override fun onChildRemoved(snapshot: DataSnapshot) {
            val client = snapshot.getValue(Client::class.java)
            client?.clientId = snapshot.key
            client?.isDeleted = true
            _client.value = client!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    // listen to the information in the database
    fun getRealtimeUpdates(){
        dbClient.addChildEventListener(childEventListener)
    }

    // get all the client information from database
    fun fetchClients(){
        dbClient.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
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


    // update client information to database
    fun updateClient(client: Client){
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

    // delete client information to database
    fun deleteClient(client:Client){
        dbClient.child(client.clientId.toString()).setValue(null)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
        }

    // when the fragment is cleared the listener of client database in this fragment is cleared
    override fun onCleared() {
        super.onCleared()
        dbClient.removeEventListener(childEventListener)
    }
}