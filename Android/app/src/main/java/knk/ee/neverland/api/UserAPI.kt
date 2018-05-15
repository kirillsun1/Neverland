package knk.ee.neverland.api

import knk.ee.neverland.models.User
import java.io.File

interface UserAPI {
    var token: String

    fun getMyData(): User

    fun getUserData(userID: Int): User

    fun uploadAvatar(file: File)

    fun follow(userID: Int)

    fun unfollow(userID: Int)
    fun getFollowers(userID: Int): List<User>
    fun getFollowings(userID: Int): List<User>
}