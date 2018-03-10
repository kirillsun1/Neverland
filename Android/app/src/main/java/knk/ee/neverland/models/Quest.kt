package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName

class Quest {
    var id: Int = 0

    @SerializedName("title")
    var title: String = ""

    @SerializedName("desc")
    var description: String = ""

    @SerializedName("author")
    var creator: User = User()

    // @SerializedName("time_created")
    // var timeCreated: LocalDateTime? = null // TODO: Date for older api

    var groupID: Int = 0
}
