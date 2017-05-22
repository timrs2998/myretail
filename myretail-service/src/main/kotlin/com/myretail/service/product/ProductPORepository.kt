package com.myretail.service.product

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(
        exported = true,
        path = "products",
        collectionResourceRel = "products",
        itemResourceRel = "product"
)
interface ProductPORepository : CrudRepository<ProductPO, Long>
