package ee.knk.neverland.controller;

import ee.knk.neverland.service.TakenQuestService;
import ee.knk.neverland.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TokenControllerTest {
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenController tokenController;

    @Before
    public void before() {
        when(tokenService.getTokenUser("token")).thenReturn(Optional.empty());
    }

    @Test
    public void testIfIsValidCallsService() {
        tokenController.isValid("token");
        verify(tokenService).getTokenUser("token");
    }

}
