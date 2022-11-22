package com.example.smartstore.util

import com.example.smartstore.ApplicationClass
import com.example.smartstore.service.ProductApi


class RetrofitUtil {
    companion object{
//        val commentService = ApplicationClass.retrofit.create(CommentApi::class.java)
//        val orderService = ApplicationClass.retrofit.create(OrderApi::class.java)
        val productService: ProductApi = ApplicationClass.retrofit.create(ProductApi::class.java)
//        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
    }
}