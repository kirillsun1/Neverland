package knk.ee.neverland

import junit.framework.Assert.assertTrue
import knk.ee.neverland.utils.Utils
import org.junit.Test

class IsPasswordCorrectTest {
    @Test
    fun testPasswordIsCorrect() {
        assertTrue(Utils.passwordIsCorrect("symbol"))
        assertTrue(Utils.passwordIsCorrect("_goodpass1"))
        assertTrue(Utils.passwordIsCorrect("_for1length-its_ok"))
    }
}