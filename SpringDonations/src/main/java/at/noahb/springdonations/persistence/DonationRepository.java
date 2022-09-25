package at.noahb.springdonations.persistence;

import at.noahb.springdonations.domain.Donation;
import at.noahb.springdonations.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer> {
    @Query(value = """
            select distinct donation.person from Donation donation
            group by donation.person
            having sum(donation.amount) >= ?1
            """)
    List<Person> findDistinctByAmount(Long amount);

    @Query("select sum(d.amount) from Donation d where d.person = ?1")
    Integer sumOfDonationsByPerson(Person person);

}
