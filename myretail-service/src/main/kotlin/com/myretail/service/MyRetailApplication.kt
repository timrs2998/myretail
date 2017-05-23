package com.myretail.service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MyRetailApplication

fun main(args: Array<String>) {
    SpringApplication.run(MyRetailApplication::class.java, *args)
}
