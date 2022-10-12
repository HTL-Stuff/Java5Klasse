package at.noahb.personinterest.presentation;

import at.noahb.personinterest.domain.Person;
import at.noahb.personinterest.domain.Sex;
import at.noahb.personinterest.persistence.InterestRepository;
import at.noahb.personinterest.persistence.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        model.addAttribute("newPerson", new Person());
        model.addAttribute("allInterests", interestRepository.findAll());
        model.addAttribute("availableSex", Sex.values());

        return "newPerson";
    }

     @PostMapping("createNewPerson")
    public String postNewPerson(@ModelAttribute("newPerson") Person newPerson) {

         System.out.println(newPerson);

        return "redirect:/web/persons";
    }
}
