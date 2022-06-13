FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
EXPOSE 8080
ADD target/sezamo-0.0.1-SNAPSHOT.jar sezamo-app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/sezamo-app.jar"]