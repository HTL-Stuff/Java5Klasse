package at.noahb.springdonations.persistence;

import at.noahb.springdonations.domain.Donation;
import at.noahb.springdonations.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("select donation from Donation donation where donation.person.id = :personId")
    List<Donation> getAllDonationsByPerson(int personId);
}
