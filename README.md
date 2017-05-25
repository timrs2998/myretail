# myretail

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://travis-ci.org/timrs2998/myretail.svg?branch=master)](https://travis-ci.org/timrs2998/myretail)

myretail is an implementation of a products API that aggregates price 
details by ID. It is a [Spring Boot](https://projects.spring.io/spring-boot/) 
microservice backed by a [Cassandra](https://cassandra.apache.org/) database
and the redsky API.

## Build and Run

Assuming you installed [JDK 8](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwibkPOQ24fUAhUL3YMKHZwBAUoQFggoMAA&url=http%3A%2F%2Fwww.oracle.com%2Ftechnetwork%2Fjava%2Fjavase%2Fdownloads%2Fjdk8-downloads-2133151.html&usg=AFQjCNEfygdKmZ1D2xJzvYIvnYbmmXWqsA&sig2=QDn2YLxSU9EOSn3BpuzzfA)
and [git](https://git-scm.com/downloads), you can build and run the project:

```bash
$ git clone git@github.com:timrs2998/myretail.git
$ cd myretail/
$ ./gradlew build
$ java -jar myretail-service/build/libs/myretail-service*.jar
```

To ease setup, the myretail will default to an in-memory Cassandra 
database but [can be configured](./myretail-service/src/main/resources/application.yml)
otherwise. 

## Run with docker

As an alternative to building the source, you can use Docker. All builds are 
tagged and pushed to [Docker Hub](https://hub.docker.com/r/timrs2998/myretail/).

To pull down and run the latest docker image:

```bash
$ docker run -p 8080:8080 timrs2998/myretail
```

## Usage

Once running, the Cassandra database will not have any price information. Any
GET request would return either a 404 (if missing in redsky) or a response 
without a price.

You can perform PUT requests to add or update price information for any
existing product:

ie: `PUT http://localhost:8080/products/13860428` with body:

```json
{
  "id": 13860428,
  "name": "The Big Lebowski (Blu-ray)",
  "current_price": {
    "value": 123.42,
    "currency_code": "USD"
  }
}
```

or you can perform a GET request to aggregate information from Cassandra and redsky:

 * [GET http://localhost:8080/products/16696652](http://localhost:8080/products/16696652)
 * [GET http://localhost:8080/products/13860428](http://localhost:8080/products/13860428)

## Hosted Instance

Instead of running myretail locally, you can see a live demo. The service is
hosted on Google App Engine, has a domain name through Google Domains, and
has a certificate through Let's Encrypt.

Visit [https://www.myretail.pw/](https://www.myretail.pw/) for the live demo.

Sample queries:
 * [GET https://www.myretail.pw/products/16696652](https://www.myretail.pw/products/16696652)
 * [GET https://www.myretail.pw/products/13860428](https://www.myretail.pw/products/13860428)

## Documentation

API documentation is written using [asciidoctor](http://asciidoctor.org/). The
integration tests generate documentation snippets and ensure they are always
up-to-date.

Some endpoints of interest include:

 * [/docs](https://www.myretail.pw/docs)
 * [/docs-actuator](https://www.myretail.pw/docs-actuator)
  * [/health](https://www.myretail.pw/health)
  * [/info](https://www.myretail.pw/info)

## Testing

There are several unit and integration tests written with 
[Spock](http://spockframework.org/) in [Groovy](http://groovy-lang.org/). Use
`./gradlew test` to run them locally.

Whenever a change is made to the GitHub project, Travis runs all the tests,
builds the code, publishes a docker image, publishes artifacts to bintray, and
deploys the latest code to Google Container Engine.

## See also

 * [Docker Hub Repo](https://hub.docker.com/r/timrs2998/myretail/)
 * [Travis Job](https://travis-ci.org/timrs2998/myretail)
