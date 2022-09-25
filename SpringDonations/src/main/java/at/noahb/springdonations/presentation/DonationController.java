package at.noahb.springdonations.presentation;

import at.noahb.springdonations.domain.Donation;
import at.noahb.springdonations.exception.NotFoundException;
import at.noahb.springdonations.payload.response.GetAllWithMinDonationResponse;
import at.noahb.springdonations.persistence.DonationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/donations")
public record DonationController(DonationRepository donationRepository) {


    @GetMapping("/{id}")
    public Donation getById(@PathVariable Integer id) {
        return donationRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not find donation with id " + id));
    }

    @GetMapping
    public Collection<?> getAllOrWithMinDonation(@RequestParam(name = "min", required = false) Long minimum) {
        if (minimum == null) {
            return donationRepository.findAll();
        }

        return donationRepository.findDistinctByAmount(minimum).stream()
                .map(person -> new GetAllWithMinDonationResponse(person, donationRepository.sumOfDonationsByPerson(person)))
                .toList();
    }
}
