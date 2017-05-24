package com.myretail.service.product

import com.myretail.api.CurrencyCode
import com.myretail.api.Price
import com.myretail.api.Product
import com.myretail.client.product.ProductApi
import com.myretail.service.MyRetailApplicationIntSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class ProductApiIntSpec extends MyRetailApplicationIntSpec {

    ProductApi api

    @Autowired
    ProductPORepository repository

    void setup() {
        api = new Retrofit.Builder()
                .baseUrl("http://localhost:$port")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(ProductApi)
    }

    void 'should get product by id'() {
        given: 'an existing product in the database'
        ProductPO existing = new ProductPO(13860428L, 13.49, CurrencyCode.USD)
        repository.save(existing)

        when: 'we fetch product by id'
        Call<Product> response = api.get(existing.id)

        then: 'the existing product with name and price is returned in the response body'
        response.execute().body() == new Product(
                13860428L,
                'The Big Lebowski (Blu-ray) (Widescreen)',
                new Price(13.49, CurrencyCode.USD)
        )
    }

    void 'should update price'() {
        given: 'an existing product in the database'
        ProductPO existing = new ProductPO(13860428L, 13.49, CurrencyCode.USD)
        repository.save(existing)

        and: "an update to the entity's price"
        Product updated = new Product(existing.id, 'The Big Lebowski (Blu-ray) (Widescreen)', new Price(99.99, existing.currentPriceCurrencyCode))

        when: "we update the product's price"
        Call<Product> response = api.update(existing.id, updated)

        then: 'the price changed'
        response.execute().body() == updated
    }

}
