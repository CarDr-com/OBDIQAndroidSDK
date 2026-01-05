package com.cardr.obdiqandroidsdkapp

import com.cardr.cardrandroidsdk.DTCResponseModel

data class VehicleUiState(
    val vin: String = "",
    val vehicleInfo: String = "",
    val dtcList: List<DTCResponseModel> = emptyList(),
    val isLoading: Boolean = true
)
