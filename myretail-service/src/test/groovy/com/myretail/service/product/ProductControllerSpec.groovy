package com.myretail.service.product

import com.myretail.api.Product
import org.springframework.hateoas.Link
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class ProductControllerSpec extends Specification {

    ProductController controller
    ProductService service = Mock(ProductService)

    void setup() {
        controller = new ProductController(service)
        HttpServletRequest mockRequest = new MockHttpServletRequest()
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest)
        RequestContextHolder.requestAttributes = servletRequestAttributes
    }

    void cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }

    void 'should get entity by id without server'() {
        given:
        Product product = new Product(1, '', null)

        when:
        def response = controller.get(product.id)

        then:
        1 * service.get(product.id) >> product
        0 * _
        response
        response.statusCode == HttpStatus.OK
        response.body.content == product
        response.body.links == [new Link('http://localhost/products/1', 'self')]
    }

    void 'should update entity'() {
        given:
        Product product = new Product(1, '', null)

        when:
        def response = controller.update(product.id, product)

        then:
        1 * service.update(product) >> product
        0 * _
        response
        response.statusCode == HttpStatus.OK
        response.body.content == product
        response.body.links == [new Link('http://localhost/products/1', 'self')]
    }

    void 'should update entity fails when ids are different'() {
        given:
        Product product = new Product(1, '', null)

        when:
        def response = controller.update(2L, product)

        then:
        0 * _
        response
        response.statusCode == HttpStatus.BAD_REQUEST
    }

}
