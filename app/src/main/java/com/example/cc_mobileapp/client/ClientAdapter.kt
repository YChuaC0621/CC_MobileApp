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

    private var clients = mutableListOf<Client>()
    var listener: ClientRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = ClientViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.client_display_item, parent, false)
    )

    override fun getItemCount() = clients.size

    override fun onBindViewHolder(holder: ClientViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtView_productDesc.text = clients[position].clientCoName
        holder.view.txtView_clientLocation.text = clients[position].clientLocation
        holder.view.btn_clientEdit.setOnClickListener { listener?.onRecycleViewItemClicked(it, clients[position])}
    }

    fun setClients(clients: List<Client>){
        Log.d("Check", "client set: $clients")
        this.clients = clients as MutableList<Client>
        notifyDataSetChanged()
    }

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
        notifyDataSetChanged()
    }


    class ClientViewModel(val view: View) : RecyclerView.ViewHolder(view)
}