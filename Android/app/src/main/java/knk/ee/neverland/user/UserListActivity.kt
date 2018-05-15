package knk.ee.neverland.user

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.User
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.ViewProgressController

class UserListActivity : AppCompatActivity() {

    @BindView(R.id.user_list)
    lateinit var userList: PlaceHolderView

    @BindView(R.id.loading_progress)
    lateinit var loadingProgress: ProgressBar

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private lateinit var listProgressView: ViewProgressController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        listProgressView = ViewProgressController(loadingProgress, userList)
        listProgressView.showProgress()

        runTaskFromIntent()
    }

    private fun runTaskFromIntent() {
        val task = intent.extras.getString("userLoadTask")

        when (task) {
            "groupSubscribers" -> runLoadGroupSubscribersTask(intent.extras.getInt("groupID"))

            "userFollowings" -> runLoadUserFollowingsTask(intent.extras.getInt("userID"))

            "userFollowers" -> runLoadUserFollowersTask(intent.extras.getInt("userID"))

            else -> finish()
        }
    }

    private fun runLoadGroupSubscribersTask(groupId: Int) {
        APIAsyncTask<List<User>>()
            .request { DefaultAPI.groupAPI.getSubscribers(groupId) }
            .handleResult { addUsersToList(it) }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .execute()
    }

    private fun runLoadUserFollowingsTask(userID: Int) {
        APIAsyncTask<List<User>>()
            .request { DefaultAPI.userAPI.getFollowings(userID) }
            .handleResult { addUsersToList(it) }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .execute()
    }

    private fun runLoadUserFollowersTask(userID: Int) {
        APIAsyncTask<List<User>>()
            .request { DefaultAPI.userAPI.getFollowers(userID) }
            .handleResult { addUsersToList(it) }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .execute()
    }

    private fun addUsersToList(users: List<User>) {
        userList.removeAllViews()
        users.forEach { userList.addView(UserElement(this, it)) }
        listProgressView.hideProgress()
    }
}
