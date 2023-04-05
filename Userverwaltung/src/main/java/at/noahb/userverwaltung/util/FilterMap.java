package at.noahb.userverwaltung.util;

import at.noahb.userverwaltung.domain.AnswerDistribution;
import at.noahb.userverwaltung.domain.AnswerType;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterMap {

    /**
     * Filters the given stream by the given questionId and adds the missing answer types.
     * This method is called by the HTML template.
     * @param stream the stream to filter
     * @param questionId the questionId to filter by
     * @return
     */
    public static Set<AnswerDistribution> filter(Stream<AnswerDistribution> stream, Long questionId) {
        TreeSet<AnswerDistribution> filteredDistribution = stream.filter(answerDistribution -> answerDistribution.getQuestionId().equals(questionId))
                .collect(Collectors.toCollection(TreeSet::new));

        filteredDistribution.add(new AnswerDistribution(questionId, AnswerType.AGREE, 0));
        filteredDistribution.add(new AnswerDistribution(questionId, AnswerType.DISAGREE, 0));
        filteredDistribution.add(new AnswerDistribution(questionId, AnswerType.TOTALLY_AGREE, 0));

        return filteredDistribution;
    }
}
