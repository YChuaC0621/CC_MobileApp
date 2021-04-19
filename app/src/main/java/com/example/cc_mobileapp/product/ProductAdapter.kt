package com.example.cc_mobileapp.product

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import kotlinx.android.synthetic.main.product_display_item.view.*

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewModel>() {

    // variable declaration
    private var products = mutableListOf<Product>()
    var listener: ProductRecyclerViewClickListener? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = ProductViewModel(
            // inflate the product_display_item layout
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_display_item, parent, false)
    )

    // get total products count
    override fun getItemCount() = products.size

    // bind the information to the user interface
    override fun onBindViewHolder(holder: ProductViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtView_productName.text = products[position].prodName
        var price = products[position].prodPrice
        holder.view.txtView_productPrice.text = String.format("%.2f",price)
        holder.view.txtView_productSupplier.text = products[position].supplierName
        holder.view.txtView_prodBarcode.text = products[position].prodBarcode
        holder.view.btn_productEdit.setOnClickListener { listener?.onRecyclerViewItemClicked(it, products[position])}
    }

    // set the products information
    fun setProducts(products: List<Product>){
        Log.d("Check", "client set: $products")
        this.products = products as MutableList<Product>
        notifyDataSetChanged()
    }

    // add product information
    fun addProduct(product: Product) {
        Log.d("Check", "real time add client $product")
        if (!products.contains(product)) {
            products.add(product)
        }else{  // delete product
            val productUpdateIndex = products.indexOf(product)
            if(product.isDeleted){
                products.removeAt(productUpdateIndex)
            }else{ // for update product
                products[productUpdateIndex] = product
            }
        }
        // get real time changes
        notifyDataSetChanged()
    }


    class ProductViewModel(val view: View) : RecyclerView.ViewHolder(view)
}