package at.noahb.userverwaltung.presentation;

import at.noahb.userverwaltung.domain.AnswerType;
import at.noahb.userverwaltung.domain.persistent.Answer;
import at.noahb.userverwaltung.domain.persistent.Question;
import at.noahb.userverwaltung.domain.persistent.User;
import at.noahb.userverwaltung.domain.security.RoleAuthority;
import at.noahb.userverwaltung.persistence.AnswerRepository;
import at.noahb.userverwaltung.persistence.QuestionRepository;
import at.noahb.userverwaltung.persistence.UserRepository;
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
    private AnswerRepository answerRepository;

    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

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

        if (userdetails.getAuthorities().contains(RoleAuthority.ROLE_USER)) {
            return "redirect:/";
        }

        model.addAttribute("newQuestion", new Question());
        model.addAttribute("user", userdetails);

        return "admin_newQuestion";
    }

    @GetMapping("/questions/overview")
    public String getQuestionOverview(Model model, Authentication authentication) {
        UserDetails userdetails = (UserDetails) authentication.getPrincipal();
        String role = userdetails.getAuthorities().contains(RoleAuthority.ROLE_ADMIN) ? "ROLE_ADMIN" : "ROLE_USER";

        model.addAttribute("questions", questionRepository.findAll());
        model.addAttribute("role", role);
        model.addAttribute("user", userdetails);

        return "questionOverview";
    }

    @PostMapping("/questions/new")
    public String postNewQuestion(@ModelAttribute Question newQuestion, BindingResult bindingResult) {

        return "redirect:/questions";
    }

    @GetMapping("/questions/{id}")
    public String getQuestion(@PathVariable Long id, Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        UserDetails userdetails = (UserDetails) authentication.getPrincipal();
        Optional<Question> possibleQuestion = questionRepository.findById(id);
        String role = userdetails.getAuthorities().contains(RoleAuthority.ROLE_ADMIN) ? "ROLE_ADMIN" : "ROLE_USER";

        if (possibleQuestion.isEmpty()) {
            return "redirect:/questions";
        }

        Answer answer = answerRepository.findByAnswererEmailAndId(userdetails.getUsername(), id).orElse(new Answer());

        Answer answer1 = new Answer();
        answer1.setAnswerType(answer.getAnswerType());

        System.out.println(answer.getAnswerType());
        System.out.println(answer1.getAnswerType());

        model.addAttribute("answer", answer1);
        model.addAttribute("question", possibleQuestion.get());
        model.addAttribute("answerTypes", AnswerType.values());
        model.addAttribute("user", userdetails);
        model.addAttribute("role", role);

        return "questionDetailed";
    }

    @PostMapping("/questions/{id}/update")
    public String postUpdateQuestion(@PathVariable Long id, @ModelAttribute Answer answer, BindingResult bindingResult, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Question> possibleQuestion = questionRepository.findById(id);

        if (possibleQuestion.isEmpty()) {
            System.out.println("empty");
            return "redirect:/questions";
        }

        if (userDetails == null) {
            throw new IllegalStateException("UserDetails not found in model! (This should never happen!");
        }

        var possibleUser = userRepository.findByEmail(userDetails.getUsername());

        if (possibleUser.isEmpty()) {
            throw new IllegalStateException("User not found in database! (This should never happen!");
        }

        Question questionToUpdate = possibleQuestion.get();
        answer.setAnswerer(possibleUser.get());
        answer = answerRepository.save(answer);
        questionToUpdate.addAnswer(answer);

        questionRepository.save(questionToUpdate);

        return "redirect:/questions/" + id;
    }
}
