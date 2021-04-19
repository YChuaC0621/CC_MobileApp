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

    //data declaration
    private var prod_report = mutableListOf<Product>()
    private val dbStockDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKDETAIL)

    var listener: ProdReportRecycleViewClickListener? = null

    //bind one of the recycle view item with the view model
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = ReportViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_report_display_item, parent, false)
    )

    override fun getItemCount() = prod_report.size

    //receive product details and set to each recycle view item
    fun setProdReportDetails(prod: List<Product>) {
        Log.d("Check", "product report set: $prod")
        this.prod_report = prod as MutableList<Product>
        notifyDataSetChanged()
    }


    class ReportViewModel(val view: View) : RecyclerView.ViewHolder(view) {

    }


    //assign value for each recycle view item
    override fun onBindViewHolder(holder: ReportViewModel, position: Int) {

        Log.d("Check", "adapter bind view holder")

        //coroutine
        GlobalScope.launch {
            dbStockDetail.get().addOnSuccessListener {
                var stockDetail = mutableListOf<StockDetail>()
                Log.d("Check", "fetch stock details")
                var rackResults = ""
                var qtyResults = ""
                if (it.exists()) {
                    it.children.forEach { it ->
                        val stockReport = StockDetail()
                        val stock: StockDetail? = it.getValue(StockDetail::class.java)
                        //check the product is stored under which racks and its stored quantity
                        if (stock?.stockDetailProdBarcode.equals(prod_report[position].prodBarcode)) {
                            stock?.stockTypeId = it.key
                            Log.d("Check", "fetch stock detail Info ${stock?.stockDetailRackId}")
                            rackResults += (stock?.stockDetailRackId + "\n")
                            qtyResults += (stock?.stockDetailQty.toString() + "\n")

                        }
                    }
                }
                if(rackResults.equals("") && qtyResults.equals("")){
                    rackResults = "Empty"
                    qtyResults = "Empty"
                }
                //set the textfield
                holder.view.txtStockInInfo.setText(rackResults)
                holder.view.txtStockInInfo2.setText(qtyResults)
                holder.view.txtProdName.text = prod_report[position].prodName
                holder.view.txtProdBarcode.text = prod_report[position].prodBarcode
                holder.view.txtProdSupName.text = prod_report[position].supplierName
            }

        }

    }
}