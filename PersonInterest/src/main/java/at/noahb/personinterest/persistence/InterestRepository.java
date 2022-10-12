package at.noahb.personinterest.persistence;

import at.noahb.personinterest.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Interest> {

}
