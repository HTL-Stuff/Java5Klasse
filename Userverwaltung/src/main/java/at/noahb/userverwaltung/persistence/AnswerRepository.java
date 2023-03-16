package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.persistent.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
