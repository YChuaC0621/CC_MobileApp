package com.example.cc_mobileapp.product

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.cc_mobileapp.Constant.CAMERA_REQUEST_CODE
import com.example.cc_mobileapp.R
import com.example.cc_mobileapp.model.Product
import kotlinx.android.synthetic.main.fragment_scan_barcode.*
import androidx.fragment.app.activityViewModels
import com.example.cc_mobileapp.product.ProductViewModel

class ScanBarcodeFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private val viewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_barcode, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupPermission()
        codeScanner()

        viewModel.scannedCode.observe(viewLifecycleOwner, Observer{
            requireActivity().supportFragmentManager.popBackStack("addProdFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        })
    }

    private fun codeScanner(){
        codeScanner = CodeScanner(requireContext(),scanner_view)

        codeScanner.apply{
            camera = CodeScanner.CAMERA_BACK
            // get all camera format
            formats = CodeScanner.ALL_FORMATS

            // auto focus on fixed interval
            autoFocusMode = AutoFocusMode.SAFE

            // continuously scan & try to find barcode
            scanMode = ScanMode.CONTINUOUS

            // set auto focus to true
            isAutoFocusEnabled = true

            // prevent when start the flash is ald on
            isFlashEnabled = false

            //call backs
            // 1. tell you what happen when decode something
            decodeCallback = DecodeCallback {
                try{
                    requireActivity().runOnUiThread {   // it = result
                        Log.e("Main", "require context successful $it")
                        viewModel.productBarcode(it.text)
                        Toast.makeText(requireContext(), it.text, Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    Toast.makeText(requireContext(), "Error on decode call back", Toast.LENGTH_SHORT).show()
                    Log.e("Main", "required context error $it")

                }
            }

            errorCallback = ErrorCallback {
                try {
                    requireActivity().runOnUiThread {   // it = result
                        Log.e("Main", "Camera innitialization error: ${it.message}")
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error on error call back", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // when user click, start new scanning
            scanner_view.setOnClickListener{
                codeScanner.startPreview()
            }
        }
    }

    // when leave the app & come back, scan new code
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        // avoid memory leaks
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        // check array of permission
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // gv the result from makeRequest()
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(requireContext(), "Camera permission are required to scan barcode!", Toast.LENGTH_SHORT).show()
                }else{
                    // successful request
                }
            }
        }
    }
}