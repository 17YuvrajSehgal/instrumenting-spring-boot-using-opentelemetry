mvn clean package -DskipTests

docker build -t spring-boot-todo-app:latest .

AGENT_FILE=opentelemetry-javaagent.jar

if [ ! -f "${AGENT_FILE}" ]; then
  curl -L https://github.com/aws-observability/aws-otel-java-instrumentation/releases/download/v1.19.2/aws-opentelemetry-agent.jar --output ${AGENT_FILE}
fi

export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=otlp
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:5555
export OTEL_RESOURCE_ATTRIBUTES=service.name=spring-boot-todo-app,service.version=1.0

java -javaagent:${AGENT_FILE} -jar target/spring-boot-todo-app-0.0.1-SNAPSHOT.jar


