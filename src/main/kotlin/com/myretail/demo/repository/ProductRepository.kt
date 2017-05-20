package com.myretail.demo.repository

import com.myretail.demo.domain.Product
import org.springframework.data.cassandra.repository.CassandraRepository

interface ProductRepository : CassandraRepository<Product>
