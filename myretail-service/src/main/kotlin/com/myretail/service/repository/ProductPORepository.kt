package com.myretail.service.repository

import com.myretail.service.po.ProductPO
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface ProductPORepository : CrudRepository<ProductPO, Long>
