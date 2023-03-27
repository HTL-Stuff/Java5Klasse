package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.persistent.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByAnswererEmailAndId(String user, Long id);

}
