package knk.ee.neverland.api.neverlandapi

import knk.ee.neverland.api.UserAPI
import knk.ee.neverland.models.User
import java.io.File

class NeverlandUserAPI : UserAPI {
    override var token: String = ""

    override fun getMyData(): User {
        val u = User()
        u.firstName = ""
        u.secondName = ""
        u.userName = ""

        u.avatarLink = "https://i.gyazo.com/d3ddcbf9bd9eef6ee7df3249b41b82d4.png"

        return u
    }

    override fun getUserData(userID: Int): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uploadAvatar(file: File) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}