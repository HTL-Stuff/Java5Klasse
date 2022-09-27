package at.noahb.employeetask.presentation;

import at.noahb.employeetask.domain.Employee;
import at.noahb.employeetask.domain.Task;
import at.noahb.employeetask.exceptions.EmployeeAlreadyExistsException;
import at.noahb.employeetask.exceptions.EmployeeNotFoundException;
import at.noahb.employeetask.persistence.EmployeeRepository;
import at.noahb.employeetask.persistence.TaskRepository;
import at.noahb.employeetask.payload.requests.EmployeeRequest;
import at.noahb.employeetask.payload.response.HoursWorked;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class Controller {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    public Controller(EmployeeRepository employeeRepository, TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping()
    public List<Employee> getAll(@RequestParam(name = "name", required = false) String partialName) throws EmployeeNotFoundException {
        if (partialName == null) {
            return employeeRepository.findAll();
        }

        var names = employeeRepository.findAllByNameContains(partialName);

        if (names.isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        return names;
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable String id) throws EmployeeNotFoundException {

        var employee = employeeRepository.findById(id);

        return employee.orElseThrow(EmployeeNotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<Employee> postEmployee(@RequestBody EmployeeRequest request) throws EmployeeAlreadyExistsException {
        System.out.println(request);

        if (request == null || request.notValid()) {
            throw new EmployeeAlreadyExistsException("Invalid request");
        }

        if (employeeRepository.existsById(request.getId())) {
            throw new EmployeeAlreadyExistsException("Employee with id " + request.getId() + " already exists");
        }

        var saved = employeeRepository.save(new Employee(request.getId(), request.getFirstName(), request.getLastName()));

        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(request.getId());
        var header = new HttpHeaders();
        header.set("Location", uri.toString());
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(header)
                .body(saved);
    }

    @GetMapping("/{id}/hoursWorked")
    public ResponseEntity<HoursWorked> getHoursWorked(@PathVariable String id) throws EmployeeNotFoundException {

        return ResponseEntity.ok(new HoursWorked(taskRepository.getTotalHoursWorkedByEmployee(getById(id))));
    }

    @GetMapping("/{id}/tasks")
    public List<Task> getAllTasksBetween(@PathVariable String id, @RequestParam String from, @RequestParam String to) throws EmployeeNotFoundException {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        if (toDate.isBefore(fromDate)) {
            throw new DateTimeException("to can not be before from");
        }

        return taskRepository.getTaskByAssignedEmployeeBetween(getById(id), fromDate , toDate);
    }

}
