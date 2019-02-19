package com.myretail.api

class PriceSpec extends BaseSpec {

  void 'serializes to/from JSON'() {
    given:
    Price instance = new Price(12.0, CurrencyCode.USD)
    String expected = jsonFromFile('price')

    when:
    String marshalled = objectMapper.writeValueAsString(instance)

    then:
    marshalled == expected

    when:
    Price unmarshalled = objectMapper.readValue(marshalled, Price)

    then:
    unmarshalled == instance
  }

}
