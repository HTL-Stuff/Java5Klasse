package at.noahb.vmm;

import at.noahb.vmm.domain.Grade;
import at.noahb.vmm.domain.Student;
import at.noahb.vmm.domain.Subject;
import at.noahb.vmm.persistence.GradeRepository;
import at.noahb.vmm.persistence.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public record DatabaseInit(StudentRepository studentRepository,
                           GradeRepository gradeRepository) implements ApplicationRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseInit.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path pathToGrades = new File(Objects.requireNonNull(getClass()
                        .getResource("/grades.csv"))
                .getFile()).toPath();

        Stream.of(Files.readString(pathToGrades).split("\r\n"))
                .skip(1L)
                .forEach(s -> {
                    String[] gradeContent = s.split(",");

                    Optional<Student> optionalStudent;

                    try {
                        optionalStudent = studentRepository.findById(Integer.parseInt(gradeContent[0]));
                    } catch (NumberFormatException exception) {
                        LOGGER.warn("Invalid Student id: " + gradeContent[0]);
                        return;
                    }

                    if (optionalStudent.isEmpty()) {
                        LOGGER.warn("Could not find student with id: " + gradeContent[0]);
                        return;
                    }

                    Student student = optionalStudent.get();

                    LocalDate localDate;
                    try {
                        localDate = LocalDate.parse(gradeContent[1]);
                    } catch (DateTimeParseException exception) {
                        LOGGER.warn("Could not parse date: " + gradeContent[1]);
                        return;
                    }

                    int gradeValue;
                    try {
                        gradeValue = Integer.parseInt(gradeContent[3]);
                    } catch (NumberFormatException exception) {
                        LOGGER.warn("Could not parse grade value: " + gradeContent[3]);
                        return;
                    }

                    Grade grade = new Grade(localDate, Subject.valueOf(gradeContent[2]), gradeValue, student);

                    gradeRepository.save(grade);
                });
    }
}
