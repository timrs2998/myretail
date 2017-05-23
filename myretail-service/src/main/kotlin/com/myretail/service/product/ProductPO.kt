package com.myretail.service.product

import com.myretail.api.CurrencyCode
import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table
import java.math.BigDecimal

@Table("products_by_id")
data class ProductPO(
        @PrimaryKey val id: Long,
        val currentPriceValue: BigDecimal,
        val currentPriceCurrencyCode: CurrencyCode
)
