FROM openjdk:8-jre-alpine
VOLUME /tmp
ADD myretail-service/build/libs/myretail-*.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
