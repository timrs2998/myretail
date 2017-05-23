package com.myretail.service.product

import com.myretail.api.CurrencyCode
import com.myretail.api.Price
import com.myretail.api.Product
import com.myretail.service.api.RedskyApi
import com.myretail.service.api.RedskyAvailableToPromiseNetwork
import com.myretail.service.api.RedskyDeepRedLabels
import com.myretail.service.api.RedskyItem
import com.myretail.service.api.RedskyProduct
import com.myretail.service.api.RedskyProductDescription
import com.myretail.service.api.RedskyResponse
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import retrofit2.Call
import retrofit2.Response
import spock.lang.Specification

class ProductServiceSpec extends Specification {

    ProductService service
    ProductPORepository repository = Mock(ProductPORepository)
    RedskyApi api = Mock(RedskyApi)
    Call<RedskyProduct> call = Mock(Call)

    void setup() {
        service = new ProductService(repository, api)
    }

    void 'should get product - already in database'() {
        given:
        ProductPO productPO = new ProductPO(1, 23.3, CurrencyCode.USD)
        RedskyProduct redskyProduct = new RedskyProduct(
                new RedskyAvailableToPromiseNetwork(1),
                new RedskyDeepRedLabels(),
                new RedskyItem(new RedskyProductDescription('hello'))
        )
        RedskyResponse redskyResponse = new RedskyResponse(redskyProduct)

        when:
        Product response = service.get(1)

        then:
        1 * repository.findOne(1) >> productPO
        1 * api.get(1, _ as List) >> call
        1 * call.execute() >> Response.success(redskyResponse)
        0 * _
        response == new Product(
                1L,
                'hello',
                new Price(23.3, CurrencyCode.USD)
        )
    }

    void 'should get product - not in database'() {
        given:
        RedskyProduct redskyProduct = new RedskyProduct(
                new RedskyAvailableToPromiseNetwork(1),
                new RedskyDeepRedLabels(),
                new RedskyItem(new RedskyProductDescription('hello'))
        )
        RedskyResponse redskyResponse = new RedskyResponse(redskyProduct)

        when:
        Product response = service.get(1)

        then:
        1 * repository.findOne(1) >> null
        1 * api.get(1, _ as List) >> call
        1 * call.execute() >> Response.success(redskyResponse)
        0 * _
        response == new Product(1L, 'hello', null)
    }

    void 'should get product throws 404 - already in database'() {
        given:
        ProductPO productPO = new ProductPO(1, 23.3, CurrencyCode.USD)
        RedskyProduct redskyProduct = new RedskyProduct(
                null,
                null,
                new RedskyItem(null)
        )
        RedskyResponse redskyResponse = new RedskyResponse(redskyProduct)

        when:
        service.get(1)

        then:
        1 * repository.findOne(1) >> productPO
        1 * api.get(1, _ as List) >> call
        1 * call.execute() >> Response.success(redskyResponse)
        0 * _
        thrown(ResourceNotFoundException)
    }

    void 'should get product throws 404 - not in database'() {
        given:
        RedskyProduct redskyProduct = new RedskyProduct(
                null,
                null,
                new RedskyItem(null)
        )
        RedskyResponse redskyResponse = new RedskyResponse(redskyProduct)

        when:
        service.get(1)

        then:
        1 * repository.findOne(1) >> null
        1 * api.get(1, _ as List) >> call
        1 * call.execute() >> Response.success(redskyResponse)
        0 * _
        thrown(ResourceNotFoundException)
    }

    void 'should update price'() {
        given:
        Product request = new Product(1L, 'hello', new Price(23.3, CurrencyCode.USD))
        RedskyProduct redskyProduct = new RedskyProduct(
                new RedskyAvailableToPromiseNetwork(1),
                new RedskyDeepRedLabels(),
                new RedskyItem(new RedskyProductDescription('hello'))
        )
        RedskyResponse redskyResponse = new RedskyResponse(redskyProduct)
        ProductPO saved = new ProductPO(1L, 23.3, CurrencyCode.USD)

        when:
        Product response = service.update(request)

        then:
        1 * api.get(1, _ as List) >> call
        1 * call.execute() >> Response.success(redskyResponse)
        1 * repository.save(saved) >> saved
        0 * _
        response == new Product(
                1L,
                'hello',
                new Price(23.3, CurrencyCode.USD)
        )
    }

    void 'should update price throws 404'() {
        given:
        Product request = new Product(1L, 'hello', new Price(23.3, CurrencyCode.USD))
        RedskyProduct redskyProduct = new RedskyProduct(
                null,
                null,
                new RedskyItem(null)
        )
        RedskyResponse redskyResponse = new RedskyResponse(redskyProduct)

        when:
        service.update(request)

        then:
        1 * api.get(1, _ as List) >> call
        1 * call.execute() >> Response.success(redskyResponse)
        0 * _
        thrown(ResourceNotFoundException)
    }

    void 'should remove price'() {
        given:
        Product request = new Product(1L, 'hello', null)
        RedskyProduct redskyProduct = new RedskyProduct(
                new RedskyAvailableToPromiseNetwork(1),
                new RedskyDeepRedLabels(),
                new RedskyItem(new RedskyProductDescription('hello'))
        )
        RedskyResponse redskyResponse = new RedskyResponse(redskyProduct)

        when:
        Product response = service.update(request)

        then:
        1 * api.get(1, _ as List) >> call
        1 * call.execute() >> Response.success(redskyResponse)
        1 * repository.delete(request.id)
        0 * _
        response == new Product(
                1L,
                'hello',
                null
        )
    }

    void 'should remove price throws 404'() {
        given:
        Product request = new Product(1L, 'hello', null)
        RedskyProduct redskyProduct = new RedskyProduct(
                null,
                null,
                new RedskyItem(null)
        )
        RedskyResponse redskyResponse = new RedskyResponse(redskyProduct)

        when:
        service.update(request)

        then:
        1 * api.get(1, _ as List) >> call
        1 * call.execute() >> Response.success(redskyResponse)
        0 * _
        thrown(ResourceNotFoundException)
    }

}
