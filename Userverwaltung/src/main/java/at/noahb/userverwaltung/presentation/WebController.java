package at.noahb.userverwaltung.presentation;

import at.noahb.userverwaltung.domain.AnswerDistribution;
import at.noahb.userverwaltung.domain.AnswerType;
import at.noahb.userverwaltung.domain.persistent.Answer;
import at.noahb.userverwaltung.domain.persistent.Question;
import at.noahb.userverwaltung.domain.security.RoleAuthority;
import at.noahb.userverwaltung.persistence.AnswerRepository;
import at.noahb.userverwaltung.persistence.QuestionRepository;
import at.noahb.userverwaltung.persistence.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
@AllArgsConstructor
public class WebController {

    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Authentication authentication) {
        if (checkAuthentication(authentication)) {
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

        if (!model.containsAttribute("newQuestion")) {
            model.addAttribute("newQuestion", new Question());
        }
        model.addAttribute("user", userdetails);

        return "admin_newQuestion";
    }

    @GetMapping("/questions/overview")
    public String getQuestionOverview(Model model, Authentication authentication) {
        if (checkAuthentication(authentication)) {
            return "redirect:/login";
        }

        UserDetails userdetails = (UserDetails) authentication.getPrincipal();
        String role = userdetails.getAuthorities().contains(RoleAuthority.ROLE_ADMIN) ? "ROLE_ADMIN" : "ROLE_USER";

        if ("ROLE_ADMIN".equals(role)) {
            model.addAttribute("questions", questionRepository.findAll());
            List<AnswerDistribution> answerDistribution = answerRepository.getAnswerDistribution();

            if (answerDistribution == null) {
                answerDistribution = Collections.emptyList();
            }

            Collections.sort(answerDistribution);

            model.addAttribute("distribution", answerDistribution);
        } else {
            model.addAttribute("distribution", Collections.emptyList());
            model.addAttribute("questions", questionRepository.findAllByExpiryDateNotExpiredAndAnswersNotContains(userdetails.getUsername()));
        }
        model.addAttribute("answerTypes", AnswerType.values());
        model.addAttribute("role", role);
        model.addAttribute("user", userdetails);

        return "questionOverview";
    }

    @PostMapping("/questions/new")
    public String postNewQuestion(@Valid @ModelAttribute("newQuestion") Question newQuestion, BindingResult bindingResult, Model model, Authentication authentication) {
        if (checkAuthentication(authentication)) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/questions";
        }

        newQuestion = questionRepository.save(newQuestion);

        return "redirect:/questions/" + newQuestion.getId();
    }

    @GetMapping("/questions/{id}")
    public String getQuestion(@PathVariable Long id, Model model, Authentication authentication) {
        if (checkAuthentication(authentication)) {
            return "redirect:/login";
        }

        UserDetails userdetails = (UserDetails) authentication.getPrincipal();
        Optional<Question> possibleQuestion = questionRepository.findById(id);
        String role = userdetails.getAuthorities().contains(RoleAuthority.ROLE_ADMIN) ? "ROLE_ADMIN" : "ROLE_USER";

        if (possibleQuestion.isEmpty()) {
            return "redirect:/questions";
        }

        Question question = possibleQuestion.get();

        Answer answer = question.getAnswers().stream()
                .filter(a -> a.getAnswerer().getEmail().equals(userdetails.getUsername()))
                .findFirst()
                .orElse(new Answer());

        System.out.println(answer);
        Answer modelAnswer = new Answer();
        modelAnswer.setAnswerType(answer.getAnswerType());

        model.addAttribute("answer", modelAnswer);
        model.addAttribute("question", question);
        model.addAttribute("answerTypes", AnswerType.values());
        model.addAttribute("user", userdetails);
        model.addAttribute("role", role);

        return "questionDetailed";
    }

    @PostMapping("/questions/{id}/update")
    public String postUpdateQuestion(@PathVariable Long id, @ModelAttribute Answer answer, BindingResult bindingResult, Authentication authentication) {
        if (checkAuthentication(authentication)) {
            return "redirect:/login";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Question> possibleQuestion = questionRepository.findById(id);

        if (possibleQuestion.isEmpty()) {
            return "redirect:/questions";
        }
        if (userDetails == null) {
            throw new IllegalStateException("UserDetails not found in model! (This should never happen!");
        }

        var user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found in database! (This should never happen!)"));

        Question questionToUpdate = possibleQuestion.get();
        answer.setAnswerer(user);
        answer = answerRepository.save(answer);
        questionToUpdate.addAnswer(answer);

        questionRepository.save(questionToUpdate);

        return "redirect:/questions/overview";
    }

    private boolean checkAuthentication(Authentication authentication) {
        return authentication == null || !authentication.isAuthenticated();
    }
}
