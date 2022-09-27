package at.noahb.employeetask.presentation;

import at.noahb.employeetask.domain.Employee;
import at.noahb.employeetask.persistence.EmployeeRepository;
import at.noahb.employeetask.persistence.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/employee/")
public record EmployeeController(EmployeeRepository employeeRepository, TaskRepository taskRepository) {

    @GetMapping("home")
    public String getHomePage(Model employeeModel) {

        employeeModel.addAttribute("employees", employeeRepository.findAll());
        employeeModel.addAttribute("newEmployee", new Employee());

        return "overview";
    }

    @GetMapping("detailed")
    public String actionGetDetailed(@RequestParam String id) {
        System.out.println(employeeRepository.findById(id));

        return "detailed-view";
    }

}
