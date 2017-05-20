package com.myretail.demo.config

import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@Configuration
@EnableCassandraRepositories(basePackages = arrayOf("com.myretail.demo.repository"))
class CassandraConfig : AbstractCassandraConfiguration() {

    override fun getKeyspaceName(): String = "myretail"

    override fun getPort(): Int = 9142

    override fun getKeyspaceCreations() = listOf(
            CreateKeyspaceSpecification.createKeyspace(keyspaceName)
    )

}
