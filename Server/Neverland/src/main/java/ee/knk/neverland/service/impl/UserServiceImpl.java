package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.UserRepository;
import ee.knk.neverland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<User> findUser(long userId) {
        return Optional.of(userRepository.findOne(userId));
    }

    @Override
    public boolean existsWithUsernameOrEmail(String username, String email) {
        return userRepository.exists(username, email).isPresent();
    }

    @Override
    public Optional<User> findMatch(String username, String password) {
        return userRepository.passwordMatches(username, password);
    }

    @Override
    public void setAvatar(Long id, String path) {
        userRepository.setAvatar(path, id);
    }

}
