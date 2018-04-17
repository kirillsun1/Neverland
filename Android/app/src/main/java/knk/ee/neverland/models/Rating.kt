package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName

data class Rating(@SerializedName("for") var votesFor: Int,
                  @SerializedName("against") var votesAgainst: Int) {
    fun intValue(): Int = (floatValue() * 100).toInt()

    private fun floatValue(): Float = votesFor.toFloat() / (votesFor + votesAgainst)
}