package com.example.interprac.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MapViewModel: ViewModel() {

    var lastLat by mutableStateOf<Double?>(null)
        private set
    var lastLon by mutableStateOf<Double?>(null)
        private set

    fun setLastLocation(lat: Double, long: Double){
        lastLat = lat
        lastLon = long

    }

}