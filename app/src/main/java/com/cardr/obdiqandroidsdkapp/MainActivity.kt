package com.cardr.obdiqandroidsdkapp

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.androidisland.ezpermission.EzPermission
import com.cardr.cardrandroidsdk.ConnectionListner
import com.cardr.cardrandroidsdk.ConnectionManager
import com.cardr.cardrandroidsdk.DTCResponseModel
import com.cardr.obdiqandroidsdk.VehicleEntries
import com.cardr.obdiqandroidsdkapp.ui.theme.CarDrAndroidSDKTheme
import com.repairclub.repaircludsdk.models.DeviceItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        proceedWithPermissionsCheck(this)

        setContent {
            CarDrAndroidSDKTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
                proceedWithPermissionsCheck(LocalContext.current)
            }
        }
    }
}

private fun proceedWithPermissionsCheck(
    context: Context
) {
    val bluetoothPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADVERTISE,
        )
    } else {
        arrayOf()
    }

    EzPermission.with(context)
        .permissions(*bluetoothPermissions)
        .request { granted, denied, permanentlyDenied ->
            CoroutineScope(Dispatchers.Main).launch {
                if (granted.size == bluetoothPermissions.size) {
                    var vin = ""
                    val cnn = ConnectionManager(context)
                    cnn.initialize(context,object : ConnectionListner{
                        override fun didDevicesFetch(foundedDevices: List<DeviceItem>?) {
                            Log.e("didDevicesFetch",foundedDevices.toString())

                        }

                        override fun didCheckScanStatus(status: String) {
                            Log.e("didCheckScanStatus",status)
                        }

                        override fun didFetchVehicalInfo(vehicleEntry: VehicleEntries) {
                            Log.e("didCheckScanStatus","vehicleEntry"+vehicleEntry)
                            vin = vehicleEntry.VIN

                        }

                        override fun didFetchMil(mil: Boolean) {
                        }

                        override fun isReadyForScan(status: Boolean, isGenric: Boolean) {
                            Log.e("isReadyForScan",status.toString())
                            cnn.stopTroubleCodeScan()
                            cnn.startScan()
                        }


                        override fun didUpdateProgress(progressStatus: String, percent: String) {
                            Log.e("Progress == ${percent}",progressStatus )
                        }

                        override fun didReceivedCode(model: List<DTCResponseModel>?) {
                            Log.e("didReceivedCode", model.toString())
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(2000)
//                                cnn.getEmissionMonitors {
////
////
//                                }
                                model?.let {
                                    cnn.getRepairCostSummary(vin,it,{status,json->

                                    })
                                }
//
                            }
                        }

                        override fun didReceivedRepairCost(jsonString: String) {
                        }

                        override fun didScanForDevice(startScan: Boolean) {
                            if(startScan){
                                cnn.scanForDevice()
                            }
                        }

                        override fun didReadyForRepairInfo(isReady: Boolean) {
                        }

                        override fun didReceiveRepairCost(result: Map<String, Any>?) {
                        }

                    })
                }
                if (denied.isNotEmpty() || permanentlyDenied.isNotEmpty()) {

                }
            }
        }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
}

@Composable
fun GreetingPreview() {

}