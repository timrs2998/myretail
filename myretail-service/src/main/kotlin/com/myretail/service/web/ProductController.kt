package com.myretail.service.web

import com.myretail.api.Product
import com.myretail.service.service.ProductService
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import javax.inject.Inject

@RepositoryRestController
@RequestMapping(value = ["products"])
class ProductController @Inject constructor(val service: ProductService) {

  @RequestMapping(value = ["{id}"], method = [RequestMethod.GET])
  @ResponseBody
  fun get(@PathVariable id: Long): ResponseEntity<Resource<Product>> {
    val entity = service.get(id)
    return responseWithLink(entity)
  }

  @RequestMapping(value = ["{id}"], method = [RequestMethod.PUT])
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
