package ee.knk.neverland.controller;

import ee.knk.neverland.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
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
