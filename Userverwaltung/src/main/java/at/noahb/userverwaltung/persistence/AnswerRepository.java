package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.persistent.Answer;
import at.noahb.userverwaltung.domain.persistent.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByAnswerer(String user);

}
