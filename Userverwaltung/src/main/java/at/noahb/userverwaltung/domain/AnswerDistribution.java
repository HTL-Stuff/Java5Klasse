package at.noahb.userverwaltung.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@AllArgsConstructor
public class AnswerDistribution implements Comparable<AnswerDistribution> {

    private final AnswerType answerType;
    private final long count;
    private final Long questionId;

    public AnswerDistribution(Long questionId, AnswerType answerType, long count) {
        this.questionId = questionId;
        this.answerType = answerType;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerDistribution that = (AnswerDistribution) o;
        return Objects.equals(questionId, that.questionId) && answerType == that.answerType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, answerType);
    }

    @Override
    public int compareTo(AnswerDistribution o) {
        return this.answerType.compareTo(o.answerType);
    }
}
