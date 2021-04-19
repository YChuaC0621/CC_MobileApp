package com.example.cc_mobileapp.client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_client.*
import kotlinx.android.synthetic.main.fragment_edit_client.*

class ClientFragment : Fragment(), ClientRecyclerViewClickListener {

    // variable
    private lateinit var viewModel: ClientViewModel
    private val adapter = ClientAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // access the information from client view model
        viewModel = ViewModelProvider(this@ClientFragment).get(ClientViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("Check", "client fragment on activity created")

        super.onActivityCreated(savedInstanceState)

        // to be use for the listener on the button for each item on the listener
        adapter.listener = this
        recycler_view_client.adapter = adapter

        // get the clients information
        viewModel.fetchClients()

        // get real time updates
        viewModel.getRealtimeUpdates()

        // observe the changes in clients, if have changes, set the clients information and make changes on UI
        viewModel.clients.observe(viewLifecycleOwner, Observer {
            adapter.setClients(it)
            Log.d("Check", "client fragment on activity created$it")
        })

        // observe the changes in clients, if have changes, set the client information and make changes on UI
        viewModel.client.observe(viewLifecycleOwner, Observer {
            Log.d("Check", "B4 realtime add  client fragment on activity created$it")
            adapter.addClient(it)
            Log.d("Check", "realtime add  client fragment on activity created$it")
        })

        // when add button is click, go the add client fragment
        button_add.setOnClickListener{
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddClientDialogFragment())
            transaction.addToBackStack("addClientFragment")
            transaction.commit()
        }
    }

    // get the particular recycler view information, pass to the next edit fragment which require the information
    override fun onRecycleViewItemClicked(view: View, client: Client) {
        val currentView = (requireView().parent as ViewGroup).id
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(currentView, EditClientFragment(client))
        transaction.addToBackStack("editClientFragment")
        transaction.commit()
    }
}