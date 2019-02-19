package com.myretail.service.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.service.redsky.RedSkyApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Inject

@Configuration
class RedSkyConfig {

    @Value("\${redSky.uri}")
    var redSkyUri: String? = null

    @Bean
    @Inject
    fun redSkyApi(objectMapper: ObjectMapper): RedSkyApi {
        return redSkyRetrofit(objectMapper).create(RedSkyApi::class.java)
    }

    protected fun redSkyRetrofit(objectMapper: ObjectMapper): Retrofit = Retrofit.Builder()
            .baseUrl(redSkyUri!!)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

}
