package co.sohamds.spring.todo.controllers;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.DoubleCounter;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.sohamds.spring.todo.domain.Todo;
import co.sohamds.spring.todo.repository.TodoRepository;

import javax.annotation.PostConstruct;

import static co.sohamds.spring.todo.controllers.Constants.*;
import static java.lang.Runtime.getRuntime;

@Controller
public class TodoController {

	private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
	public final Tracer tracer = GlobalOpenTelemetry.getTracer("io.opentelemetry.traces.Todo");
	private final Meter meter = GlobalOpenTelemetry.meterBuilder("io.opentelemetry.metrics1").build();
	private LongCounter numberOfExecutions;

	@PostConstruct
	public void createMetrics(){
		numberOfExecutions = meter
				.counterBuilder(NUMBER_OF_EXEC_NAME)
				.setDescription(NUMBER_OF_EXEC_DESCRIPTION)
				.setUnit("int")
				.build();

		meter.gaugeBuilder(HEAP_MEMORY_NAME)
				.setDescription(HEAP_MEMORY_DESCRIPTION)
				.buildWithCallback(
						r ->{
							r.record(getRuntime().totalMemory()-getRuntime().freeMemory());
						}
				);
	}

	@Autowired
	TodoRepository todoRepository;

	@GetMapping
	public String index() {
		return "index.html";
	}

	@GetMapping("/todos")
	public String todos(Model model) {
		Span span = tracer.spanBuilder("Main_Page_span").startSpan();
		span.addEvent("Init");
		span.setAttribute("http.method","GET");
		try(Scope ss = span.makeCurrent()){
			numberOfExecutions.add(1);
			model.addAttribute("todos", todoRepository.findAll());
		}finally {
			span.addEvent("End");
			span.end();
		}
		return "todos";
	}

	@PostMapping("/todoNew")
	public String add(@RequestParam String todoItem, @RequestParam
	String status, Model model) {

		Span span = tracer.spanBuilder("Add_Operation_Span").startSpan();
		span.addEvent("Init");
		span.setAttribute("http.method","POST");

		try(Scope ignored = span.makeCurrent()){

			logger.info("Adding a new task to the list.");
			Todo todo = new Todo(todoItem, status);
			todo.setTodoItem(todoItem);
			todo.setCompleted(status);
			todoRepository.save(todo);
			model.addAttribute("todos", todoRepository.findAll());

		}finally {
			span.addEvent("End");
			span.end();
		}
		return "redirect:/todos";
	}

	@PostMapping("/todoDelete/{id}")
	public String delete(@PathVariable long id, Model model) {
		Span span = tracer.spanBuilder("Delete_Operation_Span").startSpan();
		span.addEvent("Init");
		span.setAttribute("http.method","POST");

		try{
			todoRepository.deleteById(id);
			model.addAttribute("todos", todoRepository.findAll());
		}finally {
			span.addEvent("End");
			span.end();
		}

		return "redirect:/todos";
	}

	@PostMapping("/todoUpdate/{id}")
	public String update(@PathVariable long id, Model model) {
		Span span = tracer.spanBuilder("Delete_Operation_Span").startSpan();
		span.addEvent("Init");
		span.setAttribute("http.method","POST");

		try{
			Todo todo = todoRepository.findById(id).get();
			if ("Yes".equals(todo.getCompleted())) {
				todo.setCompleted("No");
			} else {
				todo.setCompleted("Yes");
			}
			todoRepository.save(todo);
			model.addAttribute("todos", todoRepository.findAll());
		}finally {
			span.addEvent("End");
			span.end();
		}

		return "redirect:/todos";
	}
}