package com.cardr.obdiqandroidsdkapp

import a.h
import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.androidisland.ezpermission.EzPermission
import com.cardr.cardrandroidsdk.ConnectionListner
import com.cardr.cardrandroidsdk.ConnectionManager
import com.cardr.cardrandroidsdk.DTCResponse
import com.cardr.cardrandroidsdk.DTCResponseModel
import com.cardr.obdiqandroidsdk.VehicleEntries
import com.cardr.obdiqandroidsdkapp.ui.theme.CarDrAndroidSDKTheme
import com.repairclub.repaircludsdk.models.DeviceItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class SampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarDrAndroidSDKTheme {

                val context = LocalContext.current

                var uiState by remember {
                    mutableStateOf(VehicleUiState())
                }

                // Call permissions + SDK only once
                LaunchedEffect(Unit) {
                    proceedWithPermissionsCheck(
                        context = context,
                        onVinFetched = { vin ->
                            uiState = uiState.copy(vin = vin)
                        },
                        onVehicleInfoFetched = { info ->
                            uiState = uiState.copy(vehicleInfo = info)
                        },
                        onScanStarted = {
                            uiState = uiState.copy(isLoading = true)
                        },
                        onDtcReceived = { dtcList ->
                            uiState = uiState.copy(
                                dtcList = dtcList,
                                isLoading = false
                            )
                        }
                    )
                }


                MainScreen(uiState)
            }
        }
    }
}

@Composable
fun MainScreen(uiState: VehicleUiState) {

    Column(
        modifier = Modifier
            .fillMaxSize()

            .padding(horizontal = 16.dp, vertical = 40.dp)
    ) {

        Text(
            text = "VIN: ${uiState.vin.ifEmpty { "--" }}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Vehicle Info: ${uiState.vehicleInfo.ifEmpty { "--" }}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "DTC Codes",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Loader
        if (uiState.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Scanning vehicle, please wait...")
        }
        // 🔹 Result
        else if (uiState.dtcList.isEmpty()) {
            Text("No DTC codes received")
        } else {
            LazyColumn {
                items(uiState.dtcList) { module ->
                    DtcModuleItem(module)
                }
            }
        }
    }
}

@Composable
fun DtcModuleItem(module: DTCResponseModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // 🔹 Module Name
            Text(
                text = "Module: ${module.moduleName}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (module.dtcCodeArray.isEmpty()) {
                Text("No DTCs in this module")
            } else {
                module.dtcCodeArray.forEach { dtc ->
                    DtcItem(dtc)
                }
            }
        }
    }
}

@Composable
fun DtcItem(dtc: DTCResponse) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = "Code: ${dtc.dtcErrorCode ?: "--"}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Status: ${dtc.status ?: "--"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Description: ${dtc.desc ?: "--"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun proceedWithPermissionsCheck(
    context: Context,
    onVinFetched: (String) -> Unit,
    onVehicleInfoFetched: (String) -> Unit,
    onScanStarted: () -> Unit,
    onDtcReceived: (List<DTCResponseModel>) -> Unit
){

    var vin = ""
    val bluetoothPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADVERTISE,
        )
    } else emptyArray()

    EzPermission.with(context)
        .permissions(*bluetoothPermissions)
        .request { granted, _, _ ->

            if (granted.size == bluetoothPermissions.size) {

                val cnn = ConnectionManager(context)
                // CARDR-58748
                //IAAf943bea2
                cnn.initialize("CARDR-58748",false,context, object : ConnectionListner {

                    override fun didFetchVehicalInfo(vehicleEntry: VehicleEntries) {
                        onVinFetched(vehicleEntry.VIN)
                        vin = vehicleEntry.VIN
                        onVehicleInfoFetched(
                            "${vehicleEntry.make} ${vehicleEntry.model} ${vehicleEntry.year}"
                        )
                    }

                    override fun didReceivedCode(model: List<DTCResponseModel>?) {
                        model?.let {
                            onDtcReceived(it)

                            CoroutineScope(Dispatchers.Main).launch {
                                delay(2000)
                                cnn.getRepairCostSummary(vin, it) { _, _ -> }
                            }
                        }
                    }

                    override fun isReadyForScan(status: Boolean, isGenric: Boolean) {
                        if (status) {
                            cnn.stopTroubleCodeScan()
                            cnn.startScan()
                        }
                    }

                    override fun didDevicesFetch(foundedDevices: List<DeviceItem>?) {}
                    override fun didCheckScanStatus(status: String) {}
                    override fun didFetchMil(mil: Boolean) {}
                    override fun didUpdateProgress(progressStatus: String, percent: String) {
                        Log.d("TAG", "didUpdateProgress: $percent")
                    }
                    override fun didReceivedRepairCost(jsonString: String) {}
                    override fun didScanForDevice(startScan: Boolean) {
                        if (startScan){ cnn.scanForDevice()
                            onScanStarted()}
                    }
                    override fun didReadyForRepairInfo(isReady: Boolean) {}
                    override fun didReceiveRepairCost(result: Map<String, Any>?) {}
                })
            }
        }
}
