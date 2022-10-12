package at.noahb.personinterest.persistence;

import at.noahb.personinterest.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}
