package knk.ee.neverland.exceptions

class NetworkException(val code: Int) : Exception("Network error. Code [$code]")