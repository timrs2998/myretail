package com.myretail.demo

import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener
import org.cassandraunit.spring.EmbeddedCassandra
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@EmbeddedCassandra
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestExecutionListeners(listeners = [
        CassandraUnitDependencyInjectionTestExecutionListener,
        DependencyInjectionTestExecutionListener
])
class DemoApplicationIntSpec extends Specification {

    void 'should initialize'() {
        expect:
        true
    }

}
