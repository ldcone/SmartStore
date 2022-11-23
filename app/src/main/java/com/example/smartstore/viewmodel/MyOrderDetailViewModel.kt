package com.example.smartstore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.smartstore.response.LatestOrderResponse
import com.ssafy.smartstore.response.OrderDetailResponse
import com.ssafy.smartstore.util.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "OrderDetail_싸피"
class MyOrderDetailViewModel(order:LatestOrderResponse?):ViewModel() {
    var orderDetailList: MutableLiveData<List<OrderDetailResponse>> = MutableLiveData<List<OrderDetailResponse>>()

    init {
        if(order != null){
            getOrderDetail(order.orderId)
        }
    }

    private fun getOrderDetail(orderId:Int){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = RetrofitUtil.orderService.getOrderDetail(orderId)
                Log.d("recentOrderList", "${result.body()}")

                if(result.code() == 200){
                    if(result.body() == null || result.body()!!.size == 0){
                        orderDetailList = MutableLiveData<List<OrderDetailResponse>>()
                    }else{
                        orderDetailList.value = result.body()!!
                        Log.d(TAG, "getOrderDetail: ${orderDetailList.value}")
                    }
                }else{
                    orderDetailList = MutableLiveData<List<OrderDetailResponse>>()
                }
            }catch(e: IOException){
                Log.d(TAG, "getOrderDetail: ${e.message}")
            }
        }
    }
}