package com.example.smartstore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartstore.ApplicationClass
import com.ssafy.smartstore.dto.OrderDetail
import com.ssafy.smartstore.dto.Product
import com.ssafy.smartstore.dto.UserOrderDetail
import com.ssafy.smartstore.response.LatestOrderResponse
import com.ssafy.smartstore.response.OrderDetailResponse
import com.ssafy.smartstore.util.RetrofitUtil
import kotlinx.coroutines.*
import java.io.IOException

const val HOME = "home"
const val ORDER = "order"
const val MYPAGE = "myPage"

private const val TAG = "MainViewModel_싸피"
class MainViewModel():ViewModel() {
    val user = ApplicationClass.sharedPreferencesUtil.getUser()
    var Product:Product?=null
    var allProduct: MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    var allRecentOrder: MutableLiveData<List<LatestOrderResponse>> = MutableLiveData<List<LatestOrderResponse>>()

    init {
        getProductList()
        getRecentOrderList(user.id)
    }

    fun getProductList(){
        CoroutineScope(Dispatchers.Main).launch {
            val result = RetrofitUtil.productService.getProductList()
            Log.d("main","$result")
            if(result.isEmpty())allProduct=MutableLiveData<List<Product>>()
            else allProduct.value = result
        }
    }

    fun getRecentOrderList(id:String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = RetrofitUtil.orderService.getLastMonthOrder(id)
                Log.d("recentOrderList", "${result.body()}")

                if(result.code() == 200){
                    if(result.body() == null || result.body()!!.size == 0){
                        allRecentOrder = MutableLiveData<List<LatestOrderResponse>>()
                    }else{
                        allRecentOrder.value = makeRecentItemData(result.body()!!)
                        Log.d(TAG, "getRecentOrderList: ${allRecentOrder.value}")
                    }
                }else{
                    allRecentOrder = MutableLiveData<List<LatestOrderResponse>>()
                }
            }catch(e:IOException){
                Log.d(TAG, "getRecentOrderList: ${e.message}")   
            }
        }
    }
    
    // 최근 주문 아이템 데이터 생성하기
    fun makeRecentItemData(responseList:List<LatestOrderResponse>):List<LatestOrderResponse>{
        val totalList = mutableListOf<LatestOrderResponse>()
        var idx = 0;
        for (order in responseList){
            if(totalList.size == 0){
                order.totalPrice += order.productPrice * order.orderCnt
                totalList.add(order)
            }else{
                if(order.orderId == totalList[idx].orderId){
                    totalList[idx].orderCnt += order.orderCnt
                    totalList[idx].totalPrice += order.productPrice * order.orderCnt
                }else{
                    order.totalPrice += order.productPrice * order.orderCnt
                    totalList.add(order)
                    idx++
                }
            }
        }
        
        return totalList
    }
}