package knk.ee.neverland.network

import org.junit.Assert.assertEquals
import org.junit.Test

class URLLinkBuilderTest {
    @Test
    fun testLinkWithNoParameters() {
        val link = URLLinkBuilder("https://some.link", "no_params")
            .finish()

        assertEquals("https://some.link/no_params", link)
    }

    @Test
    fun testLinkWithOneParameter() {
        val link = URLLinkBuilder("https://some.link", "no_params")
            .addParam("first", "here")
            .finish()

        assertEquals("https://some.link/no_params?first=here", link)
    }

    @Test
    fun testLinkWithMultipleParameters() {
        val link = URLLinkBuilder("https://some.link", "no_params")
            .addParam("first", "here")
            .addParam("even", "more")
            .addParam("parameters", "here")
            .finish()

        assertEquals("https://some.link/no_params?first=here&even=more&parameters=here", link)
    }
}