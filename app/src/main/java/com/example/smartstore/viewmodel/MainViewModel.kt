package com.example.smartstore.viewmodel

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.smartstore.ApplicationClass
import com.example.smartstore.MainActivity
import com.ssafy.smartstore.dto.Product
import com.ssafy.smartstore.dto.ShoppingCart
import com.google.gson.Gson
import com.ssafy.smartstore.dto.*
import com.ssafy.smartstore.response.LatestOrderResponse
import com.ssafy.smartstore.response.MenuDetailWithCommentResponse
import com.ssafy.smartstore.response.OrderDetailResponse
import com.ssafy.smartstore.util.RetrofitUtil
import kotlinx.coroutines.*
import retrofit2.Response
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
    var allUserInfo:MutableLiveData<HashMap<String, Any>> = MutableLiveData<HashMap<String, Any>>()
    var gradeInfo:Grade? = null
    var userInfo:User? = null
    var commentList=MutableLiveData<MutableList<MenuDetailWithCommentResponse>>(mutableListOf())
    var prodId = -1
    // bottom navigation bar 보여주는 플래그
    var shownNavigationBar:MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    // 위치 정보 가져오기 위한 권한 승인여부
    var needRequest = false
    private var currentLocation = Location("current")
    var cafeLocation = Location("cafe").apply {
        latitude = 36.1079753
        longitude = 128.418512
    }
    // 현재 위치 ~ 카페 간 거리
    var distanceToCafe:MutableLiveData<Int> = MutableLiveData<Int>()

    init {
        getProductList()
        getRecentOrderList(user.id)
        getUserInfo(user.id)
    }

    // 상품 리스트 가져오기
    private fun getProductList(){
        CoroutineScope(Dispatchers.Main).launch {
            val result = RetrofitUtil.productService.getProductList()
            Log.d("main","$result")
            if(result.isEmpty())allProduct=MutableLiveData<List<Product>>()
            else allProduct.value = result
        }
    }

    // 장바구니 리스트 가져오기
    fun getShoppingCart():MutableLiveData<MutableList<ShoppingCart>>{
        val temp = _ShoppingCart
        return temp
    }

    // 장바구니 넣기
    fun addShop(item:ShoppingCart){
        _ShoppingCart.value = _ShoppingCart.value?.plus(listOf(item)) as MutableList<ShoppingCart>?
    }

    //장바구니 삭제
    fun removeShop(index:ShoppingCart){
        _ShoppingCart.value = _ShoppingCart.value?.filter { it != index }?.toMutableList()
        getUserInfo(user.id)
    }

    // 최근 주문정보 가져오기
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

    //유저정보 가져오기
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

    //장바구니 주문 완료하기
    fun completeOrder(order: Order){
        CoroutineScope(Dispatchers.IO).launch {
            val orderId =RetrofitUtil.orderService.makeOrder(order)
            Log.d("view_Order","$orderId")
            getRecentOrderList(order.userId)
        }
        _ShoppingCart.value = mutableListOf()
    }

    //상품별 코멘트 리스트 가져오기
    fun getProductCommentInfo(id:Int){
        CoroutineScope(Dispatchers.Main).launch {
           commentList.value =RetrofitUtil.productService.getProductWithComments(id) as MutableList<MenuDetailWithCommentResponse>
        }
        prodId = id
    }

    //상품별 코멘트 삭제
    fun removeComment(id:Int){
        CoroutineScope(Dispatchers.Main).launch {
            val job = CoroutineScope(Dispatchers.IO).async{
                RetrofitUtil.commentService.delete(id)
            }
            job.await()
            getProductCommentInfo(prodId)
        }


    }
    //상품별 코멘트 넣기
    fun addComment(comment:Comment){
        CoroutineScope(Dispatchers.Main).launch {
            val job = CoroutineScope(Dispatchers.IO).async{
                RetrofitUtil.commentService.insert(comment)
            }
            job.await()
            getProductCommentInfo(prodId)
        }
    }


    // 현재 위치 좌표 설정하기
    fun setCurrentLocation(latitude:Double, longitude:Double){
        currentLocation.latitude = latitude
        currentLocation.longitude = longitude
    }

    // 현재 위치와 카페 간 거리 구하기
    fun getDistanceByCafe(){
        val distance = currentLocation.distanceTo(cafeLocation).toInt()
        Log.d(TAG, "getDistanceByCafe 현재 위치 : ${currentLocation.latitude} / ${currentLocation.longitude}")
        Log.d(TAG, "getDistanceByCafe 카페 위치: ${cafeLocation.latitude} / ${cafeLocation.longitude}")
        Log.d(TAG, "getDistanceByCafe 거리 : ${distance}m")
        distanceToCafe.value = distance
    }

    // 메인에서 최근 주문 선택했을 때 장바구니에 담기
    fun addShopCartWithLatestOrder(item:LatestOrderResponse){
        CoroutineScope(Dispatchers.Main).launch {
            var orderDetailList:List<OrderDetailResponse> = listOf()

            val job = CoroutineScope(Dispatchers.IO).async{
                try {
                    val result = RetrofitUtil.orderService.getOrderDetail(item!!.orderId)
                    Log.d("recentOrderList", "${result.body()}")

                    if(result.code() == 200){
                        if(result.body() == null || result.body()!!.size == 0){
                            orderDetailList = listOf()
                        }else{
                            orderDetailList = result.body()!!
                            Log.d(TAG, "getOrderDetail: ${orderDetailList}")
                        }
                    }else{
                        orderDetailList = listOf()
                    }
                }catch(e: IOException){
                    Log.d(TAG, "getOrderDetail: ${e.message}")
                }
            }
            job.await()

            Log.d(TAG, "addShopCartWithLatestOrder 매개변수값: ${item} ")
            Log.d(TAG, "넘어온 orderDetail: $orderDetailList")

            if(orderDetailList.size > 0){
                for(product in orderDetailList){
                    val temp = ShoppingCart(product.productId,
                        product.img,
                        product.productName,
                        product.quantity,
                        product.unitPrice,
                        (product.quantity * product.unitPrice),
                        product.productType)
                    addShop(temp)
                }
            }

            Log.d(TAG, "shoppingCart 담긴 값: ${_ShoppingCart.value}")
        }
    }

    // bottom navigation bar 표시 여부 나타내기
    fun setVisibleBottomNav(isVisible:Boolean){
        shownNavigationBar.value = isVisible
    }
}