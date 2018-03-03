package knk.ee.neverland.api

import android.os.AsyncTask
import knk.ee.neverland.fakeapi.FakeQuestAPI
import knk.ee.neverland.neverlandapi.NeverlandAuthAPI

object DefaultAPI {
    private var userKey: String? = null
    private var userLogin: String? = null

    private var checkKeyTask: CheckKeyTask? = null

    val authAPI: AuthAPI = NeverlandAuthAPI()
    val questAPI: QuestApi = FakeQuestAPI()

    val isKeySet: Boolean
        get() = userKey != null && userLogin != null

    fun setUserData(login: String, key: String) {
        if (!key.isEmpty() && !login.isEmpty()) {
            checkKeyTask = CheckKeyTask(key, login)
            checkKeyTask!!.execute()
        }
    }

    private class CheckKeyTask(var key: String, var login: String) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg p0: Void?): Boolean {
            if (!isKeySet) return false
            return authAPI.isKeyActive(key)
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                userKey = key
                userLogin = login

                (questAPI as FakeQuestAPI).key = key
            }
        }
    }
}
