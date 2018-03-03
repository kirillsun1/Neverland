package knk.ee.neverland.exceptions

import knk.ee.neverland.api.AuthAPIResponse

class AuthAPIException(reason: AuthAPIResponse) : Exception()