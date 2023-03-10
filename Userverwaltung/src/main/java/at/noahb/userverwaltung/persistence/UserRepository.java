package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.persistent.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
