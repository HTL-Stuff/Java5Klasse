package at.noahb.springdonations.presentation;

import at.noahb.springdonations.domain.Donation;
import at.noahb.springdonations.persistence.DonationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/donations")
public record DonationController(DonationRepository donationRepository) {


    @GetMapping("/{id}")
    public Donation getById(@PathVariable Integer id) {
        return donationRepository.findById(id).orElseThrow();
    }

    @GetMapping
    public Collection<?> getAllOrWithMinDonation(@RequestParam(name = "min", required = false) Long minimum) {
        if (minimum == null) {
            return donationRepository.findAll();
        }

        return Collections.emptyList();
    }


}
