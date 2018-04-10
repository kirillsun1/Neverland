package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName
import knk.ee.neverland.datetime.DateTime

data class Quest(
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("desc")
    val description: String,

    @SerializedName("author")
    val author: User,

    @SerializedName("time_created")
    val timeCreated: DateTime,

    @SerializedName("time_taken")
    var timeTaken: DateTime?,

    val groupID: Int
) {
    fun isTaken() = timeTaken != null
}
