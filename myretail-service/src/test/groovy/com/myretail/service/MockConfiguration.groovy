package com.myretail.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.service.api.RedskyApi
import com.myretail.service.api.RedskyAvailableToPromiseNetwork
import com.myretail.service.api.RedskyDeepRedLabels
import com.myretail.service.api.RedskyItem
import com.myretail.service.api.RedskyProduct
import com.myretail.service.api.RedskyProductDescription
import com.myretail.service.api.RedskyResponse
import com.myretail.service.config.RedskyConfig
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
class MockConfiguration extends RedskyConfig {

    static RedskyProduct existing = new RedskyProduct(
            new RedskyAvailableToPromiseNetwork(13860428L),
            new RedskyDeepRedLabels(),
            new RedskyItem(new RedskyProductDescription('The Big Lebowski (Blu-ray) (Widescreen)'))
    )
    static RedskyProduct notFound = new RedskyProduct(
            null,
            null,
            new RedskyItem(null)
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
                (existing.availableToPromiseNetwork.product_id): existing
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
