package at.noahb.userverwaltung.config;

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
import java.util.List;

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

        var question1 = new Question(0L, "Hungry?", "Are you hungry?", AnswerType.TOTALLY_AGREE, LocalDate.of(2023, 4, 10), user1);
        var question2 = new Question();
        var question3 = new Question();
        var question4 = new Question();
        var question5 = new Question();


    }
}
