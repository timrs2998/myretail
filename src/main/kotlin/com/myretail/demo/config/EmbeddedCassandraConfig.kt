package com.myretail.demo.config

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class EmbeddedCassandraConfig {

    @Value("\${embeddedCassandra.enabled}")
    var enabled: Boolean? = null

    @Bean
    @PostConstruct
    fun embeddedCassandra(): Boolean {
        if (enabled!!) {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra()
            return true
        }
        return false
    }

}
