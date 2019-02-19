package com.myretail.api

class ProductSpec extends BaseSpec {

  void 'serializes to/from JSON'() {
    given:
    Product instance = new Product(
      13860428L,
      'The Big Lebowski (Blu-ray) (Widescreen)',
      new Price(13.49, CurrencyCode.USD)
    )
    String expected = jsonFromFile('product')

    when:
    String marshalled = objectMapper.writeValueAsString(instance)

    then:
    marshalled == expected

    when:
    Product unmarshalled = objectMapper.readValue(marshalled, Product)

    then:
    unmarshalled == instance
  }

}
