package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDateTime

data class Quest(
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("desc")
    val description: String,

    @SerializedName("author")
    val author: User,

    @SerializedName("time_created")
    val timeCreated: LocalDateTime,

    @SerializedName("time_taken")
    var timeTaken: LocalDateTime?,

    val groupID: Int
)
