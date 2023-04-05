package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.persistent.Question;
import at.noahb.userverwaltung.domain.persistent.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("""
    select q
    from Question q
    where q.expiryDate >= current_date
    and ?1 not in (
        select a.answerer.email
        from Answer a
        where a member of q.answers
    )
    """)
    List<Question> findAllByExpiryDateNotExpiredAndAnswersNotContains(String userEmail);


}
