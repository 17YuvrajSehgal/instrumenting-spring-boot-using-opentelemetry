# instrumenting-spring-boot-using-opentelemetry

This project provides an example of a todo web application written in spring boot that produces telemetry data for traces and metrics using OpenTelemetry. 

Requirements
  Java 17+
  Maven 3.8.6+
  Docker
  
 
🏢 Running the microservice with Grafana, Grafana Tempo, and Prometheus as observability backend.
1. Start the containers using docker: docker compose up -d
2. Run the file:  run.sh
3.Access the application at http://localhost:8282 (Username: user, Password: generated in the terminal after compilation)
4. Access the Grafana UI: http://localhost:3000

**REPORT**:
ANSWER 2: 
The application has been instrumented using docker. The process creates images in the docker and sets up various visualization tools on the local host. I have used Grafana for holding the traces and Prometheus for visualization. My collector exposes endpoints at local host 5555 and 6666 from where Grafana and Prometheus scape the data.
When we first run the app, the output looks like this: 
 

Each method has its own span that represents different attributes:
 
 

These are the two types of metrics that I have used in the instrumentation. The first metrics counts the number of times the home page is executed. It is a monotonic type of metric as it can only go up.
 It uses the meter that was declared as follows: 
 
The second metrics is a non-monotonic metric as it calculates the total memory consumption by the program. It can go up as well as down. The @PostConstruct makes sure that we don’t have to call the method for the metrics. It calls this method itself after application is build.
Various kinds of attributes have been captured by the instrumentation process as follows. 
 

The visualization of the total heap memory consumption by the application is as follows:  


