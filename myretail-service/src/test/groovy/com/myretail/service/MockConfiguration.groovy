package com.myretail.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.service.redsky.RedskyApi
import com.myretail.service.redsky.AvailableToPromiseNetwork
import com.myretail.service.redsky.DeepRedLabels
import com.myretail.service.redsky.Item
import com.myretail.service.redsky.RedskyProduct
import com.myretail.service.redsky.ProductDescription
import com.myretail.service.redsky.RedskyResponse
import com.myretail.service.config.RedSkyConfig
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

    static RedskyProduct existing = new RedskyProduct(
            new AvailableToPromiseNetwork(13860428L),
            DeepRedLabels.INSTANCE,
            new Item(new ProductDescription('The Big Lebowski (Blu-ray) (Widescreen)'))
    )
    static RedskyProduct notFound = new RedskyProduct(
            null,
            null,
            new Item(null)
    )

    @Bean
    @Inject
    @Override
    RedskyApi redskyApi(ObjectMapper objectMapper) {
        Retrofit retrofit = redskyRetrofit(objectMapper)
        BehaviorDelegate<RedskyApi> delegate = new MockRetrofit.Builder(retrofit)
                .networkBehavior(perfectNetwork())
                .build()
                .create(RedskyApi)
        return new MockRedskyApi(delegate)
    }

    private NetworkBehavior perfectNetwork() {
        NetworkBehavior networkBehavior = NetworkBehavior.create()
        networkBehavior.errorPercent = 0
        networkBehavior.failurePercent = 0
        networkBehavior.variancePercent = 0
        networkBehavior.setDelay(0L, TimeUnit.MILLISECONDS)
        return networkBehavior
    }

    private static class MockRedskyApi implements RedskyApi {
        final BehaviorDelegate<RedskyApi> delegate
        final Map<Long, RedskyProduct> products = [
                (existing.availableToPromiseNetwork.productId): existing
        ]

        MockRedskyApi(BehaviorDelegate<RedskyApi> delegate) {
            this.delegate = delegate
        }

        @Override
        CompletableFuture<RedskyResponse> get(long id, List<String> exclude) {
            RedskyProduct redskyProduct = products.get(id)
            return delegate
                    .returningResponse(new RedskyResponse(redskyProduct ?: notFound))
                    .get(id, exclude)
        }

    }

}
