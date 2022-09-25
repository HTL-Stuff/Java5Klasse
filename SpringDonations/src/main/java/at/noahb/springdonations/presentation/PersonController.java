package at.noahb.springdonations.presentation;

import at.noahb.springdonations.domain.Donation;
import at.noahb.springdonations.persistence.PersonRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/persons")
public record PersonController(PersonRepository personRepository) {

    @GetMapping("/{id}/donations")
    public List<Donation> getAllDonationsForAPerson(@PathVariable Integer id) {
        return personRepository.getAllDonationsByPerson(id);
    }
}
