package com.myretail.service.product

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface ProductPORepository : CrudRepository<ProductPO, Long>
