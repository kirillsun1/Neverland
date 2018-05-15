package ee.knk.neverland.controller;

import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private UserController userController;

    @Mock
    private User user;

    @Before
    public void before() {
        when(tokenService.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userService.existsWithUsernameOrEmail("dummy", "dummy@e.e")).thenReturn(false);

    }

    @Test
    public void testIfRegisterControlsUserExistence() {
        userController.register("dummy","dummy","dummy@e.e","Dummy","Dummy");
        verify(userService).existsWithUsernameOrEmail("dummy", "dummy@e.e");
    }

    @Test
    public void testIfRegisterCallsAddUser() {
        userController.register("dummy","dummy","dummy@e.e","Dummy","Dummy");
        verify(userService).addUser(any());
    }

    @Test
    public void testIfLoginCallsService() {
        userController.login("dummy","dummy");
        verify(userService).findMatch("dummy", "dummy");
    }

}
