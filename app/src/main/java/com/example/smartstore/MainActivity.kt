package com.example.smartstore

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstore.Screen.MainApp
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.smartstore.dto.Token
import com.ssafy.smartstore.util.RetrofitUtil
import com.ssafy.smartstore.util.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.google.android.gms.location.*

private const val TAG = "MainActivity_싸피"
class MainActivity : ComponentActivity() {
    private var mLocationProviderClient:FusedLocationProviderClient? = null
    private lateinit var locationCallback:LocationCallback
    private val locationInterval = 1000L
    private val locationFastestInterval = 500L
    private val locationMaxWaitTime = 10000L
    private lateinit var mainViewModel:MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")

        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
            if(task.result != null){
                CoroutineScope(Dispatchers.IO).launch {
                    val temp = Token(0,SharedPreferencesUtil(applicationContext).getUser().id,task.result!!)
                    RetrofitUtil.tokenService.uploadToken(temp)
                }
            }
        })
        createNotificationChannel(channel_id, "ssafy")

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
                        )
                        mainViewModel = viewModel
                        MainApp(viewModel, user)
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Notification 수신을 위한 체널 추가
    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager
                = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object{
        // Notification Channel ID
        const val channel_id = "ssafy_channel"
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