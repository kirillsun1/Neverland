package ee.knk.neverland.tools;

import org.junit.Test;

public class ValidatorTest {
    private final Validator validator = new Validator();


    @Test
    public void testIfLoginCheckAcceptsSimpleRightLogin() {
        String login = "aftereight";
        assert(validator.loginIsCorrect(login));
    }

    @Test
    public void testIfLoginCheckDoesntAcceptLoginWithSign() {
        String login = "after.eight";
        assert(!validator.loginIsCorrect(login));
    }

    @Test
    public void testIfLoginCheckAcceptsRightLoginSixSymbols() {
        String login = "aftere";
        assert(validator.loginIsCorrect(login));
    }

    @Test
    public void testIfLoginCheckAcceptsRightLoginSixteenSymbols() {
        String login = "aftereightidoeat";
        assert(validator.loginIsCorrect(login));
    }

    @Test
    public void testIfLoginCheckAcceptsRightLoginWithNumbers() {
        String login = "after8";
        assert(validator.loginIsCorrect(login));
    }

    @Test
    public void testIfLoginCheckAcceptsAllNumbers() {
        String login = "1816253";
        assert(validator.loginIsCorrect(login));
    }

    @Test
    public void testIfLoginCheckDoesntAcceptsTooShort() {
        String login = "after";
        assert(!validator.loginIsCorrect(login));
    }

    @Test
    public void testIfLoginCheckDoesntAcceptsTooLong() {
        String login = "aftereightidonoteat";
        assert(!validator.loginIsCorrect(login));
    }

    @Test
    public void testIfEmailCheckAcceptsSimpleEmail() {
        String email = "e@e.e";
        assert(validator.emailIsCorrect(email));
    }

    @Test
    public void testIfEmailCheckDoesNotAcceptEmptyEmail() {
        String email = "@.";
        assert(!validator.emailIsCorrect(email));
    }

    @Test
    public void testIfEmailCheckAcceptsEmailWithNumbers() {
        String email = "e5@e.e";
        assert(validator.emailIsCorrect(email));
    }

    @Test
    public void testIfEmailCheckAcceptsEmailWithSignsInside() {
        String email = "e.e_e@e.e";
        assert(validator.emailIsCorrect(email));
    }

    @Test
    public void testIfEmailCheckDoesNotAcceptEmptyString() {
        String email = "";
        assert(!validator.emailIsCorrect(email));
    }

    @Test
    public void testIfNameCheckAcceptsShortName() {
        String name = "M";
        assert(validator.nameIsCorrect(name));
    }

    @Test
    public void testIfNameCheckAcceptsLongName() {
        String name = "Mynameisveryveryveryveyveryveryvervyeveryvrveveryvreveyvrverrvyverylong";
        assert(validator.nameIsCorrect(name));
    }

    @Test
    public void testIfNameCheckDoesNotAcceptEmptyString() {
        String name = "";
        assert(!validator.nameIsCorrect(name));
    }

    @Test
    public void testIfNameCheckDoesNotAcceptNameWithNumbers() {
        String name = "M5";
        assert(!validator.nameIsCorrect(name));
    }
    
    @Test
    public void testIfNameCheckAcceptsNameWithDots() {
        String name = "M.D";
        assert(validator.nameIsCorrect(name));
    }

    @Test
    public void testIfNameCheckAcceptsNameWithAphostrophe() {
        String name = "M'D";
        assert(validator.nameIsCorrect(name));
    }

    @Test
    public void testIfNameCheckAcceptsNameWithComma() {
        String name = "M,D";
        assert(validator.nameIsCorrect(name));
    }

    @Test
    public void testIfNameCheckAcceptsNameWithSpae() {
        String name = "M D";
        assert(validator.nameIsCorrect(name));
    }

    @Test
    public void testIfNameCheckAcceptsNameWithHyphen() {
        String name = "M-D";
        assert(validator.nameIsCorrect(name));
    }

}
