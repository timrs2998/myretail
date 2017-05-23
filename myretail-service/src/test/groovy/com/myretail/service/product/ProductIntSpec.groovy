package com.myretail.service.product

import com.myretail.api.CurrencyCode
import com.myretail.api.Price
import com.myretail.api.Product
import com.myretail.service.MyRetailApplicationIntSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity

class ProductIntSpec extends MyRetailApplicationIntSpec {

    @Autowired
    ProductPORepository repository

    void setup() {
        repository.deleteAll()
    }

    void 'should get product by id - already in database'() {
        given: 'an existing product in the database'
        ProductPO existing = new ProductPO(13860428L, 13.49, CurrencyCode.USD)
        repository.save(existing)

        when: 'we fetch product by id'
        ResponseEntity<Product> response = restTemplate.getForEntity('/products/13860428', Product)

        then: 'the existing product with name is returned in the response body'
        response.body == new Product(
                13860428L,
                'The Big Lebowski (Blu-ray) (Widescreen)',
                new Price(13.49, CurrencyCode.USD)
        )
    }

    void 'should get product by id - not in database'() {
        when: 'we fetch product by id'
        ResponseEntity<Product> response = restTemplate.getForEntity('/products/13860428', Product)

        then: 'the existing product with name is returned in the response body'
        response.body == new Product(
                13860428L,
                'The Big Lebowski (Blu-ray) (Widescreen)',
                null
        )
    }

    void 'should update price with PUT request - already exists in database'() {
        given: 'an existing product in the database'
        ProductPO existing = new ProductPO(13860428L, 13.49, CurrencyCode.USD)
        repository.save(existing)

        and: "an update to the entity's price"
        Product updated = new Product(existing.id, 'The Big Lebowski (Blu-ray) (Widescreen)', new Price(99.99, existing.currentPriceCurrencyCode))
        RequestEntity<Product> requestEntity = new RequestEntity<>(updated, HttpMethod.PUT, URI.create("http://localhost:$port/products/$existing.id"))

        when: "we update the product's price"
        ResponseEntity<Product> response = restTemplate.exchange("/products/$existing.id", HttpMethod.PUT, requestEntity, Product)

        then: 'the price changed'
        updated == response.body
        response.body.currentPrice.value == 99.99
    }

    void 'should update price with PUT request - not in database'() {
        given: "an update to an entity's price"
        Long id = 13860428L
        Product updated = new Product(id, 'The Big Lebowski (Blu-ray) (Widescreen)', new Price(99.99, CurrencyCode.USD))
        RequestEntity<Product> requestEntity = new RequestEntity<>(updated, HttpMethod.PUT, URI.create("http://localhost:$port/products/$id"))

        when: "we update the product's price"
        ResponseEntity<Product> response = restTemplate.exchange("/products/$id", HttpMethod.PUT, requestEntity, Product)

        then: 'the price is saved'
        updated == response.body
        response.body.currentPrice.value == 99.99
    }

    void 'should remove price with PUT request - exists in database'() {
        given: 'an existing product in the database'
        ProductPO existing = new ProductPO(13860428L, 13.49, CurrencyCode.USD)
        repository.save(existing)

        and: "an update to remove the entity's price"
        Product updated = new Product(existing.id, 'The Big Lebowski (Blu-ray) (Widescreen)', null)
        RequestEntity<Product> requestEntity = new RequestEntity<>(updated, HttpMethod.PUT, URI.create("http://localhost:$port/products/$existing.id"))

        when: "we update the product's price"
        ResponseEntity<Product> response = restTemplate.exchange("/products/$existing.id", HttpMethod.PUT, requestEntity, Product)

        then: 'the price changed'
        updated == response.body
        response.body.currentPrice == null
    }

    void 'should remove price with PUT request - not in database'() {
        given: "an update to remove the entity's price"
        Long id = 13860428L
        Product updated = new Product(id, 'The Big Lebowski (Blu-ray) (Widescreen)', null)
        RequestEntity<Product> requestEntity = new RequestEntity<>(updated, HttpMethod.PUT, URI.create("http://localhost:$port/products/$id"))

        when: "we update the product's price"
        ResponseEntity<Product> response = restTemplate.exchange("/products/$id", HttpMethod.PUT, requestEntity, Product)

        then: 'the price changed'
        updated == response.body
        response.body.currentPrice == null
    }

}
