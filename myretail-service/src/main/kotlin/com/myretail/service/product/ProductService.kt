package com.myretail.service.product

import com.myretail.api.Price
import com.myretail.api.Product
import com.myretail.service.api.RedskyApi
import com.myretail.service.api.RedskyProduct
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class ProductService @Inject constructor(val repository: ProductPORepository,
                                         val api: RedskyApi) {

    fun get(id: Long): Product {
        val productPO: ProductPO? = repository.findOne(id)
        val redskyProduct: RedskyProduct? = api.get(id, listOf()).execute().body()?.product
        validateRedskyProductExists(redskyProduct)
        return if (productPO == null) {
            buildProduct(redskyProduct!!)
        } else {
            buildProduct(productPO, redskyProduct!!)
        }
    }

    fun update(updated: Product): Product {
        val redskyProduct: RedskyProduct? = api.get(updated.id, listOf()).execute().body()?.product
        validateRedskyProductExists(redskyProduct)
        if (updated.currentPrice == null) {
            repository.delete(updated.id)
            return buildProduct(redskyProduct!!)
        }
        val productPO = repository.save(buildProductPO(updated))
        return buildProduct(productPO, redskyProduct!!)
    }

    private fun validateRedskyProductExists(redskyProduct: RedskyProduct?) {
        if (redskyProduct?.availableToPromiseNetwork == null) {
            throw ResourceNotFoundException("Product does not exist in Redsky.")
        }
    }

    private fun buildProductPO(product: Product): ProductPO {
        return ProductPO(
                id = product.id,
                currentPriceValue = product.currentPrice!!.value,
                currentPriceCurrencyCode = product.currentPrice!!.currencyCode
        )
    }

    private fun buildProduct(redskyProduct: RedskyProduct, price: Price? = null): Product {
        return Product(
                id = redskyProduct.availableToPromiseNetwork!!.product_id,
                name = redskyProduct.item.productDescription!!.title,
                currentPrice = price
        )
    }

    private fun buildProduct(productPO: ProductPO, redskyProduct: RedskyProduct): Product {
        return Product(
                id = productPO.id,
                name = redskyProduct.item.productDescription!!.title,
                currentPrice = Price(
                        value = productPO.currentPriceValue,
                        currencyCode = productPO.currentPriceCurrencyCode
                )
        )
    }
}
