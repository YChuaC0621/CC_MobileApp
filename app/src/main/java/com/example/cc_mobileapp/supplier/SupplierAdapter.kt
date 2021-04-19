package com.example.cc_mobileapp.supplier

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.supplier.SupplierAdapter
import com.example.cc_mobileapp.supplier.SupplierRecycleViewClickListener
import com.example.cc_mobileapp.model.Supplier
import kotlinx.android.synthetic.main.supplier_display_item.view.*

class SupplierAdapter:RecyclerView.Adapter<SupplierAdapter.SupplierViewModel>(){

    //data declaration
    private var sup = mutableListOf<Supplier>()
    var listener: SupplierRecycleViewClickListener? = null

    //bind view model to view
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = SupplierViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.supplier_display_item, parent, false)
    )

    override fun getItemCount() = sup.size

    //set the value for each recycle view
    override fun onBindViewHolder(holder: SupplierViewModel, position: Int) {
        Log.d("Check", "adapter bind view holder")
        holder.view.txtSupName.text = sup[position].supName
        holder.view.txtCmpName.text = sup[position].supCmpName
        holder.view.btn_supEdit.setOnClickListener { listener?.onRecycleViewItemClicked(it, sup[position])}
    }

    fun setSuppliers(supplier: List<Supplier>){
        Log.d("Check", "supplier set: $supplier")
        this.sup = supplier as MutableList<Supplier>
        notifyDataSetChanged()
    }

    //add to database
    fun addSupplier(supplier: Supplier) {
        Log.d("Check", "real time add supplier $supplier")
        if (!sup.contains(supplier)) {
            sup.add(supplier)
        }else{  // delete supplier
            val supUpdateIndex = sup.indexOf(supplier)
            if(!supplier.supStatus){
                sup.removeAt(supUpdateIndex)
            }else{ // for update client
                sup[supUpdateIndex] = supplier
            }
        }
        notifyDataSetChanged()
    }


    class SupplierViewModel(val view: View) : RecyclerView.ViewHolder(view)
}
