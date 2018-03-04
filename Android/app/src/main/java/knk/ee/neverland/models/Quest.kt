package knk.ee.neverland.models

import java.time.LocalDateTime

class Quest {
    private val id: Int = 0
    var name: String? = null
    var description: String? = null
    private val creator: User? = null
    private val timeCreated: LocalDateTime? = null
    var groupID: Int = 0
}
