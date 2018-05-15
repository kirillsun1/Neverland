package knk.ee.neverland.api

import knk.ee.neverland.models.Group
import knk.ee.neverland.models.User
import java.io.File

interface GroupAPI {
    var token: String

    fun createGroup(groupName: String)

    fun getMyGroups(): List<Group>

    fun uploadAvatar(groupID: Int, avatar: File)

    fun getGroup(groupID: Int): Group

    fun subscribe(groupID: Int)

    fun unsubscribe(groupID: Int)

    fun getGroupsToJoin(): List<Group>

    fun getSubscribers(groupID: Int): List<User>
}