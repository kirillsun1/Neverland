package knk.ee.neverland.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.feed.UserProofCard
import knk.ee.neverland.models.Proof
import knk.ee.neverland.models.Quest
import knk.ee.neverland.models.User
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.quests.QuestElement
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.Utils
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class ProfileActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private var user: User? = null

    private val REQUEST_PERMISSION_REQUEST_CODE = 1201

    @BindView(R.id.user_proofs)
    lateinit var userProofList: PlaceHolderView

    @BindView(R.id.user_quests)
    lateinit var userQuestsList: PlaceHolderView

    @BindView(R.id.completed_quests_tab_button)
    lateinit var completedQuestsTab: Button

    @BindView(R.id.suggested_quests_tab_button)
    lateinit var suggestedQuestsTab: Button

    @BindView(R.id.profile_follow_button)
    lateinit var followButton: Button

    @BindView(R.id.profile_user_name)
    lateinit var userName: TextView

    @BindView(R.id.profile_collapsing_toolbar)
    lateinit var toolBar: CollapsingToolbarLayout

    @BindView(R.id.profile_user_rating)
    lateinit var userRating: TextView

    @BindView(R.id.profile_user_following)
    lateinit var userFolliwings: TextView

    @BindView(R.id.profile_user_followers)
    lateinit var userFollowers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ButterKnife.bind(this)
        setSupportActionBar(findViewById(R.id.profile_toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        followButton.visibility = GONE
        switchToCompletedQuestsTab()

        getUserIDFromIntent()

        runLoadUserDataTask()
        runLoadQuestTask()
        runGetProofsTask()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getFirstImageOrNull(data)
            runUploadAvatarTask(image.path)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showToast(getString(R.string.no_permissions_granted))
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode != REQUEST_PERMISSION_REQUEST_CODE) {
            showToast(getString(R.string.some_permission_are_not_granted))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        menu!!.findItem(R.id.change_avatar).isVisible = isUserProfile()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change_avatar -> {
                startUploadingAvatar()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @OnClick(R.id.completed_quests_tab_button)
    fun switchToCompletedQuestsTab() {
        userProofList.visibility = VISIBLE
        userQuestsList.visibility = GONE
    }

    @OnClick(R.id.suggested_quests_tab_button)
    fun switchToSuggestedQuestsTab() {
        userQuestsList.visibility = VISIBLE
        userProofList.visibility = GONE
    }

    @OnClick(R.id.profile_follow_button)
    fun onFollowButtonClicked() {
        if (isUserProfile()) {
            return
        }
        val following = followButton.tag as Boolean
        if (following) {
            runChangeFollowingTask(false)
        } else {
            runChangeFollowingTask(true)
        }
    }

    private fun isUserProfile(): Boolean = user == null || user!!.id == DefaultAPI.userID

    @SuppressLint("SetTextI18n")
    private fun setUserData(user: User) {
        userName.text = user.toString()
        toolBar.title = user.userName

        userRating.text = "%.0f%%".format(user.rating * 100)
        userFolliwings.text = Utils.compactHugeNumber(user.following)
        userFollowers.text = Utils.compactHugeNumber(user.followers)

        initializeFollowingButton(user.iFollow)
        loadUserAvatar(user)
    }

    private fun loadUserAvatar(user: User) {
        Glide.with(applicationContext)
            .load(user.avatarLink)
            .apply(RequestOptions()
                .skipMemoryCache(true)
                .placeholder(R.drawable.no_avatar))
            .into(findViewById(R.id.profile_user_avatar))
    }

    private fun initializeFollowingButton(following: Boolean) {
        if (!isUserProfile()) {
            followButton.visibility = VISIBLE
            followButton.isEnabled = true

            followButton.text = getString(if (following) R.string.profile_unfollow else R.string.profile_follow)
            followButton.tag = following
        } else {
            followButton.visibility = GONE
            followButton.isEnabled = false
        }
    }

    private fun getUserIDFromIntent() {
        if (intent.extras != null) {
            user = intent.extras.get("user") as User
        }
    }

    private fun getUserID(): Int = if (user != null) user!!.id else DefaultAPI.userID!!

    private fun runLoadUserDataTask() {
        APIAsyncTask<User>()
            .request {
                DefaultAPI.userAPI.getUserData(getUserID())
            }
            .handleResult {
                setUserData(it)
            }
            .uiErrorView(UIErrorView.Builder().with(this)
                .messageOnAPIFail(R.string.error_getting_user_data)
                .create())
            .execute()
    }

    private fun startUploadingAvatar() {
        if (isUserProfile()) {
            if (EasyPermissions.hasPermissions(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {

                ImagePicker.create(this)
                    .returnMode(ReturnMode.ALL)
                    .folderMode(true)
                    .single()
                    .showCamera(true)
                    .imageDirectory("Neverland")
                    .enableLog(true)
                    .start()
            } else {
                EasyPermissions.requestPermissions(this,
                    getString(R.string.permission_ask),
                    REQUEST_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun runUploadAvatarTask(avatarPath: String) {
        val avatarFile = File(avatarPath)

        var success = false
        APIAsyncTask<Boolean>()
            .doBefore { showToast(getString(R.string.user_uploading_avatar)) }
            .request {
                DefaultAPI.userAPI.uploadAvatar(avatarFile)
                success = true
                true
            }
            .uiErrorView(UIErrorView.Builder().with(this)
                .messageOnAPIFail(R.string.error_uploading_avatar)
                .create())
            .doAfter {
                if (success) {
                    showToast(getString(R.string.user_avatar_uploaded))
                    runLoadUserDataTask()
                }
            }
            .execute()
    }

    private fun runGetProofsTask() {
        APIAsyncTask<List<Proof>>()
            .request {
                DefaultAPI.proofAPI.getProofsByUserID(getUserID())
            }
            .handleResult {
                userProofList.removeAllViews()
                it.forEach { userProofList.addView(UserProofCard(this, it)) }
            }
            .uiErrorView(UIErrorView.Builder().with(this).create())
            .execute()
    }

    private fun runLoadQuestTask() {
        APIAsyncTask<List<Quest>>()
            .request {
                DefaultAPI.questAPI.getSuggestedByUserQuests(getUserID())
            }
            .handleResult {
                userQuestsList.removeAllViews()
                it.forEach { userQuestsList.addView(QuestElement(this, it)) }
            }
            .uiErrorView(UIErrorView.Builder().with(this).create())
            .execute()
    }

    private fun runChangeFollowingTask(follow: Boolean) {
        APIAsyncTask<Boolean>()
            .request {
                if (follow) {
                    DefaultAPI.userAPI.follow(getUserID())
                } else {
                    DefaultAPI.userAPI.unfollow(getUserID())
                }
                true
            }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .doAfter {
                runLoadUserDataTask()
                initializeFollowingButton(follow)
            }
            .execute()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
