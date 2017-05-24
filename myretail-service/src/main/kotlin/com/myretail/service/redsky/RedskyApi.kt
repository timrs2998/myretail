package com.myretail.service.redsky

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.CompletableFuture

interface RedskyApi {

    @GET("/v2/pdp/tcin/{id}")
    fun get(@Path("id") id: Long, @Query("exclude") exclude: List<String>): CompletableFuture<RedskyResponse>

}
