package com.ssafy.smartstore.api

import com.ssafy.smartstore.dto.Product
import com.ssafy.smartstore.response.MenuDetailWithCommentResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("rest/product")
    suspend fun getProductList():List<Product>

    // {productId}에 해당하는 상품의 정보를 comment와 함께 반환한다.
    // comment 조회시 사용
    @GET("rest/product/{productId}")
    fun getProductWithComments(@Path("productId") productId: Int): List<MenuDetailWithCommentResponse>
}