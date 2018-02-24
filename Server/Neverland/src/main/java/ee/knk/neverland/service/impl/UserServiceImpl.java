package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.UserRepository;
import ee.knk.neverland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public void delete(Long userId) {

    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
