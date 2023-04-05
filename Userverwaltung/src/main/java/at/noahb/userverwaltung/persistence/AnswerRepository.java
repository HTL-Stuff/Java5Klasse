package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.AnswerDistribution;
import at.noahb.userverwaltung.domain.persistent.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("""
            select new at.noahb.userverwaltung.domain.AnswerDistribution(q.id, a.answerType, count(a))
            from Question q join q.answers a
            group by q.id, a.answerType
            """)
    List<AnswerDistribution> getAnswerDistribution();
}
