package com.myretail.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.service.config.RedSkyConfig
import com.myretail.service.redsky.AvailableToPromiseNetwork
import com.myretail.service.redsky.DeepRedLabels
import com.myretail.service.redsky.Item
import com.myretail.service.redsky.ProductDescription
import com.myretail.service.redsky.RedSkyApi
import com.myretail.service.redsky.RedSkyProduct
import com.myretail.service.redsky.RedSkyResponse
import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

import javax.inject.Inject
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@CompileStatic
@Configuration
class MockConfiguration extends RedSkyConfig {

    static RedSkyProduct existing = new RedSkyProduct(
            new AvailableToPromiseNetwork(13860428L),
            DeepRedLabels.INSTANCE,
            new Item(new ProductDescription('The Big Lebowski (Blu-ray) (Widescreen)'))
    )
    static RedSkyProduct notFound = new RedSkyProduct(
            null,
            null,
            new Item(null)
    )

    @Bean
    @Inject
    @Override
    RedSkyApi redSkyApi(ObjectMapper objectMapper) {
        Retrofit retrofit = redSkyRetrofit(objectMapper)
        BehaviorDelegate<RedSkyApi> delegate = new MockRetrofit.Builder(retrofit)
                .networkBehavior(perfectNetwork())
                .build()
                .create(RedSkyApi)
        return new MockRedSkyApi(delegate)
    }

    private NetworkBehavior perfectNetwork() {
        NetworkBehavior networkBehavior = NetworkBehavior.create()
        networkBehavior.errorPercent = 0
        networkBehavior.failurePercent = 0
        networkBehavior.variancePercent = 0
        networkBehavior.setDelay(0L, TimeUnit.MILLISECONDS)
        return networkBehavior
    }

    private static class MockRedSkyApi implements RedSkyApi {
        final BehaviorDelegate<RedSkyApi> delegate
        final Map<Long, RedSkyProduct> products = [
                (existing.availableToPromiseNetwork.productId): existing
        ]

        MockRedSkyApi(BehaviorDelegate<RedSkyApi> delegate) {
            this.delegate = delegate
        }

        @Override
        CompletableFuture<RedSkyResponse> get(long id, List<String> exclude) {
            RedSkyProduct redSkyProduct = products.get(id)
            return delegate
                    .returningResponse(new RedSkyResponse(redSkyProduct ?: notFound))
                    .get(id, exclude)
        }

    }

}
