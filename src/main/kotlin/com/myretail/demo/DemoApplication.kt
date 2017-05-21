package com.myretail.demo

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    // Remove this line once we have a real cassandra cluster
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()

    SpringApplication.run(DemoApplication::class.java, *args)
}
