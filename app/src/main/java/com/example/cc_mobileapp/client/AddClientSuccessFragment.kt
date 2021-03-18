package com.example.cc_mobileapp.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.databinding.FragmentAddClientSuccessBinding

class AddClientSuccessFragment : Fragment() {

    private var binding: FragmentAddClientSuccessBinding? = null

    private val sharedViewModel: ClientViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        val addClientSuccessBinding =FragmentAddClientSuccessBinding.inflate(
            inflater, container,false)

        binding = addClientSuccessBinding

        return addClientSuccessBinding.root
    }
}