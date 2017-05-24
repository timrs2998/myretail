package com.myretail.service.product

import com.myretail.api.Price
import com.myretail.api.Product
import com.myretail.service.redsky.RedskyApi
import com.myretail.service.redsky.RedskyProduct
import com.myretail.service.redsky.RedskyResponse
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.stereotype.Component
import retrofit2.HttpException
import java.util.concurrent.Future
import javax.inject.Inject

@Component
class ProductService @Inject constructor(val repository: ProductPORepository,
                                         val api: RedskyApi) {

    val excludes = listOf("taxonomy", "price", "promotion", "bulk_ship",
            "rating_and_review_reviews", "rating_and_review_statistics",
            "question_answer_statistics")

    fun get(id: Long): Product {
        val future: Future<RedskyResponse> = api.get(id, excludes)
        val productPO: ProductPO? = repository.findOne(id)
        val redskyProduct: RedskyProduct = getProduct(future)
        return if (productPO == null) {
            buildProduct(redskyProduct)
        } else {
            buildProduct(productPO, redskyProduct)
        }
    }

    fun update(updated: Product): Product {
        val redskyProduct: RedskyProduct? = getProduct(api.get(updated.id, excludes))
        if (updated.currentPrice == null) {
            repository.delete(updated.id)
            return buildProduct(redskyProduct!!)
        }
        val productPO = repository.save(buildProductPO(updated))
        return buildProduct(productPO, redskyProduct!!)
    }

    private fun getProduct(future: Future<RedskyResponse>): RedskyProduct {
        try {
            return future.get().product
        } catch (ex: java.util.concurrent.ExecutionException) {
            val cause = ex.cause
            if (cause is HttpException && cause.code() == 404) {
                throw ResourceNotFoundException("Product does not exist in Redsky.")
            }
            throw ex
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
                id = redskyProduct.availableToPromiseNetwork!!.productId,
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
