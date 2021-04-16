package com.example.cc_mobileapp.product

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.*
import com.example.cc_mobileapp.Constant.CAMERA_REQUEST_CODE
import com.example.cc_mobileapp.R
import kotlinx.android.synthetic.main.fragment_scan_barcode.*

class ScanBarcodeFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private val sharedBarcodeViewModel: ProductBarcodeViewModel by activityViewModels()

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

        sharedBarcodeViewModel.scannedCode.observe(viewLifecycleOwner, Observer {
            if(!sharedBarcodeViewModel.scannedCode.value.isNullOrEmpty())
            {
                Toast.makeText(requireContext(), getCallerFragment(), Toast.LENGTH_SHORT).show()
                if(getCallerFragment().equals("addProductFragment")){
                    requireActivity().supportFragmentManager.popBackStack("addBarcode", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else{
                    requireActivity().supportFragmentManager.popBackStack("editBarcode", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        })
    }

    private fun codeScanner(){
        codeScanner = CodeScanner(requireContext(), scanner_view)

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

                        sharedBarcodeViewModel.productBarcode(it.text)
                    }
                }catch (e: Exception){
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
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        // gv the result from makeRequest()
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "Camera permission are required to scan barcode!", Toast.LENGTH_SHORT).show()
                } else {
                    // successful request
                }
            }
        }
    }

    // get previous fragment name
     fun getCallerFragment(): String? {
        val fm: FragmentManager = requireActivity().supportFragmentManager
        val count: Int = requireActivity().supportFragmentManager.backStackEntryCount
        return fm.getBackStackEntryAt(count - 2).name
    }
}