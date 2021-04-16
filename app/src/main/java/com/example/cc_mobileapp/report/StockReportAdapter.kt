package com.example.cc_mobileapp.report

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cc_mobileapp.Constant
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.stock_report_display_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class StockReportAdapter(): RecyclerView.Adapter<StockReportAdapter.ReportViewModel>() {

    private var prod_report = mutableListOf<Product>()
    private var stockIn_report = mutableListOf<StockIn>()
    private var stockOut_report = mutableListOf<StockOut>()
    private var stockInDetail_report = mutableListOf<StockDetail>()
    private var stockOutDetail_report = mutableListOf<StockOutDetail>()
    private lateinit var startDate :String
    private lateinit var endDate: String


    private val dbStockIn = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKIN)
    private val dbStockOut = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKOUT)
    private val dbStockOutDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_STOCKOUTDETAIL)
    private val dbStockInDetail = FirebaseDatabase.getInstance().getReference(Constant.NODE_PERM_STOCKINDETAIL)



    var listener: ProdReportRecycleViewClickListener? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = ReportViewModel(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.stock_report_display_item, parent, false)
    )

    override fun getItemCount() = prod_report.size

    fun setStockReportDetails(prod: List<Product>, startDate: String, endDate: String,
                              stockIn: List<StockIn>, stockOut: List<StockOut>,
                              stockInDetail: List<StockDetail>, stockOutDetail: List<StockOutDetail>) {
        Log.d("Check", "product report set: $prod")
        Log.d("Check", "start date: $startDate")
        Log.d("Check", "end date: $endDate")
        Log.d("Check", "stockIn report set: $stockIn")
        Log.d("Check", "stockOut report set: $stockOut")
        Log.d("Check", "stockInDetail report set: $stockInDetail")
        Log.d("Check", "stockOutDetail report set: $stockOutDetail")
        this.prod_report = prod as MutableList<Product>
        this.stockIn_report = stockIn as MutableList<StockIn>
        this.stockOut_report = stockOut as MutableList<StockOut>
        this.stockInDetail_report = stockInDetail as MutableList<StockDetail>
        this.stockOutDetail_report = stockOutDetail as MutableList<StockOutDetail>
        this.startDate = startDate
        this.endDate = endDate

        notifyDataSetChanged()
    }


    class ReportViewModel(val view: View) : RecyclerView.ViewHolder(view) {

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReportViewModel, position: Int) {

        Log.d("Check", "adapter bind view holder")

            var stockin_results = ""
            for(child in stockInDetail_report)
            {
                if(child.stockDetailProdBarcode.equals(prod_report[position].prodBarcode))
                {
                    for (childStockIn in stockIn_report)
                    {
                        if(childStockIn.stockInId.equals(child.stockTypeId))
                        {
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
                            Log.d("Check", "start date: $startDate")
                            Log.d("Check", "end date: $endDate")
                            val date_start: LocalDate = LocalDate.parse(startDate, formatter)
                            val date_end: LocalDate = LocalDate.parse(endDate, formatter)
                            val strDate: LocalDate = LocalDate.parse(childStockIn.stockInDate, formatter)
                            Log.d("Check", "start date pattern: $strDate")
                            if ((strDate.isAfter(date_start) || strDate.isEqual(date_start)) &&  strDate.isBefore(date_end)) {
                                stockin_results += childStockIn.stockInDate + " : " + child.stockDetailQty.toString() + "\n"
                            }
                        }
                    }
                }
            }

        var stockout_results = ""
        for(child in stockOutDetail_report)
        {
            if(child.stockOutDetailProdBarcode.equals(prod_report[position].prodBarcode))
            {
                for (childStockOut in stockOut_report)
                {
                    if(childStockOut.stockOutId.equals(child.stockTypeId))
                    {
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
                        Log.d("Check", "start date: $startDate")
                        Log.d("Check", "end date: $endDate")
                        val date_start: LocalDate = LocalDate.parse(startDate, formatter)
                        val date_end: LocalDate = LocalDate.parse(endDate, formatter)
                        val strDate: LocalDate = LocalDate.parse(childStockOut.stockOutDate, formatter)
                        Log.d("Check", "start date pattern: $strDate")
                        if ((strDate.isAfter(date_start) || strDate.isEqual(date_start)) &&  strDate.isBefore(date_end)) {
                            stockout_results += childStockOut.stockOutDate + " : " + child.stockOutDetailQty.toString() + "\n"
                        }
                    }
                }
            }
        }

            if(stockin_results == "")
            {
                holder.view.txtStockInInfo.setText("No result")
            }
            else
            {
                holder.view.txtStockInInfo.setText(stockin_results)
            }
            if(stockout_results == "")
            {
                holder.view.txtStockOutInfo.setText("No result")
            }
            else
            {
                holder.view.txtStockOutInfo.setText(stockout_results)
            }
            holder.view.txtQtyNum.setText(prod_report[position].prodQty.toString())
            holder.view.txtProdName.text = prod_report[position].prodName
            holder.view.txtProdBarcode.text = prod_report[position].prodBarcode
            holder.view.txtProdSupName.text = prod_report[position].supplierName




    }
}