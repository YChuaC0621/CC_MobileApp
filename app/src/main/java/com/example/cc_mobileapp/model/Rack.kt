package com.example.cc_mobileapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.Exclude

public class Rack (
    @get:Exclude
    var rackId: String? = null,
    var rackName: String? = null,
    var startLot: String? = null,
    var endLot: String? = null,
    var row_num: Int? = null
)
{

}