package at.noahb.userverwaltung.domain.persistent;

import at.noahb.userverwaltung.domain.AnswerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "answers")
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User answerer;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    public Answer() {
    }

    public Answer(User answerer, AnswerType answerType) {
        this.answerer = answerer;
        this.answerType = answerType;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
