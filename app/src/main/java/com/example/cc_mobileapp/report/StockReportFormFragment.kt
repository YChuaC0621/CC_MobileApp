package com.example.cc_mobileapp.report

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cc_mobileapp.R
import kotlinx.android.synthetic.main.fragment_edit_staff.*
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.fragment_sitemap.*
import kotlinx.android.synthetic.main.fragment_stockreport_form.*
import java.util.*


class StockReportFormFragment : Fragment() {
    //data declaration
    private lateinit var viewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //bind view model to view
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this@StockReportFormFragment).get(ReportViewModel::class.java)

        return inflater.inflate(R.layout.fragment_stockreport_form, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("Check", "stock report fragment on activity created")

        super.onActivityCreated(savedInstanceState)

        val date: EditText

        //make the date entered in only in dd/MM/yyyy format
        date = editTxt_startDate
        date.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal = Calendar.getInstance()
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    var clean = s.toString().replace("[^\\d.]".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]".toRegex(), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()
                        if (mon > 12) mon = 12
                        cal[Calendar.MONTH] = mon - 1
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal[Calendar.YEAR] = year
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }
                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8))
                    sel = if (sel < 0) 0 else sel
                    current = clean
                    date.setText(current)
                    date.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        val enddate: EditText

        enddate = editTxt_endDate
        enddate.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal = Calendar.getInstance()
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    var clean = s.toString().replace("[^\\d.]".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]".toRegex(), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()
                        if (mon > 12) mon = 12
                        cal[Calendar.MONTH] = mon - 1
                        year = if (year < 2010) 2010 else if (year > 2100) 2100 else year
                        cal[Calendar.YEAR] = year
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }
                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8))
                    sel = if (sel < 0) 0 else sel
                    current = clean
                    enddate.setText(current)
                    enddate.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        //navigate to display stock report
        btn_continue2.setOnClickListener{
            Log.d("Check", "start date string: ${editTxt_startDate.text}")
            Log.d("Check", "end date string: ${editTxt_endDate.text}")
            if(editTxt_startDate.text.isNullOrBlank() || !checkRegexdate(editTxt_startDate.text.toString())){
                txtInputLayout_startDate.error = getString(R.string.date_invalid_msg)
                return@setOnClickListener
            }
            else if(editTxt_endDate.text.isNullOrBlank() || !checkRegexdate(editTxt_endDate.text.toString()))
            {
                txtInputLayout_endDate.error = getString(R.string.date_invalid_msg)
                return@setOnClickListener
            }
            else
            {
                val startDateString = editTxt_startDate.text.toString()
                val endDateString = editTxt_endDate.text.toString()
                Log.d("Check", "start date: $startDateString")
                Log.d("Check", "end date: $endDateString")
                val currentView = (requireView().parent as ViewGroup).id
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(currentView, StockReportFragment(startDateString, endDateString))
                transaction.addToBackStack("fragmentA")
                transaction.commit()
            }

        }
    }

    //check the input is only integer
    private fun checkRegexdate(Date: String): Boolean {
        var Date: String = Date
        var regex: Regex = Regex(pattern="^\\d{2}/\\d{2}/\\d{4}\$")
        var result = regex.matches(input = Date)
        Log.d("Check", "end date string: ${result}")
        return result
    }
}