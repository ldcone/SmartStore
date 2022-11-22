package com.example.smartstore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.smartstore.dto.Product
import com.ssafy.smartstore.util.RetrofitUtil
import kotlinx.coroutines.*

const val HOME = "home"
const val ORDER = "order"
const val MYPAGE = "myPage"

class MainViewModel():ViewModel() {
    var allProduct: MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    init {
        getProductList()
    }

    fun getProductList(){
        CoroutineScope(Dispatchers.Main).launch {
            val result = RetrofitUtil.productService.getProductList()
            Log.d("main","$result")
            if(result.isEmpty())allProduct=MutableLiveData<List<Product>>()
            else allProduct.value = result
        }
    }
}