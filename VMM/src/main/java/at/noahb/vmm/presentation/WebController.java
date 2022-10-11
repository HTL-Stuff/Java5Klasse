package at.noahb.vmm.presentation;

import at.noahb.vmm.domain.Grade;
import at.noahb.vmm.domain.Student;
import at.noahb.vmm.domain.Subject;
import at.noahb.vmm.persistence.GradeRepository;
import at.noahb.vmm.persistence.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

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

        Student student = optionalStudent.get();

        model.addAttribute("grades", student.getGrades());
        model.addAttribute("studentId", student.getStudentId());
        model.addAttribute("newGrade", new Grade());
        model.addAttribute("subjects", Subject.values());

        return "detailed";
    }

    @PostMapping("/{id}/newGrade")
    public String postNewGrade(@PathVariable(name = "id") Integer studentId,
                               @Valid @ModelAttribute("newGrade") Grade grade,
                               BindingResult bindingResult, Model model) {


        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            return "redirect:/web/students/home";
        }

        if (bindingResult.hasErrors())
            return "redirect:/web/students/" + studentId;

        grade.setStudent(student.get());

        gradeRepository.save(grade);

        return "redirect:/web/students/home";
    }
}
