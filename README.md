# instrumenting-spring-boot-using-opentelemetry

This project provides an example of a todo web application written in spring boot that produces telemetry data for traces and metrics using OpenTelemetry. 

Requirements
  Java 17+
  Maven 3.8.6+
  Docker
  
 
🏢 Running the microservice with Grafana, Grafana Tempo, and Prometheus as observability backend.
1. Start the containers using docker:

  docker compose up -d
  
2. Run the file:
  run.sh

3.Access the application at http://localhost:8282 (Username: user, Password: generated in the terminal after compilation)

4. Access the Grafana UI: http://localhost:3000
