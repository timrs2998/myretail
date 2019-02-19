package com.myretail.service.po

import com.myretail.api.CurrencyCode
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.math.BigDecimal

@Table("products_by_id")
data class ProductPO(
  @PrimaryKey val id: Long,
  val currentPriceValue: BigDecimal,
  val currentPriceCurrencyCode: CurrencyCode
)
