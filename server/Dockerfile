FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
ENV TZ="Asia/Yekaterinburg"
ENTRYPOINT ["java","-jar","/app.jar"]