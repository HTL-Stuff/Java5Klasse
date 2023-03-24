package at.noahb.userverwaltung.domain.persistent;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 20)
    @NotNull
    private String name;

    @Length(max = 200)
    @NotNull
    private String question;

    private LocalDate expiryDate;

    public Question(String name, String question, LocalDate expiryDate) {
        this.name = name;
        this.question = question;
        this.expiryDate = expiryDate;
    }

    public Question(String name, String question, LocalDate expiryDate, Set<Answer> answers) {
        this.name = name;
        this.question = question;
        this.expiryDate = expiryDate;
        this.answers = answers;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "question_answers",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id"))
    private Set<Answer> answers;

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
