# Build jar file on JDK image
FROM openjdk:8u131-jdk-alpine as builder
WORKDIR /app
COPY . /app/
RUN ./gradlew assemble -x test

# JRE base image
FROM openjdk:8u131-jre-alpine
LABEL maintainer="timrs2998@gmail.com"

# Copy jar from JDK image into JRE image
WORKDIR /
COPY --from=builder /app/myretail-service/build/libs/myretail-*.jar app.jar

# healthchecks
RUN apk add --no-cache curl
HEALTHCHECK --interval=5s --timeout=3s --retries=3 CMD curl --fail http://localhost:8080/health || exit 1

# non-root user
RUN adduser -SDHs /bin/bash user
USER user

EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
