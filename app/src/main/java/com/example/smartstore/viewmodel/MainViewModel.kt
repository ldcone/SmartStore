package com.example.smartstore.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartstore.ApplicationClass
import com.example.smartstore.MainActivity
import com.ssafy.smartstore.dto.Product
import com.ssafy.smartstore.dto.ShoppingCart
import com.google.gson.Gson
import com.ssafy.smartstore.dto.*
import com.ssafy.smartstore.response.LatestOrderResponse
import com.ssafy.smartstore.util.RetrofitUtil
import kotlinx.coroutines.*
import java.io.IOException

const val HOME = "home"
const val ORDER = "order"
const val MYPAGE = "myPage"

private const val TAG = "MainViewModel_싸피"
class MainViewModel():ViewModel() {
    var allProduct: MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    private val _ShoppingCart=MutableLiveData<MutableList<ShoppingCart>>(mutableListOf())
    val user = ApplicationClass.sharedPreferencesUtil.getUser()
    var Product:Product?=null
    var Order:LatestOrderResponse?=null

    var allRecentOrder: MutableLiveData<List<LatestOrderResponse>> = MutableLiveData<List<LatestOrderResponse>>()

//    val _ShoppingCart = mutableStateListOf<ShoppingCart>()
//    val shoppingCart:MutableList<ShoppingCart> = _ShoppingCart
    var allUserInfo:MutableLiveData<HashMap<String, Any>> = MutableLiveData<HashMap<String, Any>>()
    var gradeInfo:Grade? = null
    var userInfo:User? = null

    init {
        getProductList()
        getRecentOrderList(user.id)
//        ShoppingCart.value = mutableListOf()
        getUserInfo(user.id)

    }
    private fun getProductList(){
        CoroutineScope(Dispatchers.Main).launch {
            val result = RetrofitUtil.productService.getProductList()
            Log.d("main","$result")
            if(result.isEmpty())allProduct=MutableLiveData<List<Product>>()
            else allProduct.value = result
        }
    }
    fun getShoppingCart():MutableLiveData<MutableList<ShoppingCart>>{
        val temp = _ShoppingCart
        return temp
    }
    fun addShop(item:ShoppingCart){
        _ShoppingCart.value = _ShoppingCart.value?.plus(listOf(item)) as MutableList<ShoppingCart>?
    }
    fun removeShop(index:ShoppingCart){
        _ShoppingCart.value = _ShoppingCart.value?.filter { it != index }?.toMutableList()
        getUserInfo(user.id)
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
        Log.d(TAG, "userInfo: ${userInfo}")

        val grade:Grade = Gson().fromJson(userInfo.get("grade").toString(), Grade::class.java)

        var strUser = userInfo.get("user").toString()
        strUser = strUser.replace("=","=\"")
        strUser = strUser.replace(",","\",")
        strUser = strUser.replace("=\"[","=[")

        val user:User = Gson().fromJson(strUser, User::class.java)

        gradeInfo = grade
        this.userInfo = user
    }

    fun completeOrder(order: Order){
        CoroutineScope(Dispatchers.IO).launch {
            val orderId =RetrofitUtil.orderService.makeOrder(order)
            Log.d("view_Order","$orderId")
        }
    }
}