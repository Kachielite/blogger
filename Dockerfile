FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY build/libs/blogger-0.0.1-SNAPSHOT.jar blogger.jar
ENTRYPOINT ["java","-jar","blogger.jar"]