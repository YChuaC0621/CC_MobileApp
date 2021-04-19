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
import com.example.cc_mobileapp.model.Product
import kotlinx.android.synthetic.main.fragment_add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_client.*
import kotlinx.android.synthetic.main.fragment_product.*

class ProductFragment : Fragment(), ProductRecyclerViewClickListener {

    // variable
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
        // access the information from product view model
        viewModel = ViewModelProvider(this@ProductFragment).get(ProductViewModel::class.java)
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // to be use for the listener on the button for each item on the listener
        adapter.listener = this
        recycler_view_product.adapter = adapter

        // get the products information
        viewModel.fetchProduct()
        // get real time updates
        viewModel.getRealtimeUpdates()

        // observe the changes in products, if have changes, set the products information and make changes on UI
        viewModel.products.observe(viewLifecycleOwner, Observer{
            adapter.setProducts(it)
        })

        // observe the changes in product, if have changes, set the products information and make changes on UI
        viewModel.product.observe(viewLifecycleOwner, Observer{
            adapter.addProduct(it)
        })

        // get the particular recycler view information, pass to the next edit fragment which require the information
        btn_productAdd.setOnClickListener {
            val currentView = (requireView().parent as ViewGroup).id
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(currentView, AddProductDialogFragment())
            transaction.addToBackStack("addProductFragment")
            transaction.commit()
        }
    }

    // check on which recycler view's item has been clicked
    override fun onRecyclerViewItemClicked(view: View, product: Product) {
        val currentView = (requireView().parent as ViewGroup).id
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(currentView, EditProductFragment(product))
        transaction.addToBackStack("editProductFragment")
        transaction.commit()
    }

}