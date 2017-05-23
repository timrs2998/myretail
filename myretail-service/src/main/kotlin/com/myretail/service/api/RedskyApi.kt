package com.myretail.service.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedskyApi {

    @GET("/v2/pdp/tcin/{id}")
    fun get(@Path("id") id: Long, @Query("exclude") exclude: List<String>): Call<RedskyResponse>

}

data class RedskyResponse(val product: RedskyProduct)

data class RedskyProduct(
        val availableToPromiseNetwork: RedskyAvailableToPromiseNetwork?,
        val deepRedLabels: RedskyDeepRedLabels?,
        val item: RedskyItem
)

data class RedskyAvailableToPromiseNetwork(val product_id: Long)

class RedskyDeepRedLabels

class RedskyItem(val productDescription: RedskyProductDescription?)

data class RedskyProductDescription(val title: String)
