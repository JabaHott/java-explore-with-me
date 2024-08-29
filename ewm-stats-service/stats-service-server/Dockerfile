FROM amazoncorretto:21-alpine-jdk
COPY target/*.jar stats-service-server.jar
ENTRYPOINT ["java","-jar","/stats-service-server.jar"]