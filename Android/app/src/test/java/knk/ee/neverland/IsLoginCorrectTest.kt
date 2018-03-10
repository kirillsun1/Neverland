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
        assertTrue(Utils.loginIsCorrect("4sym"))
        assertTrue(Utils.loginIsCorrect("5syms"))
        assertTrue(Utils.loginIsCorrect("_okok"))
        assertTrue(Utils.loginIsCorrect("_okokokok"))

        assertTrue(Utils.loginIsCorrect("aaaaaaaaaaaaaaaa")) // 16 syms ok!
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
}