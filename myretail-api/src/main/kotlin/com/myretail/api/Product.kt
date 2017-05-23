package com.myretail.api

data class Product(
        val id: Long,
        val name: String,
        val currentPrice: Price?
)
