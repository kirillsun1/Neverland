package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private User user;

    @Mock
    private Token token;

    @Mock
    private PeopleGroup group;

    @Test
    public void testIfAddTokenCallsRepo() {
        when(tokenRepository.saveAndFlush(token)).thenReturn(token);
        tokenService.addToken(token);
        verify(tokenRepository).saveAndFlush(token);
    }

    @Test
    public void testIfCleanOutByUserCallsRepo() {
        tokenService.cleanOutByUser(user);
        verify(tokenRepository).cleanOutOutOfDateKeys(user);
    }

    @Test
    public void testIfIIsUserSubscribedReturnsFalseIfRepoGivesEmptyOptional() {
        when(tokenRepository.exists("token")).thenReturn(Optional.empty());
        assert (!tokenService.exists("token"));
    }

    @Test
    public void testIfIsUserSubscribedReturnsTrueIfRepoGivesNotEmptyOptional() {
        when(tokenRepository.exists("token")).thenReturn(Optional.of(token));
        assert (tokenService.exists("token"));
    }

    @Test
    public void testIfGeAllCallsRepo() {
        when(tokenRepository.findAll()).thenReturn(new ArrayList<>());
        tokenService.getAll();
        verify(tokenRepository).findAll();
    }

    @Test
    public void testIfGetTokenUserCallsRepo() {
        when(tokenRepository.getTokenUser("token")).thenReturn(Optional.empty());
        tokenService.getTokenUser("token");
        verify(tokenRepository).getTokenUser("token");
    }


}
