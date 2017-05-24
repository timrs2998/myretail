package com.myretail.service.redsky

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import spock.lang.Specification

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE

class RedskyResponseSpec extends Specification {

    ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new KotlinModule())

    void 'can parse Big Lebowski reponse'() {
        given: 'Big Lebowski json response from redsky'
        String json = getClass().getResource('/redsky/bigLebowskiResponse.json').text

        when: 'response converted to object'
        RedskyResponse deserialized = objectMapper.readValue(json, RedskyResponse)

        then: 'object has id and name'
        deserialized == new RedskyResponse(new RedskyProduct(
                new AvailableToPromiseNetwork(13860428),
                DeepRedLabels.INSTANCE,
                new Item(new ProductDescription('The Big Lebowski (Blu-ray)'))
        ))
    }

    void 'can parse Beats response'() {
        given: 'Big Lebowski json response from redsky'
        String json = getClass().getResource('/redsky/beatsResponse.json').text

        when: 'response converted to object'
        RedskyResponse deserialized = objectMapper.readValue(json, RedskyResponse)

        then: 'object has id and name'
        deserialized == new RedskyResponse(new RedskyProduct(
                new AvailableToPromiseNetwork(16696652),
                DeepRedLabels.INSTANCE,
                new Item(new ProductDescription('Beats Solo 2 Wireless - Black (MHNG2AM/A)'))
        ))
    }

}
