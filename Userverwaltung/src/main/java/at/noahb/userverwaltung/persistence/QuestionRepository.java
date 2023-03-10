package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.persistent.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
