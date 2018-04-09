package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName
import knk.ee.neverland.datetime.Date

data class Proof(
    val id: Int,

    val comment: String?,

    @SerializedName("pic_path")
    val imageLink: String,

    val quest: Quest,

    @SerializedName("proofer")
    val sender: User,

    @SerializedName("add_time")
    val dateSent: Date,

    @SerializedName("for")
    val votesFor: Int,

    @SerializedName("against")
    val votesAgainst: Int,

    @SerializedName("my_vote")
    val myVote: Int
)
