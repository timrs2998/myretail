package com.myretail.demo.product

import com.myretail.demo.DemoApplicationIntSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity

class ProductIntSpec extends DemoApplicationIntSpec {

    @Autowired
    ProductRepository repository

    void 'should get product by id'() {
        given: 'an existing product in the database'
        ProductPO existing = new ProductPO(13860428L, 13.49, CurrencyCode.USD)
        repository.save(existing)

        when: 'we fetch product by id'
        ResponseEntity<ProductPO> response = restTemplate.getForEntity('/products/13860428', ProductPO)

        then: 'the existing product is returned in the response body'
        response.statusCode == HttpStatus.OK
        existing == response.body
    }

    void 'should update price with PUT request'() {
        given: 'an existing product in the database'
        ProductPO existing = new ProductPO(13860428L, 13.49, CurrencyCode.USD)
        repository.save(existing)

        and: "an update to the entity's price"
        ProductPO updated = new ProductPO(existing.id, 99.99, existing.currentPriceCurrencyCode)
        RequestEntity<ProductPO> requestEntity = new RequestEntity<>(updated, HttpMethod.PUT, URI.create("http://localhost:$port/products/$existing.id"))

        when: "we update the product's price"
        ResponseEntity<ProductPO> response = restTemplate.exchange("/products/$existing.id", HttpMethod.PUT, requestEntity, ProductPO)

        then: 'the price changed'
        response.statusCode == HttpStatus.OK
        existing != response.body
        updated == response.body
        response.body.currentPriceValue == 99.99
    }

}
