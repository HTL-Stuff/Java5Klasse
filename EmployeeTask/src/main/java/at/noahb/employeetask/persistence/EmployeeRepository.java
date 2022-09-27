package at.noahb.employeetask.persistence;

import at.noahb.employeetask.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query("""
            select e from Employee e
            where e.firstName like concat('%', ?1, '%')
            or e.lastName like concat('%', ?1, '%')
            """)
    List<Employee> findAllByNameContains(String partialName);


}
