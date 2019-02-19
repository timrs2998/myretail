package com.myretail.client.product

import com.myretail.api.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApi {
  @GET("/products/{id}")
  fun get(@Path("id") id: Long): Call<Product>

  @PUT("/products/{id}")
  fun update(@Path("id") id: Long, @Body product: Product): Call<Product>
}
