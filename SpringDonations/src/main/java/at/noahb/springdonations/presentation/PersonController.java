package at.noahb.springdonations.presentation;

import at.noahb.springdonations.domain.Donation;
import at.noahb.springdonations.exception.NotFoundException;
import at.noahb.springdonations.payload.request.NewDonationRequestBody;
import at.noahb.springdonations.persistence.DonationRepository;
import at.noahb.springdonations.persistence.PersonRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/persons")
public record PersonController(PersonRepository personRepository, DonationRepository donationRepository) {

    @GetMapping("/{id}/donations")
    public List<Donation> getAllDonationsForAPerson(@PathVariable Integer id) {
        return personRepository.getAllDonationsByPerson(id).orElseThrow(() -> new NotFoundException("Could not find person with id " + id));
    }

    @PostMapping("/{id}/donations")
    public Donation addDonationToPerson(@PathVariable Integer id, @RequestBody NewDonationRequestBody newDonationRequestBody) {

        var person = personRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not find person with id " + id));

        Donation donation = new Donation(newDonationRequestBody.amount(), newDonationRequestBody.date(), person);

        donation = donationRepository.save(donation);

        return donation;
    }
}
