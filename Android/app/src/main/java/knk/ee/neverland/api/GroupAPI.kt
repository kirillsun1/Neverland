package knk.ee.neverland.api

import knk.ee.neverland.api.models.GroupToCreate
import knk.ee.neverland.models.Group

interface GroupAPI {
    var token: String

    fun createGroup(group: GroupToCreate)

    fun getMyGroups(): List<Group>
}