package knk.ee.neverland.api

import knk.ee.neverland.models.Group
import java.io.File

interface GroupAPI {
    var token: String

    fun createGroup(groupName: String)

    fun getMyGroups(): List<Group>

    fun uploadAvatar(groupID: Int, avatar: File)

    fun getGroup(groupID: Int): Group
}