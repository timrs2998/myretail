package com.myretail.api

import java.math.BigDecimal
import javax.validation.constraints.NotNull

data class Price(
  @field:NotNull val value: BigDecimal,
  @field:NotNull val currencyCode: CurrencyCode
)
