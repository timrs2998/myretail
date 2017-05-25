package com.myretail.service.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class ViewConfig : WebMvcConfigurerAdapter() {

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/docs").setViewName("forward:/docs/index.html")
        registry.addViewController("/_ah/health").setViewName("forward:/health")
    }

}
