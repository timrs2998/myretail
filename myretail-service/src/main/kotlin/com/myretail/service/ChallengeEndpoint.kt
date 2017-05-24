package com.myretail.service

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/.well-known/acme-challenge")
@RestController
class ChallengeEndpoint {

    @RequestMapping(value = "/kE3sMDJJUeshQnV8gEjrEaMi2IGU-rgQHbIYl7RRVV8", method = arrayOf(GET))
    @ResponseBody
    fun getChallengeOne() = "kE3sMDJJUeshQnV8gEjrEaMi2IGU-rgQHbIYl7RRVV8.M1Liq7-0HQFfHU-Kt-rbtApdlyXRY6Py4asgoFgupT4"

    @RequestMapping(value = "/hweSxAcm8xcMGsce7d2bkh1QjttmVtYhpIpxn97WIgc", method = arrayOf(GET))
    @ResponseBody
    fun getChallengeTwo() = "hweSxAcm8xcMGsce7d2bkh1QjttmVtYhpIpxn97WIgc.M1Liq7-0HQFfHU-Kt-rbtApdlyXRY6Py4asgoFgupT4"

}
