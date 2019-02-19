package com.myretail.service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class HealthIntSpec extends MyRetailApplicationIntSpec {

    void 'health endpoint redirects to spring actuator'() {
        when: 'we hit the Google App Engine health endpoint'
        ResponseEntity<JsonNode> response = restTemplate.getForEntity('/_ah/health', JsonNode)

        then: "we receive the actuator health endpoint's success response"
        response.statusCode == HttpStatus.OK
        response.body.status.textValue() == 'UP'
        response.body.details.cassandra.status.textValue() == 'UP'
    }

}
