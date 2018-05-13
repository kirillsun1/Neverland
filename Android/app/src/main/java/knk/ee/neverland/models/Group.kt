package knk.ee.neverland.models

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDateTime
import java.io.Serializable

data class Group(val id: Int,
                 val name: String,
                 val admin: User,
                 val quantity: Int,
                 val avatarLink: String?,
                 @SerializedName("creation_time") val created: LocalDateTime
) : Serializable
