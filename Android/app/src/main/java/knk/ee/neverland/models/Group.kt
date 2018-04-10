package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName
import knk.ee.neverland.datetime.DateTime

data class Group(
    val id: Int,

    val name: String,

    val admin: User,

    val quantity: Int, // ???

    @SerializedName("creation_time")
    val created: DateTime
)
