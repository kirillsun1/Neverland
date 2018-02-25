package ee.knk.neverland.service;

import ee.knk.neverland.entity.User;

import java.util.List;

public interface UserService {
    User addUser(User user);
    void delete(Long userId);
    List<User> getAll();
}
