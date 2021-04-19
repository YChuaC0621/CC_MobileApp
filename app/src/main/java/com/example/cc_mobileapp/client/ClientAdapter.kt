package com.example.cc_mobileapp.client

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import kotlinx.android.synthetic.main.client_display_item.view.*


class ClientAdapter: RecyclerView.Adapter<ClientAdapter.ClientViewModel>(){

    // variable declaration
    private var clients = mutableListOf<Client>()
    var listener: ClientRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = ClientViewModel(
            // inflate the client_display_item layout
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.client_display_item, parent, false)
    )

    // get total clients count
    override fun getItemCount() = clients.size

    // bind the information to the user interface
    override fun onBindViewHolder(holder: ClientViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtView_clientCoName.text = clients[position].clientCoName
        holder.view.txtView_clientLocation.text = clients[position].clientLocation
        holder.view.txtView_clientEmail.text = clients[position].clientEmail
        holder.view.txtView_clientHpNum.text = clients[position].clientHpNum
        holder.view.btn_clientEdit.setOnClickListener { listener?.onRecycleViewItemClicked(it, clients[position])}
    }

    // set the clients information
    fun setClients(clients: List<Client>){
        Log.d("Check", "client set: $clients")
        this.clients = clients as MutableList<Client>
        // allow real time changes
        notifyDataSetChanged()
    }

    // add client information
    fun addClient(client: Client) {
        Log.d("Check", "real time add client $client")
        if (!clients.contains(client)) {
            clients.add(client)
        }else{  // delete client
            val clientUpdateIndex = clients.indexOf(client)
            if(client.isDeleted){
                clients.removeAt(clientUpdateIndex)
            }else{ // for update client
                clients[clientUpdateIndex] = client
            }
        }
        // allow real time changes
        notifyDataSetChanged()
    }
    class ClientViewModel(val view: View) : RecyclerView.ViewHolder(view)
}
