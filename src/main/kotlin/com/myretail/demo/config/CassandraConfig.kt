package com.myretail.demo.config

import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext
import org.springframework.data.cassandra.mapping.CassandraMappingContext

@Configuration
open class CassandraConfig : AbstractCassandraConfiguration() {

    override fun getKeyspaceName(): String = "testKeySpace"

    @Bean
    override fun cluster(): CassandraClusterFactoryBean {
        val cluster = CassandraClusterFactoryBean()
        cluster.setContactPoints("127.0.0.1")
        cluster.setPort(9142)
        cluster.keyspaceCreations = listOf(CreateKeyspaceSpecification(keyspaceName))
        return cluster
    }

    @Bean
    override fun cassandraMapping(): CassandraMappingContext {
        return BasicCassandraMappingContext()
    }

}
