package at.noahb.userverwaltung.domain.persistent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class LegacyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String question;

    private LocalDate expiryDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "question_answers",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id"))
    private Set<Answer> answers;

    public LegacyQuestion(String name, String question, LocalDate expiryDate) {
        this.name = name;
        this.question = question;
        this.expiryDate = expiryDate;
    }

    public LegacyQuestion(String name, String question, LocalDate expiryDate, Set<Answer> answers) {
        this.name = name;
        this.question = question;
        this.expiryDate = expiryDate;
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        if (answers.stream().map(Answer::getAnswerer).anyMatch(answer.getAnswerer()::equals)) {
            return;
        }
        answers.add(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegacyQuestion question = (LegacyQuestion) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
