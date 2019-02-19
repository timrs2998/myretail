package com.myretail.service.redsky

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import spock.lang.Specification

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE

class RedSkyResponseSpec extends Specification {

    ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new KotlinModule())

    void 'can parse Big Lebowski reponse'() {
        given: 'Big Lebowski json response from RedSky'
        String json = getClass().getResource('/redsky/bigLebowskiResponse.json').text

        when: 'response converted to object'
        RedSkyResponse deserialized = objectMapper.readValue(json, RedSkyResponse)

        then: 'object has id and name'
        deserialized
        deserialized.product.availableToPromiseNetwork.productId == 13860428
        deserialized.product.deepRedLabels instanceof DeepRedLabels
        deserialized.product.item.productDescription.title == 'The Big Lebowski (Blu-ray)'
    }

    void 'can parse Beats response'() {
        given: 'Big Lebowski json response from RedSky'
        String json = getClass().getResource('/redsky/beatsResponse.json').text

        when: 'response converted to object'
        RedSkyResponse deserialized = objectMapper.readValue(json, RedSkyResponse)

        then: 'object has id and name'
        deserialized
        deserialized.product.availableToPromiseNetwork.productId == 16696652
        deserialized.product.deepRedLabels instanceof DeepRedLabels
        deserialized.product.item.productDescription.title == 'Beats Solo 2 Wireless - Black (MHNG2AM/A)'
    }

}
