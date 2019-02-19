package com.myretail.service.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.service.MyRetailApplication
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@CompileStatic
@ContextConfiguration(classes = [
  MyRetailApplication,
  MockConfiguration
])
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class MyRetailApplicationIntSpec extends Specification {

  @Value('${local.server.port}')
  Integer port

  @Autowired
  ObjectMapper objectMapper

  @Autowired
  TestRestTemplate restTemplate

  void setup() {
    restTemplate.restTemplate.messageConverters
      .findAll { it instanceof MappingJackson2HttpMessageConverter }
      .collect { it as MappingJackson2HttpMessageConverter }
      .each { MappingJackson2HttpMessageConverter converter ->
      converter.objectMapper.propertyNamingStrategy = SNAKE_CASE
    }
  }

}
