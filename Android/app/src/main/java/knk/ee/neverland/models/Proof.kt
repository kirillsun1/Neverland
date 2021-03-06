package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDateTime

data class Proof(
    val id: Int,

    val comment: String?,

    @SerializedName("pic_path")
    val imageLink: String,

    val quest: Quest,

    @SerializedName("proofer")
    val sender: User,

    @SerializedName("add_time")
    val dateSent: LocalDateTime,

    var rating: Rating,

    @SerializedName("my_vote")
    val myVote: Int
)
