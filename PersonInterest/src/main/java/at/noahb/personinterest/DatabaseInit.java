package at.noahb.personinterest;

import at.noahb.personinterest.domain.Interest;
import at.noahb.personinterest.domain.Person;
import at.noahb.personinterest.domain.Sex;
import at.noahb.personinterest.persistence.InterestRepository;
import at.noahb.personinterest.persistence.PersonRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public record DatabaseInit(PersonRepository personRepository,
                           InterestRepository interestRepository) implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        interestRepository.saveAll(List.of(
                new Interest("Reading"),
                new Interest("Trains"),
                new Interest("Soccer"),
                new Interest("Gaming"),
                new Interest("Programming")
        ));

        List<Interest> interests = interestRepository.findAll();

        personRepository.saveAll(List.of(
                new Person(interests.stream().limit(3).collect(Collectors.toSet()), "Hubert", "Herbert", Sex.MALE, LocalDate.of(1983, 10, 2)),
                new Person(Set.of(), "Malene", "Berger", Sex.FEMALE, LocalDate.of(1989, 3, 10))
        ));

        System.out.println(personRepository.findAll());
    }
}
