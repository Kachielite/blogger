FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY build/libs/blogger-0.0.1-SNAPSHOT.jar /opt/app/blogger.jar
ENTRYPOINT ["java","-Dspring.profiles.active=local","-jar","/opt/app/blogger.jar"]