package com.myretail.demo.product

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(
        exported = true,
        path = "products",
        collectionResourceRel = "products",
        itemResourceRel = "product"
)
interface ProductRepository : CrudRepository<ProductPO, Long>
