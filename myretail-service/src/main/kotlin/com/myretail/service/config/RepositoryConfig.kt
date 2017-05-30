package com.myretail.service.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.myretail.service.product.ProductPO
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter

@Configuration
class RepositoryConfig : RepositoryRestConfigurerAdapter() {

    override fun configureJacksonObjectMapper(objectMapper: ObjectMapper) {
        objectMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

}
