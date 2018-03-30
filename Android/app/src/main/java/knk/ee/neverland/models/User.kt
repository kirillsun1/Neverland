package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("user_name")
    var userName: String = ""
    @SerializedName("first_name")
    var firstName: String = ""
    @SerializedName("second_name")
    var secondName: String = ""

    var avatarLink: String = ""

    override fun toString(): String {
        return "$firstName $secondName"
    }
}
