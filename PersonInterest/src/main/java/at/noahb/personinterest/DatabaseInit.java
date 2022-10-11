package at.noahb.personinterest;

import at.noahb.personinterest.domain.Interest;
import at.noahb.personinterest.repository.InterestRepository;
import at.noahb.personinterest.repository.PersonRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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


    }
}
