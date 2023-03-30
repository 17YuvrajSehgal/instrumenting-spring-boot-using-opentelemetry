package co.sohamds.spring.todo;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import co.sohamds.spring.todo.domain.Todo;
import co.sohamds.spring.todo.repository.TodoRepository;

@SpringBootApplication
public class SpringBootTodoAppApplication implements CommandLineRunner {

	private final Meter meter = GlobalOpenTelemetry.meterBuilder("io.opentelemetry.metrics.CPU").build();

	LongCounter cpuUsageCounter = meter
			.counterBuilder("cpu.usage")
			.setDescription("Total CPU usage")
			.setUnit("1")
			.build();
    @Autowired
    public TodoRepository todoRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTodoAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Todo test = Todo.builder().id(10).completed("its completed").todoItem("python ML").build();
        System.out.println(test.toString());
        List<Todo> todos = Arrays.asList(new Todo("Learn Spring", "Yes"), new Todo("Learn Driving", "No"), new Todo("Go for a Walk", "No"), new Todo("Cook Dinner", "Yes"));
        todos.forEach(todoRepository::save);

		// Calculate CPU usage
		double cpuUsage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
		cpuUsageCounter.add(1);

    }
}
