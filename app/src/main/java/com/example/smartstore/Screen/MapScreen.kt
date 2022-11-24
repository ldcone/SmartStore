package com.example.smartstore.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstore.ui.theme.CaffeMenuBack
import com.example.smartstore.ui.theme.CaffePointRed
import com.example.smartstore.viewmodel.MainViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    viewModel: MainViewModel
){
    // bottom navigation bar shown
    viewModel.setVisibleBottomNav(false)

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text("Map",
                style= TextStyle(
                    fontSize = 28.sp,
                    color = CaffePointRed,
                    fontWeight = FontWeight.Bold)
            )
        }
        GoogleMapScreen(viewModel)
    }
}

@Composable
fun GoogleMapScreen(viewModel: MainViewModel){
    val cafeLocation = viewModel.cafeLocation
    val cafe = LatLng(cafeLocation.latitude, cafeLocation.longitude)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(cafe, 17.0f)
    }
    Box(
        Modifier.background(CaffeMenuBack)
    ){
        GoogleMap(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true
            ),

        ){
            Marker(
                state = MarkerState(position = cafe),
                title = "SmartStore",
                snippet = "SmartStore 구미점"
            )
        }
    }

}


@Preview
@Composable
fun MapPreView(){
    MapScreen(viewModel())
}