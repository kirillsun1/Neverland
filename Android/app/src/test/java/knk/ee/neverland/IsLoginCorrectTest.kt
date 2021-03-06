package knk.ee.neverland

import knk.ee.neverland.utils.Utils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsLoginCorrectTest {
    @Test
    fun testEmptyLoginIsNotCorrect() {
        assertFalse(Utils.loginIsCorrect(""))
    }

    @Test
    fun testLoginIsIncorrectIfItContainsOnlySpaces() {
        assertFalse(Utils.loginIsCorrect("      "))
        assertFalse(Utils.loginIsCorrect("   "))
        assertFalse(Utils.loginIsCorrect("  "))
        assertFalse(Utils.loginIsCorrect(" "))
    }

    @Test
    fun testLoginIsIncorrectIfItsLengthIsLessThan4() {
        assertFalse(Utils.loginIsCorrect("abc"))
        assertFalse(Utils.loginIsCorrect("ab"))
        assertFalse(Utils.loginIsCorrect("a"))
    }

    @Test
    fun testLoginIsCorrectIfMatchesLength() {
        assertTrue(Utils.loginIsCorrect("6symmm"))
        assertTrue(Utils.loginIsCorrect("7symskk"))
        assertTrue(Utils.loginIsCorrect("_okoklll"))
        assertTrue(Utils.loginIsCorrect("_okokokok"))

        assertTrue(Utils.loginIsCorrect("kirillsunodddddd")) // 16 syms ok!
    }

    @Test
    fun testLoginIsIncorrectIfItIsTooLong() {
        assertFalse(Utils.loginIsCorrect("aaaaaaaaaaaaaaaaa")) // 17 not ok!
        assertFalse(Utils.loginIsCorrect("asdgnklasdngladskgnkalsdgn")) // 17 not ok!
    }

    @Test
    fun testLoginIsIncorrectIfItContainsProhibitedSymbols() {
        val symbols = "!@#$%^&*()"

        for (sym in symbols) {
            assertFalse(Utils.loginIsCorrect("i-s$(sym)login"))
            assertFalse(Utils.loginIsCorrect("i_s$(sym)login"))
            assertFalse(Utils.loginIsCorrect("is$(sym)login"))
        }
    }

    @Test
    fun testLoginIsCorrectWithAllowedSpecialSymbols() {
        assertTrue(Utils.loginIsCorrect("login_is_good"))
        assertTrue(Utils.loginIsCorrect("_login-isgood"))
    }

    @Test
    fun testLoginWithNumbers() {
        assertTrue(Utils.loginIsCorrect("hello123"))
        assertTrue(Utils.loginIsCorrect("hello_13"))
        assertTrue(Utils.loginIsCorrect("heldlo-456"))
    }

    @Test
    fun testUTF8SymbolsAreNotAllowed() {
        assertFalse(Utils.loginIsCorrect("loginвпывп"))
        assertFalse(Utils.loginIsCorrect("logвäädföüõ"))
    }

    @Test
    fun testLoginIsIncorrectIfContainsOnlyNumbers() {
        assertFalse(Utils.loginIsCorrect("0123456789"))
    }
}