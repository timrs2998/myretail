package com.myretail.api

import javax.validation.Valid
import javax.validation.constraints.NotNull

data class Product(
  @field:NotNull val id: Long,
  @field:NotNull val name: String,
  @field:Valid val currentPrice: Price?
)
