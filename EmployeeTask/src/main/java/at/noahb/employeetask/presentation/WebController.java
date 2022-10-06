package at.noahb.employeetask.presentation;

import at.noahb.employeetask.domain.Employee;
import at.noahb.employeetask.persistence.EmployeeRepository;
import at.noahb.employeetask.persistence.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/employee/")
public record WebController(EmployeeRepository employeeRepository, TaskRepository taskRepository) {

    @GetMapping("home")
    public String getHomePage(Model employeeModel) {

        employeeModel.addAttribute("employees", employeeRepository.findAll());
        if (!employeeModel.containsAttribute("newEmployee"))
            employeeModel.addAttribute("newEmployee", new Employee());

        return "overview";
    }

    @GetMapping("detailed")
    public String actionGetDetailed(@RequestParam String id, Model employeeModel) {
        var optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employeeModel.addAttribute("employee", employee);
            employeeModel.addAttribute("tasks", taskRepository.findAllByAssignedEmployee(employee));
            return "detailed-view";
        } else {
            return "redirect:/web/employee/home";
        }
    }

}
