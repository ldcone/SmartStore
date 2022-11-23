package com.example.smartstore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.smartstore.ApplicationClass
import com.example.smartstore.response.UserResponse
import com.google.gson.Gson
import com.ssafy.smartstore.dto.*
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
    var allUserInfo:MutableLiveData<HashMap<String, Any>> = MutableLiveData<HashMap<String, Any>>()
    var gradeInfo:MutableLiveData<Grade> = MutableLiveData<Grade>()
    var userInfo:MutableLiveData<UserResponse> = MutableLiveData<UserResponse>()

    init {
        getProductList()
        getRecentOrderList(user.id)
        getUserInfo(user.id)
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

    fun getUserInfo(id:String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = RetrofitUtil.userService.getInfo(id)
                Log.d("userInfo", "${id} : ${result.code()}, ${result.body()}")

                if(result.code() == 200){
                    if(result.body() == null || result.body()!!.size == 0){
                        allUserInfo = MutableLiveData<HashMap<String, Any>>()
                    }else{
                        allUserInfo.value = result.body()
                        getUserInfoData(allUserInfo.value!!)
                    }
                }else{
                    allUserInfo = MutableLiveData<HashMap<String, Any>>()
                }
            }catch(e:IOException){
                Log.d(TAG, "userInfo: ${e.message}")
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

    // 회원정보 데이터 뽑아내기
    fun getUserInfoData(userInfo:HashMap<String, Any>){
//        val orderList:List<Order> = userInfo.get("order") as List<Order>
        val grade:Grade = Gson().fromJson(userInfo.get("grade").toString(), Grade::class.java)
        Log.d(TAG, "getUserInfoData: ${grade}")
        //val user:UserResponse = Gson().fromJson(userInfo.get("user").toString(), UserResponse::class.java)
//        Log.d(TAG, "getUserInfoData: ${orderList}")
        Log.d(TAG, "getUserInfoData: ${user}")
        if(grade == null){
            gradeInfo = MutableLiveData<Grade>()
        }else{
            gradeInfo.value = grade
        }
        if(user != null){
            this.userInfo = MutableLiveData<UserResponse>()
        }else{
            this.userInfo.value = user
        }
    }
}