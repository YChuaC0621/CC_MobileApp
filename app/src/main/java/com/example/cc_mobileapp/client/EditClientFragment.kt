package com.example.cc_mobileapp.client

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Client
import kotlinx.android.synthetic.main.fragment_add_client_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_client.*


class EditClientFragment(private val client: Client) : Fragment() {

    private lateinit var  viewModel: ClientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@EditClientFragment).get(ClientViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_client, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_text_editClientCoName.setText(client.clientCoName)
        edit_text_editClientEmail.setText(client.clientEmail)
        edit_text_editClientHp.setText(client.clientHpNum)
        edit_text_editClientLocation.setText(client.clientLocation)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.client_added)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            //dismiss()
            requireActivity().supportFragmentManager.popBackStack("fragmentA", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        })

        btn_clientConfirmEdit.setOnClickListener {
            val clientCoName = edit_text_editClientCoName.text.toString().trim()
            val clientEmail = edit_text_editClientEmail.text.toString().trim()
            val clientHp = edit_text_editClientHp.text.toString().trim()
            val clientLocation = edit_text_editClientLocation.text.toString().trim()
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
                    client.clientCoName = clientCoName
                    client.clientEmail = clientEmail
                    client.clientHpNum = clientHp
                    client.clientLocation = clientLocation
                    Log.d("Check", "Update client data $client")
                    viewModel.updateClient(client)
                }
            }
        }

        btn_dialog_clientDelete.setOnClickListener{
            AlertDialog.Builder(requireContext()).also{
                it.setTitle(getString(R.string.delete_confirmation))
                it.setPositiveButton(getString(R.string.yes)){ dialog, which -> viewModel.deleteClient(client)
                }
            }.create().show()
        }
    }
}
