package knk.ee.neverland.api

interface VoteAPI {
    var token: String

    fun voteFor(proofID: Int)

    fun voteAgainst(proofID: Int)
}