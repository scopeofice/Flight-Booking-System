FROM openjdk:8-jdk-alpine
COPY target/spring-boot-app.jar spring-boot-app.jar
ENTRYPOINT ["java","-jar","/spring-boot-app.jar"]
