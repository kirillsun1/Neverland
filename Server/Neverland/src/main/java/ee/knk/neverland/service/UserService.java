package ee.knk.neverland.service;

import ee.knk.neverland.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User addUser(User user);
    Optional<User> findUser(long userId);
    boolean existsWithUsernameOrEmail(String username, String email);
    Optional<User> findMatch(String username, String password);

    void setAvatar(Long id, String path);
}
