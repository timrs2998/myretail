# Build jar file on JDK image
FROM library/openjdk:11.0.2-jdk-slim-stretch as builder
WORKDIR /app
COPY . /app/
RUN ./gradlew :myretail-service:bootJar

# JRE base image
FROM library/openjdk:11.0.2-jre-slim-stretch
LABEL maintainer="timrs2998@gmail.com"

# Copy jar from JDK image into JRE image
WORKDIR /
COPY --from=builder /app/myretail-service/build/libs/myretail-*.jar app.jar

# healthchecks
RUN apt-get update && \
  apt-get install -y curl
HEALTHCHECK --interval=5s --timeout=3s --retries=3 \
  CMD curl --fail http://localhost:8080/actuator/health || exit 1

# non-root user
RUN useradd -ms /bin/bash user
USER user

EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -XX:+UnlockExperimentalVMOptions -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
