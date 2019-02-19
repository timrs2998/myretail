package com.myretail.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.api.CurrencyCode
import com.myretail.api.Price
import com.myretail.api.Product
import com.myretail.service.product.ProductPO
import com.myretail.service.product.ProductPORepository
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AsciidoctorSpec extends MyRetailApplicationIntSpec {

    @Rule
    JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation('build/generated-snippets')

    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    ProductPORepository repository

    @Autowired
    WebApplicationContext context

    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build()
    }

    void cleanup() {
        repository.deleteAll()
    }

    void 'should get product without price'() {
        given:
        repository.deleteAll()

        when: 'we fetch product by id'
        ResultActions result = mockMvc.perform(get('/products/13860428').accept(MediaType.APPLICATION_JSON))

        then: 'response is successful'
        result
                .andExpect(status().isOk())
                .andDo(document(
                'get-product-without-price',
                links(halLinks(), linkWithRel("self").ignored()),
                responseFields(
                        fieldWithPath('id').description('product id'),
                        fieldWithPath('name').description('product name from RedSky'),
                        subsectionWithPath('_links').ignored()
                )
        ))
    }

    void 'should get product with price in database'() {
        given: 'an existing product in the database'
        ProductPO existing = new ProductPO(13860428L, 13.49, CurrencyCode.USD)
        repository.save(existing)

        when: 'we fetch product by id'
        ResultActions result = mockMvc.perform(get('/products/13860428').accept(MediaType.APPLICATION_JSON))

        then: 'response is successful'
        result
                .andExpect(status().isOk())
                .andDo(document(
                'get-product-with-price',
                links(halLinks(), linkWithRel("self").ignored()),
                responseFields(
                        fieldWithPath('id').description('product id'),
                        fieldWithPath('current_price.value').description('current price of product from Cassandra'),
                        fieldWithPath('current_price.currency_code').description('current currency code of product from Cassandra'),
                        fieldWithPath('name').description('product name from RedSky'),
                        subsectionWithPath('_links').ignored()
                )))
    }

    void 'should add price to product'() {
        given: "an update to an entity's price"
        Long id = 13860428L
        Product updated = new Product(id, 'The Big Lebowski (Blu-ray) (Widescreen)', new Price(99.99, CurrencyCode.USD))

        when: "we update the product's price"
        ResultActions result = mockMvc.perform(put('/products/13860428')
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated))
                .contentType(MediaType.APPLICATION_JSON))

        then: 'the price is saved'
        result
                .andExpect(status().isOk())
                .andDo(document(
                'update-product-price',
                links(halLinks(), linkWithRel("self").ignored()),
                responseFields(
                        fieldWithPath('id').description('product id'),
                        fieldWithPath('current_price.value').description('current price of product from Cassandra'),
                        fieldWithPath('current_price.currency_code').description('current currency code of product from Cassandra'),
                        fieldWithPath('name').description('product name from RedSky'),
                        subsectionWithPath('_links').ignored()
                )))
    }

    void 'should remove price from product'() {
        given: "an update to remote an entity's price"
        Long id = 13860428L
        Product updated = new Product(id, 'The Big Lebowski (Blu-ray) (Widescreen)', null)

        when: "we remove the product's price"
        ResultActions result = mockMvc.perform(put('/products/13860428')
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated))
                .contentType(MediaType.APPLICATION_JSON))

        then: 'the price is removed'
        result
                .andExpect(status().isOk())
                .andDo(document(
                'remove-product-price',
                links(halLinks(), linkWithRel("self").ignored()),
                responseFields(
                        fieldWithPath('id').description('product id'),
                        fieldWithPath('name').description('product name from RedSky'),
                        subsectionWithPath('_links').ignored()
                )))
    }

}
