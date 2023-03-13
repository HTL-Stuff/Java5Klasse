package at.noahb.userverwaltung.config;

import at.noahb.userverwaltung.domain.persistent.Answer;
import at.noahb.userverwaltung.domain.AnswerType;
import at.noahb.userverwaltung.domain.persistent.Question;
import at.noahb.userverwaltung.domain.persistent.Role;
import at.noahb.userverwaltung.domain.persistent.User;
import at.noahb.userverwaltung.domain.security.RoleAuthority;
import at.noahb.userverwaltung.persistence.QuestionRepository;
import at.noahb.userverwaltung.persistence.RoleRepository;
import at.noahb.userverwaltung.persistence.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public record DatabaseInit(QuestionRepository questionRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userRole = new Role(1L, RoleAuthority.ROLE_USER);
        var adminRole = new Role(0L, RoleAuthority.ROLE_ADMIN);
        roleRepository.saveAll(List.of(userRole, adminRole));

        var admin = new User("admin@comp.com", adminRole, passwordEncoder.encode("admin"));
        var user1 = new User("user1@comp.com", userRole, passwordEncoder.encode("user1"));
        var user2 = new User("user2@comp.com", userRole, passwordEncoder.encode("user2"));
        var user3 = new User("user3@comp.com", userRole, passwordEncoder.encode("user3"));
        userRepository.saveAll(List.of(admin, user1, user2, user3));

        var question1 = new Question(0L, "Hungry?",
                "Are you hungry?", LocalDate.now().minus(1, ChronoUnit.DAYS),
                Set.of(new Answer(0L, user1, AnswerType.TOTALLY_AGREE), new Answer(1L, user2, AnswerType.AGREE)));
        var question2 = new Question(1L, "School is boring",
                "Idk i just think school is boring. I could use my time efficiently and play computer games instead...",
                LocalDate.now().plus(1, ChronoUnit.MONTHS), Set.of(new Answer(2L, user1, AnswerType.DISAGREE)));
        var question3 = new Question(1L, "Is Star Wars good?",
                "Hey my friends are always asking me if I already watched Star Wars. Is it really that good?",
                LocalDate.now().plus(1, ChronoUnit.MONTHS), Collections.emptySet());
        var question4 = new Question(1L, "Penguins are cute",
                "Today I was in the local zoo and I loved the penguins they have. I even took a picture of them!",
                LocalDate.now().plus(1, ChronoUnit.MONTHS), Collections.emptySet());
        var question5 = new Question(1L, "School is boring",
                "Idk i just think school is boring. I could use my time efficiently and play computer games instead...",
                LocalDate.now().plus(1, ChronoUnit.MONTHS), Collections.emptySet());

        questionRepository.saveAll(List.of(question1, question2, question3, question4, question5));

    }
}
