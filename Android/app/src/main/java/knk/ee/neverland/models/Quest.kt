package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName
import knk.ee.neverland.datetime.DateTime

class Quest {
    var id: Int = 0

    @SerializedName("title")
    var title: String = ""

    @SerializedName("desc")
    var description: String = ""

    @SerializedName("author")
    var creator: User = User()

    @SerializedName("time_created")
    var timeCreated: DateTime = DateTime()

    @SerializedName("time_taken")
    var timeTaken: DateTime = DateTime()

    var groupID: Int = 0
}
