package com.example.cc_mobileapp.report

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.product_report_display_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductReportAdapter(): RecyclerView.Adapter<ProductReportAdapter.ReportViewModel>() {

    private var prod_report = mutableListOf<Product>()
    private val dbRack = FirebaseDatabase.getInstance().getReference(Constant.NODE_RACK)

    var listener: ProdReportRecycleViewClickListener? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = ReportViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_report_display_item, parent, false)
    )

    override fun getItemCount() = prod_report.size

    fun setProdReportDetails(prod: List<Product>) {
        Log.d("Check", "product report set: $prod")
        this.prod_report = prod as MutableList<Product>
        notifyDataSetChanged()
    }


    class ReportViewModel(val view: View) : RecyclerView.ViewHolder(view) {

    }


    override fun onBindViewHolder(holder: ReportViewModel, position: Int) {

        Log.d("Check", "adapter bind view holder")

        GlobalScope.launch {
            dbRack.get().addOnSuccessListener {
                var racks = mutableListOf<Rack>()
                Log.d("Check", "fetch racks")
                var rackResults = ""
                if (it.exists()) {
                    it.children.forEach { it ->
                        val rackReport = Rack()
                        val rack: Rack? = it.getValue(Rack::class.java)
                        if (rack?.prodId.equals(prod_report[position].prodBarcode)) {
                            rack?.rackId = it.key
                            Log.d("Check", "fetch racks Info ${rack?.rackName}")
                            rackResults += (rack?.rackName + "\t \t \t \t Current Quantity : " + rack?.currentQty + "\n")

                        }
                    }
                }
                if(rackResults.equals("")){
                    rackResults = "No rack is storing this product"
                }
                holder.view.txtStockInInfo.setText(rackResults)
                holder.view.txtProdName.text = prod_report[position].prodName
                holder.view.txtProdBarcode.text = prod_report[position].prodBarcode
                holder.view.txtProdSupName.text = prod_report[position].supplierName
            }

        }

    }
}