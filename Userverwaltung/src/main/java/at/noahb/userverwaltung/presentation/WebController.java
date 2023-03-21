package at.noahb.userverwaltung.presentation;

import at.noahb.userverwaltung.domain.AnswerType;
import at.noahb.userverwaltung.domain.persistent.Question;
import at.noahb.userverwaltung.domain.security.RoleAuthority;
import at.noahb.userverwaltung.persistence.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class WebController {

    private QuestionRepository questionRepository;

    @GetMapping("/")
    public String index(Authentication authentication) {
        UserDetails userdetails = (UserDetails) authentication.getPrincipal();

        if (userdetails.getAuthorities().contains(RoleAuthority.ROLE_ADMIN)) {
            return "redirect:/questions";
        } else if (userdetails.getAuthorities().contains(RoleAuthority.ROLE_USER)) {
            return "redirect:/questions/overview";
        }

        return "redirect:/logout";
    }

    @GetMapping("/questions")
    public String questions(Model model, Authentication authentication) {
        UserDetails userdetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("newQuestion", new Question());
        model.addAttribute("user", userdetails);

        return "admin_newQuestion";
    }

    @GetMapping("/questions/overview")
    public String getQuestionOverview(Model model) {

        model.addAttribute("questions", questionRepository.findAll());
        return "questionOverview";
    }

    @PostMapping("/questions/new")
    public String postNewQuestion(@ModelAttribute Question newQuestion, BindingResult bindingResult) {

        return "redirect:/questions";
    }

    @GetMapping("/questions/{id}")
    public String getQuestion(@PathVariable Long id, Model model) {
        Optional<Question> possibleQuestion = questionRepository.findById(id);

        if (possibleQuestion.isEmpty()) {
            return "redirect:/questions";
        }

        model.addAttribute("question", possibleQuestion.get());
        model.addAttribute("answerTypes", AnswerType.values());

        return "questionDetailed";
    }
}
