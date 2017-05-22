package com.myretail.demo.product

import org.springframework.data.cassandra.mapping.PrimaryKey
import org.springframework.data.cassandra.mapping.Table
import java.math.BigDecimal

@Table("product")
data class ProductPO(
        @PrimaryKey val id: Long,
        val currentPriceValue: BigDecimal,
        val currentPriceCurrencyCode: CurrencyCode
)
