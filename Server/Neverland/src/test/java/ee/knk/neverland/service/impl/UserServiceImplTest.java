package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private User user;

    @Test
    public void testIfAddUserByIdCallsRepo() {
        when(userRepository.saveAndFlush(user)).thenReturn(user);
        userService.addUser(user);
        verify(userRepository).saveAndFlush(user);
    }

    @Test
    public void testIfExistsWithUsernameOrEmailReturnsFalseIfRepoGivesEmptyOptional() {
        when(userRepository.exists("dummy", "dummy")).thenReturn(Optional.empty());
        assert(!userService.existsWithUsernameOrEmail("dummy", "dummy"));
    }

    @Test
    public void testIfCheckAdminsRightsReturnsTrueIfRepoGivesObjectInOptional() {
        when(userRepository.exists("dummy", "dummy")).thenReturn(Optional.of(user));
        assert(userService.existsWithUsernameOrEmail("dummy", "dummy"));
    }

    @Test
    public void testIfSetAvatarCallsRepo() {
        userService.setAvatar(0L, "");
        verify(userRepository).setAvatar("", 0L);
    }







}