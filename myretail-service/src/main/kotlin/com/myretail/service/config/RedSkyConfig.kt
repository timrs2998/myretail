package com.myretail.service.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.service.redsky.RedskyApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.adapter.java8.Java8CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Inject

@Configuration
class RedSkyConfig {

    @Value("\${redsky.uri}")
    var redskyUri: String? = null

    @Bean
    @Inject
    fun redskyApi(objectMapper: ObjectMapper): RedskyApi {
        return redskyRetrofit(objectMapper).create(RedskyApi::class.java)
    }

    protected fun redskyRetrofit(objectMapper: ObjectMapper): Retrofit = Retrofit.Builder()
            .baseUrl(redskyUri)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .build()

}
