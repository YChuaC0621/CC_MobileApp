package com.example.cc_mobileapp.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.client.ClientAdapter
import com.example.cc_mobileapp.client.ClientViewModel
import com.example.cc_mobileapp.client.EditClientFragment
import com.example.cc_mobileapp.databinding.ActivityClientAddBinding.inflate
import com.example.cc_mobileapp.databinding.ActivityClientUpdateBinding.inflate
import com.example.cc_mobileapp.model.Product
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_client.*
import kotlinx.android.synthetic.main.fragment_product.*

class ProductFragment : Fragment(), ProductRecyclerViewClickListener {

    private lateinit var viewModel: ProductViewModel
    private val adapter = ProductAdapter()
    private var binding: ProductFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         //Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@ProductFragment).get(ProductViewModel::class.java)
        return inflater.inflate(R.layout.fragment_product, container, false)

//        val fragmentBinding = ProductFragment.inflate(inflater, container, false)
//        binding = fragmentBinding
//        return fragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.listener = this
        recycler_view_product.adapter = adapter

        viewModel.fetchProduct()
        viewModel.getRealtimeUpdates()

        viewModel.products.observe(viewLifecycleOwner, Observer{
            adapter.setProducts(it)
        })

        viewModel.product.observe(viewLifecycleOwner, Observer{
            adapter.addProduct(it)
        })

        btn_productAdd.setOnClickListener {
//            AddProductDialogFragment()
//                .show(childFragmentManager,"")
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddProductDialogFragment())
            transaction.addToBackStack("addBarcodeFragment")
            transaction.commit()
        }
    }

    override fun onRecyclerViewItemClicked(view: View, product: Product) {
        val currentView = (requireView().parent as ViewGroup).id
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(currentView, EditProductFragment(product))
        transaction.addToBackStack("editBarcodeFragment")
        transaction.commit()
    }

}