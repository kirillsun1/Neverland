package knk.ee.neverland.exceptions

class APIException(val code: Int) : Exception("API Request failed. Code [$code]")