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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.example.cc_mobileapp.Constant.NODE_CLIENT
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*

class AddClientDialogFragment : Fragment() {

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
                    Log.d("Check Query", "else Part")
                    var clientNameQuery: Query = FirebaseDatabase.getInstance().reference.child(NODE_CLIENT).orderByChild("clientCoName").equalTo(clientCoName)
                    Log.d("Check Query", "Go in")
                    clientNameQuery.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.childrenCount > 0){
                                Toast.makeText(requireActivity(), "Client Company Name Exist", Toast.LENGTH_SHORT).show()
                                input_layout_clientCoName.error = "Client Company Name Exist"
                            }
                            else{
                                val client = Client()
                                client.clientCoName = clientCoName
                                client.clientEmail = clientEmail
                                client.clientHpNum = clientHp
                                client.clientLocation = clientLocation
                                Log.d("Check", "client data $client")
                                viewModel.addClient(client)
                                requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }
        }
    }

}


