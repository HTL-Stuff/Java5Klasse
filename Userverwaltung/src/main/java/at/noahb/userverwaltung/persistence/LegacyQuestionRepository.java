package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.persistent.LegacyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegacyQuestionRepository extends JpaRepository<LegacyQuestion, Long> {
}
