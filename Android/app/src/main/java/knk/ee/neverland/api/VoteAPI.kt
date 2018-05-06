package knk.ee.neverland.api

import knk.ee.neverland.models.Rating

interface VoteAPI {
    var token: String

    fun voteFor(proofID: Int): Rating

    fun voteAgainst(proofID: Int): Rating
}