FROM openjdk:8-jdk-alpine
VOLUME /tmp

ADD target/*.jar app.jar

ENV JAVA_OPTS=""
ENV JASYPT_PATH=""
ENV DB_PASS=""

EXPOSE 8080

RUN chmod 755 app.jar
USER 1001

ENTRYPOINT exec java $JAVA_OPTS \
-jar /app.jar