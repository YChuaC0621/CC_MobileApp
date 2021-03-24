package com.example.cc_mobileapp.client

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*

class AddClientDialogFragment : DialogFragment() {

    private lateinit var viewModel: ClientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@AddClientDialogFragment).get(ClientViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_client_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState:Bundle?){
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer{
            val message = if(it == null) {// success
                getString(R.string.client_added)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message,Toast.LENGTH_SHORT).show()
        })

        btn_add.setOnClickListener{
            val clientCoName = edit_text_clientCoName.text.toString().trim()
            val clientEmail = edit_text_clientEmail.text.toString().trim()
            val clientHp = edit_text_clientHp.text.toString().trim()
            val clientLocation = edit_text_clientLocation.text.toString().trim()

            when {
                clientCoName.isEmpty() -> {
                    input_layout_clientCoName.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                clientEmail.isEmpty() -> {
                    input_layout_clientEmail.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                clientHp.isEmpty() -> {
                    input_layout_clientHp.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                clientLocation.isEmpty() -> {
                    input_layout_clientLocation.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                else -> {
                    val client = Client()
                    client.clientCoName = clientCoName
                    client.clientEmail = clientEmail
                    client.clientHpNum = clientHp
                    client.clientLocation = clientLocation
                    Log.d("Check", "client data $client")
                    viewModel.addClient(client)
                }
            }
        }
    }

}


