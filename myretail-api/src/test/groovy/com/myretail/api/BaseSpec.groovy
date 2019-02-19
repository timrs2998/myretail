package com.myretail.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import groovy.transform.CompileStatic
import spock.lang.Shared
import spock.lang.Specification

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE

@CompileStatic
abstract class BaseSpec extends Specification {

  @Shared
  ObjectMapper objectMapper = new ObjectMapper()
    .setPropertyNamingStrategy(SNAKE_CASE)
    .registerModule(new KotlinModule())

  String jsonFromFile(String filename) {
    return objectMapper.readValue(getClass().getResource("/${filename}.json").text, JsonNode).toString()
  }

  def <T> T fromJson(String filename, Class<T> clazz) {
    return objectMapper.readValue(jsonFromFile(filename), clazz)
  }

}
