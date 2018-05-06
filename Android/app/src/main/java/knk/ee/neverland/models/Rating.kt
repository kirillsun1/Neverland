package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName

data class Rating(@SerializedName("for") private val votesFor: Int,
                  @SerializedName("against") private val votesAgainst: Int,
                  @SerializedName("my_vote") private val myVote: Int) {
    fun intValue(): Int = (floatValue() * 100).toInt()

    private fun floatValue(): Float = votesFor.toFloat() / (votesFor + votesAgainst)

    fun myVoteIsFor(): Boolean = myVote == 1

    fun myVoteIsAgainst(): Boolean = myVote == 0
}