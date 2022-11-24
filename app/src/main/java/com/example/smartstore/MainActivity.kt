package com.example.smartstore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstore.Screen.MainApp
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.example.smartstore.viewmodel.MainViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainActivity_싸피"
class MainActivity : ComponentActivity() {
    private var mLocationProviderClient:FusedLocationProviderClient? = null
    private lateinit var locationCallback:LocationCallback
    private val locationInterval = 1000L
    private val locationFastestInterval = 500L
    private val locationMaxWaitTime = 10000L
    private lateinit var mainViewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContent {
            SmartStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val owner = LocalViewModelStoreOwner.current
                    val user = ApplicationClass.sharedPreferencesUtil.getUser()
                    owner?.let {
                        val viewModel:MainViewModel = viewModel(
                            it,
                            "MainViewModel",
                            MainViewModelFactory(
//                                LocalContext.current.applicationContext as Application
                            )
                        )
                        mainViewModel = viewModel
                        MainApp(viewModel, user)
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting()
        }
        if(!checkLocationPermission()){
            checkLocationInfo()
        }
    }

    // 위치 정보 서비스 활성화 여부 판단
    fun checkLocationServicesStatus():Boolean{
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    // 위치정보 활성화되지 않은 경우 안내 Dialog
    fun showDialogForLocationServiceSetting(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle(R.string.dialog_title_location_service_disable)
            setMessage(R.string.dialog_message_location_service_disable)
            setCancelable(true)
            setPositiveButton("설정"){_,_ ->
                val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requestActivity.launch(callGPSSettingIntent)
            }
            setNegativeButton("취소"){dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    // 위치정보 권한 확인
    fun checkLocationPermission():Boolean{
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }


    // 위치 권한 확인
    fun checkLocationInfo(){
        if(!checkLocationPermission()){
            val permissionListener = object: PermissionListener {
                override fun onPermissionGranted() {
                    mainViewModel.needRequest = true
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    mainViewModel.needRequest = false
                    Toast.makeText(this@MainActivity, getString(R.string.permission_location_denied_msg) + deniedPermissions.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }

            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage(R.string.permission_location_denied)
                .setPermissions(Manifest.permission.WRITE_CONTACTS)
                .check()
        }else if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting()
        }
    }

    // 위치정보 페이지 다녀온 이후
    val requestActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            if(checkLocationServicesStatus()){
                mainViewModel.needRequest = true
            }else{
                Toast.makeText(this, R.string.location_service_off, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationInterval).setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(locationFastestInterval)
            .setMaxUpdateDelayMillis(locationMaxWaitTime)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult?.let {
                    for(location in it.locations) {
                        Log.d("Location", "${location.latitude} , ${location.longitude}")
                    }
                }
                mainViewModel.setCurrentLocation(locationResult.locations[0].latitude, locationResult.locations[0].longitude)
                mainViewModel.getDistanceByCafe()
            }
        }
        mLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback!!, Looper.myLooper())
    }
}