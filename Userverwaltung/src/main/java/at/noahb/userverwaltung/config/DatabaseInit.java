package at.noahb.userverwaltung.config;

import at.noahb.userverwaltung.domain.persistent.Answer;
import at.noahb.userverwaltung.domain.AnswerType;
import at.noahb.userverwaltung.domain.persistent.Question;
import at.noahb.userverwaltung.domain.persistent.Role;
import at.noahb.userverwaltung.domain.persistent.User;
import at.noahb.userverwaltung.domain.security.RoleAuthority;
import at.noahb.userverwaltung.persistence.AnswerRepository;
import at.noahb.userverwaltung.persistence.QuestionRepository;
import at.noahb.userverwaltung.persistence.RoleRepository;
import at.noahb.userverwaltung.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public record DatabaseInit(QuestionRepository questionRepository,
                           UserRepository userRepository,
                           AnswerRepository answerRepository,
                           RoleRepository roleRepository) implements ApplicationRunner {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void run(ApplicationArguments args) throws Exception {
        var adminRole = roleRepository.save(new Role(RoleAuthority.ROLE_ADMIN));
        var userRole = roleRepository.save(new Role(RoleAuthority.ROLE_USER));

        var admin = userRepository.save(new User("admin@comp.com", adminRole, passwordEncoder.encode("admin")));
        var user1 = userRepository.save(new User("user1@comp.com", userRole, passwordEncoder.encode("user1")));
        var user2 = userRepository.save(new User("user2@comp.com", userRole, passwordEncoder.encode("user2")));
        var user3 = userRepository.save(new User("user3@comp.com", userRole, passwordEncoder.encode("user3")));

        var answer1 = answerRepository.save(new Answer(user1, AnswerType.TOTALLY_AGREE));
        var answer2 = answerRepository.save(new Answer(user2, AnswerType.AGREE));
        var answer3 = answerRepository.save(new Answer(user1, AnswerType.DISAGREE));

        var question1 = questionRepository.save(new Question("Hungry?",
                "Are you hungry?", LocalDate.now().minus(1, ChronoUnit.DAYS),
                Set.of(answer1, answer2)));
        var question2 = questionRepository.save(new Question("School is boring",
                "Idk i just think school is boring. I could use my time efficiently and play computer games instead...",
                LocalDate.now().plus(1, ChronoUnit.MONTHS), Set.of(answer3)));
        var question3 = questionRepository.save(new Question("Is Star Wars good?",
                "Hey my friends are always asking me if I already watched Star Wars. Is it really that good?",
                LocalDate.now().plus(1, ChronoUnit.MONTHS)));
        var question4 = questionRepository.save(new Question("Penguins are cute",
                "Today I was in the local zoo and I loved the penguins they have. I even took a picture of them!",
                LocalDate.now().plus(1, ChronoUnit.MONTHS)));
        var question5 = questionRepository.save(new Question("School is boring",
                "Idk i just think school is boring. I could use my time efficiently and play computer games instead...",
                LocalDate.now().plus(1, ChronoUnit.MONTHS)));
    }
}
