package knk.ee.neverland.network

class URLLinkBuilder(link: String, action: String) {
    private val paramsBuilder: StringBuilder = StringBuilder()

    private var params: Int = 0

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
        println(paramsBuilder.toString())
        return paramsBuilder.toString()
    }
}