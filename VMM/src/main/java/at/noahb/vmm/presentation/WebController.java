package at.noahb.vmm.presentation;

import at.noahb.vmm.domain.Student;
import at.noahb.vmm.persistence.GradeRepository;
import at.noahb.vmm.persistence.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("web/students/")
public record WebController(StudentRepository studentRepository, GradeRepository gradeRepository) {

    @GetMapping("home")
    public String getHome(Model model) {

        model.addAttribute("students", studentRepository.findAll());

        return "overview";
    }

    @GetMapping("{id}")
    public String getDetailed(@PathVariable Integer id, Model model) {


        var optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isEmpty()) {
            return "redirect:/web/students/home";
        }

        model.addAttribute("grades", optionalStudent.get().getGrades());

        return "detailed";
    }
}
