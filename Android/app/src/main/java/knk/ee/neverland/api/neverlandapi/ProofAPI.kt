package knk.ee.neverland.api.neverlandapi

import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.models.Proof

interface ProofAPI {
    var token: String

    fun submitProof(proof: ProofToSubmit)

    fun getProofs(feedScope: FeedScope): List<Proof>
}