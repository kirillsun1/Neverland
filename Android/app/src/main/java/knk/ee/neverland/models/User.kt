package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("u_id")
    val id: Int,

    @SerializedName("user_name")
    val userName: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("second_name")
    val secondName: String,

    @SerializedName("avatar")
    val avatarLink: String,
    val rating: Double,
    val following: Int,
    val followers: Int,
    var iFollow: Boolean
) {

    override fun toString(): String {
        return "$firstName $secondName"
    }
}