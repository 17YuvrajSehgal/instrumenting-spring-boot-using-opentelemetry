#FROM maven:3.8.1-openjdk-17-slim
#
#VOLUME /tmp
#ADD . /usr/src/app
#WORKDIR /usr/src/app
#
#RUN mvn clean package -DskipTests
#RUN curl -L https://github.com/aws-observability/aws-otel-java-instrumentation/releases/download/v1.19.2/aws-opentelemetry-agent.jar --output opentelemetry-javaagent-all.jar
#ENTRYPOINT [ "java", "-javaagent:opentelemetry-javaagent-all.jar", "-jar", "target/hello-app-1.0.jar" ]


FROM maven:3.8.1-openjdk-17-slim
LABEL maintainer='Yuvraj Sehgal'

ADD target/spring-boot-todo-app-0.0.1-SNAPSHOT.jar spring-boot-todo-app.jar
ENTRYPOINT ["java", "-jar", "spring-boot-todo-app.jar"]