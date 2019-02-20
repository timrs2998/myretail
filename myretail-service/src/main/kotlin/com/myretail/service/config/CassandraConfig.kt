package com.myretail.service.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@Configuration
@EnableCassandraRepositories(basePackages = ["com.myretail.service.repository"])
class CassandraConfig : AbstractCassandraConfiguration() {
  @Value("\${cassandra.contactPoints}")
  private lateinit var contactPoints: String

  @Value("\${cassandra.keyspaceName}")
  private lateinit var keyspaceName: String

  @Value("\${cassandra.port}")
  private var port: Int? = null

  override fun getContactPoints() = contactPoints

  override fun getKeyspaceName() = keyspaceName

  override fun getPort() = port!!

  override fun getEntityBasePackages() = arrayOf("com.myretail.service.po")

  override fun getKeyspaceCreations() = listOf(
    CreateKeyspaceSpecification.createKeyspace(keyspaceName).ifNotExists()
  )

  override fun getSchemaAction() = SchemaAction.CREATE_IF_NOT_EXISTS
}
