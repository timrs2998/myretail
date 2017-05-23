package com.myretail.service.product

import com.myretail.api.Product
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@RepositoryRestController
@RequestMapping(value = "products")
class ProductController @Inject constructor(val service: ProductService) {

    @RequestMapping(value = "{id}", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun get(@PathVariable id: Long): ResponseEntity<Resource<Product>> {
        val entity = service.get(id)
        return responseWithLink(entity)
    }

    @RequestMapping(value = "{id}", method = arrayOf(RequestMethod.PUT))
    @ResponseBody
    fun update(@PathVariable id: Long, @RequestBody body: Product): ResponseEntity<Resource<Product>> {
        if (id != body.id) {
            return ResponseEntity.badRequest().build()
        }
        val entity = service.update(body)
        return responseWithLink(entity)
    }

    private fun responseWithLink(entity: Product): ResponseEntity<Resource<Product>> {
        val resource = Resource<Product>(entity)
        resource.add(linkTo(methodOn(ProductController::class.java).get(entity.id)).withSelfRel())
        return ResponseEntity.ok(resource)
    }

}