package co.sohamds.spring.todo.domain;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Todo {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private long id;
private String todoItem;
private String completed;



public Todo(String todoItem, String completed) {
	super();
	this.todoItem = todoItem;
	this.completed = completed;
}

	// add metrics for this class
	private static final Meter meter = MeterProvider.noop().get("Todo meter");
	private static final LongCounter totalTodosCounter = meter.counterBuilder("total_todos")
			.setDescription("Total number of todos")
			.setUnit("1")
			.build();
	private static final LongCounter completedTodosCounter = meter.counterBuilder("completed_todos")
			.setDescription("Total number of completed todos")
			.setUnit("1")
			.build();

	// method to increment the counters
	public void incrementCounters() {
		totalTodosCounter.add(1);
		if (this.completed.equals("Yes")) {
			completedTodosCounter.add(1);
		}
	}




}