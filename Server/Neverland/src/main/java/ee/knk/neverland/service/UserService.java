package ee.knk.neverland.service;

import ee.knk.neverland.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User addUser(User user);
    User findUser(long userId);
    boolean existsWithUsernameOrEmail(String username, String email);
    User editUser(User user);
    Optional<User> findMatch(String username, String password);
    void delete(Long userId);
    List<User> getAll();
}
