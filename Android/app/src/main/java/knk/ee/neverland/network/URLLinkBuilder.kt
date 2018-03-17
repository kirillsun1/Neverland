package knk.ee.neverland.network

class URLLinkBuilder(val link: String, val action: String) {
    private var params: Int = 0
    private val paramsBuilder: StringBuilder = StringBuilder()

    init {
        paramsBuilder
            .append(link)
            .append("/")
            .append(action)
    }

    fun addParam(key: String, value: String): URLLinkBuilder {
        if (params == 0) {
            paramsBuilder.append("?")
        }

        if (params++ > 0) {
            paramsBuilder.append("&")
        }

        paramsBuilder
            .append(key)
            .append("=")
            .append(value)

        return this
    }

    fun finish(): String {
        return paramsBuilder.toString()
    }
}