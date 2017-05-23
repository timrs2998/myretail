package com.myretail.service.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.myretail.service.api.RedskyApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
open class RedskyConfig {

    @Value("\${redsky.uri}")
    var redskyUri: String? = null

    @Bean
    fun redskyApi(): RedskyApi {
        val objectMapper = ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .registerModule(KotlinModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        return Retrofit.Builder()
                .baseUrl(redskyUri)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(RedskyApi::class.java)
    }

}
