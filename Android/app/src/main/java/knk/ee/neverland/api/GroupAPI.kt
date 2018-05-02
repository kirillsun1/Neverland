package knk.ee.neverland.api

import knk.ee.neverland.models.Group

interface GroupAPI {
    var token: String

    fun createGroup(groupName: String)

    fun getMyGroups(): List<Group>
}