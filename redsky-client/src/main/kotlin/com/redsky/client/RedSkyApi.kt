package com.redsky.client

import com.redsky.api.RedSkyResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.CompletableFuture

interface RedSkyApi {
  @GET("/v2/pdp/tcin/{id}")
  fun get(@Path("id") id: Long, @Query("exclude") exclude: List<String>): CompletableFuture<RedSkyResponse>
}
