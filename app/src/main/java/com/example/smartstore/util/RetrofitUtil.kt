package com.ssafy.smartstore.util

import com.example.smartstore.ApplicationClass
import com.ssafy.smartstore.api.CommentApi
import com.ssafy.smartstore.api.OrderApi
import com.ssafy.smartstore.api.ProductApi
import com.ssafy.smartstore.api.UserApi

class RetrofitUtil {
    companion object{
        val commentService = ApplicationClass.retrofit.create(CommentApi::class.java)
        val orderService = ApplicationClass.retrofit.create(OrderApi::class.java)
        val productService = ApplicationClass.retrofit.create(ProductApi::class.java)
        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
    }
}