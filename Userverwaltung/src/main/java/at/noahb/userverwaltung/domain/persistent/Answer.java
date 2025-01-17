package at.noahb.userverwaltung.domain.persistent;

import at.noahb.userverwaltung.domain.AnswerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "answers")
@AllArgsConstructor
@Getter
@ToString
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private User answerer;

    @Enumerated(EnumType.STRING)
    @Setter
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
