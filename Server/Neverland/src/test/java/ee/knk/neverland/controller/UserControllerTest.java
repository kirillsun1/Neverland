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

    @Test
    public void testIfRegisterControlsUserExistence() {
        when(tokenService.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userService.existsWithUsernameOrEmail(any(), any())).thenReturn(true);
        userController.register("dummydummy","dummy","dummy@e.e","Dummy","Dummy");
        verify(userService).existsWithUsernameOrEmail(any(), any());
    }


    @Test
    public void testIfLoginCallsService() {
        when(userService.findMatch(any(), any())).thenReturn(Optional.empty());
        userController.login("dummy","dummy");
        verify(userService).findMatch("dummy", "dummy");
    }

}
