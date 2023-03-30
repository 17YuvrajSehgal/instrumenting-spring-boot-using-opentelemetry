# instrumenting-spring-boot-using-opentelemetry

This project provides an example of a todo web application written in spring boot that produces telemetry data for traces and metrics using OpenTelemetry. 

Requirements
  Java 17+
  Maven 3.8.6+
  Docker
  
 
🏢 Running the microservice with Grafana, Grafana Tempo, and Prometheus as observability backend.
1. Start the containers using docker: docker compose up -d
2. Run the file:  run.s
3. Access the application at http://localhost:8282: Username: user, Password: generated in the terminal after compilation
4. Access the Grafana UI: http://localhost:3000

**REPORT**:
The application has been instrumented using docker. The process creates images in the docker and sets up various visualization tools on the local host. I have used Grafana for holding the traces and Prometheus for visualization. My collector exposes endpoints at local host 5555 and 6666 from where Grafana and Prometheus scape the data.
When we first run the app, the output looks like this: 
 ![image](https://user-images.githubusercontent.com/81456735/228794621-ba6a5ef4-82e3-48ea-9867-854971feec99.png)
 
Each method has its own span that represents different attributes:
![image](https://user-images.githubusercontent.com/81456735/228794977-4ff8ad55-4dfc-4f31-9d6a-2b0425ed878d.png)
![image](https://user-images.githubusercontent.com/81456735/228795006-57b24edb-51d2-41f3-8374-86780e6a6c1c.png)

These are the two types of metrics that I have used in the instrumentation. The first metrics counts the number of times the home page is executed. It is a monotonic type of metric as it can only go up.
 It uses the meter that was declared as follows: 
![image](https://user-images.githubusercontent.com/81456735/228795038-1c91bf4a-b709-4d5a-83c0-372cf58b1487.png)

 
The second metrics is a non-monotonic metric as it calculates the total memory consumption by the program. It can go up as well as down. The @PostConstruct makes sure that we don’t have to call the method for the metrics. It calls this method itself after application is build.
Various kinds of attributes have been captured by the instrumentation process as follows. 
![image](https://user-images.githubusercontent.com/81456735/228795070-29f66cb8-7c81-466f-8bd9-2ca0ad3fac43.png)

The visualization of the total heap memory consumption by the application is as follows:  
![image](https://user-images.githubusercontent.com/81456735/228795101-26b8257f-eae4-485b-a428-3419b5d49590.png)


