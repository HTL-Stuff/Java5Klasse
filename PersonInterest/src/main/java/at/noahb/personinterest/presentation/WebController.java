package at.noahb.personinterest.presentation;

import at.noahb.personinterest.domain.Person;
import at.noahb.personinterest.domain.Sex;
import at.noahb.personinterest.persistence.InterestRepository;
import at.noahb.personinterest.persistence.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/web/persons")
public record WebController(PersonRepository personRepository, InterestRepository interestRepository) {


    @GetMapping
    public String getOverview(Model model) {

        model.addAttribute("persons", personRepository.findAll());

        return "overview";
    }

    @GetMapping("new")
    public String getNewPersonForm(Model model) {

        if (!model.containsAttribute("newPerson"))
            model.addAttribute("newPerson", new Person());
        model.addAttribute("interests", interestRepository.findAll());
        model.addAttribute("sex", Sex.values());

        return "newPerson";
    }

    @PostMapping("createNewPerson")
    public String postNewPerson(@Valid @ModelAttribute("newPerson") Person newPerson, BindingResult bindingResult, Model model) {

        System.out.println(newPerson);

        if (bindingResult.hasErrors()) {
            return getNewPersonForm(model);
        }

        personRepository.save(newPerson);

        return "redirect:/web/persons";
    }
}
