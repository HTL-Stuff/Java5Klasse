package at.noahb.springdonations.persistence;

import at.noahb.springdonations.domain.Donation;
import at.noahb.springdonations.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("select donation from Donation donation where donation.person.id = :personId")
    Optional<List<Donation>> getAllDonationsByPerson(int personId);

}
