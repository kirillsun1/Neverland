package knk.ee.neverland.api

import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.models.Proof

interface ProofAPI {
    var token: String

    fun submitProof(proof: ProofToSubmit)

    fun getProofs(): List<Proof>

    fun getProofsByUserID(userID: Int): List<Proof>

    fun getProofsByQuestID(questID: Int): List<Proof>

    fun getMyProofs() : List<Proof>
}