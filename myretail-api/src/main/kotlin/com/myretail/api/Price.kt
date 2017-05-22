package com.myretail.api

import java.math.BigDecimal

data class Price(
        val value: BigDecimal,
        val currencyCode: CurrencyCode
)
