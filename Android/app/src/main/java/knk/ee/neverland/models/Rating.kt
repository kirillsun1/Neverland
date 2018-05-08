package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName
import knk.ee.neverland.utils.Constants

data class Rating(@SerializedName("for") val votesFor: Int,
                  @SerializedName("against") val votesAgainst: Int,
                  @SerializedName("my_vote") private val myVote: Int) {
    fun intValue(): Int = (floatValue() * 100).toInt()

    fun iVoted(): Boolean = myVote != Constants.NO_VOTE

    fun myVoteIsFor(): Boolean = myVote == Constants.POSITIVE_VOTE

    fun myVoteIsAgainst(): Boolean = myVote == Constants.NEGATIVE_VOTE

    private fun floatValue(): Float = votesFor.toFloat() / (votesFor + votesAgainst)
}
