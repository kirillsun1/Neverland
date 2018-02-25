package ee.knk.neverland.repository;

import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
