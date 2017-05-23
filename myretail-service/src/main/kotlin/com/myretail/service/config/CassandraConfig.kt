package com.myretail.service.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@Configuration
@DependsOn("embeddedCassandra")
@EnableCassandraRepositories(basePackages = arrayOf("com.myretail.service.product"))
class CassandraConfig : AbstractCassandraConfiguration() {

    @Value("\${cassandra.contactPoints}")
    var _contactPoints: String? = null

    @Value("\${cassandra.keyspaceName}")
    var _keyspaceName: String? = null

    @Value("\${cassandra.port}")
    var _port: Int? = null

    override fun getContactPoints() = _contactPoints

    override fun getEntityBasePackages() = arrayOf("com.myretail.service.product")

    override fun getKeyspaceCreations() = listOf(
            CreateKeyspaceSpecification.createKeyspace(keyspaceName)
    )

    override fun getKeyspaceName() = _keyspaceName

    override fun getPort() = _port!!

    override fun getSchemaAction() = SchemaAction.CREATE_IF_NOT_EXISTS

}
