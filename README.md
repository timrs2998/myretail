# myretail

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://travis-ci.org/timrs2998/myretail.svg?branch=master)](https://travis-ci.org/timrs2998/myretail)

myretail is an implementation of a products API that aggregates price 
details by ID. It is a [Spring Boot](https://projects.spring.io/spring-boot/) 
microservice backed by a [Cassandra](https://cassandra.apache.org/) database
and the RedSky API.

## Build and Run

Assuming you installed [git](https://git-scm.com/downloads), you can clone
and enter the project directory:

```bash
$ git clone git@github.com:timrs2998/myretail.git
$ cd myretail/
```

### Start Cassandra

The app will fail to run and build without Cassandra. To start Cassandra, run:

```bash
# Mac
$ brew install cassandra
$ brew services start cassandra

# docker
$ docker run -p 9042:9042 -t library/cassandra:3.11.0

# docker-compose
$ docker-compose up db

# arch (from AUR)
$ yay -S cassandra
$ systemctl start cassandra
```

### Run the app

Assuming you installed [JDK 8](http://www.oracle.com/technetwork/pt/java/javase/downloads/index.html)
you can build and run the project:

```bash
# Build and run jar
$ ./gradlew build
$ java -jar myretail-service/build/libs/myretail-service*.jar

# Or run via bootRun task
$ ./gradlew myretail-service:bootRun
```

### Run with docker

As an alternative to building from source with the JDK, you can use Docker. All 
builds are tagged and pushed to [Docker Hub](https://hub.docker.com/r/timrs2998/myretail/).

To pull down and run the latest docker image:

```bash
$ docker run -p 8080:8080 timrs2998/myretail
```

### Run with docker-compose

Start service:

```bash
$ docker-compose build
$ docker-compose up db
# wait for Cassandra to start listening
$ docker-compose up app
```

<!--
### Run with Minikube

Start service:

```bash
# Start minikube
$ minikube start --cpus 4 --memory 8192

# Build image in kubernetes (optional)
$ eval $(minikube docker-env)
$ docker build -t timrs2998/myretail .

# Deploy
$ kubectl apply --filename kubernetes/cassandra.yml
# wait for Cassandra to start listening
$ kubectl apply --filename kubernetes/myretail.yml

# Wait for deployment to finish
$ watch -n 0.5 kubectl get pods

# Verify service is running
$ minikube service myretail --url
$ curl 127.0.0.1:8080/actuator/health

# Cleanup
$ minikube delete
```
-->

### Run with Docker Swarm

Similar to Kubernetes, the myretail app can be deployed on
[Docker Swarm](https://docs.docker.com/engine/swarm/):

```bash
# Start swarm mode and deploy stack
$ docker swarm init --advertise-adr 127.0.0.1
$ docker stack deploy --compose-file docker-compose.yml myretail-swarm-demo

# Wait for deployment to finish
$ watch -n 0.5 docker ps
$ watch -n 0.5 docker-compose ps

# Verify service is running
$ curl 127.0.0.1:8080/actuator/health

# Cleanup
$ docker stack rm myretail-swarm-demo
$ docker swarm leave --force
```

## Usage

Once running, the Cassandra database will not have any price information. Any
GET request would return either a 404 (if missing in RedSky) or a response
without a price.

You can perform PUT requests to add or update price information for any
existing product:

ie: `PUT http://127.0.0.1:8080/products/13860428` with body:

```bash
$ curl 'http://127.0.0.1:8080/products/13860428' \
  -X PUT \
  -H 'Content-Type: application/json' \
  -d '{'\
'  "id": 13860428,'\
'  "name": "The Big Lebowski (Blu-ray)",'\
'  "current_price": {'\
'    "value": 123.42,'\
'    "currency_code": "USD"'\
'  }'\
'}'
```

or you can perform a GET request to aggregate information from Cassandra and RedSky:

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

 * [/](https://www.myretail.pw/) (HAL API Browser)
  * [/actuator/health](https://www.myretail.pw/actuator/health)
  * [/actuator/info](https://www.myretail.pw/actuator/info)

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
