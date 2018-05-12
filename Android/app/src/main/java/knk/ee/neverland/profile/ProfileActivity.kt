package knk.ee.neverland.profile

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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mindorks.placeholderview.PlaceHolderView
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.feed.UserProofCard
import knk.ee.neverland.models.Proof
import knk.ee.neverland.models.Quest
import knk.ee.neverland.models.User
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.quests.SuggestedQuestElement
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.Utils
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class ProfileActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private var userID: Int = Int.MIN_VALUE

    private val SELECTING_IMAGE_REQUEST_CODE = 1200
    private val REQUEST_PERMISSION_REQUEST_CODE = 1201
    private val SELECTING_IMAGE_SUCCESS_RESULT_CODE = -1

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

        if (requestCode == SELECTING_IMAGE_REQUEST_CODE && resultCode == SELECTING_IMAGE_SUCCESS_RESULT_CODE) {
            val photos = data!!.getSerializableExtra(GalleryActivity.PHOTOS) as List<*>
            runUploadAvatarTask(photos[0] as String)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showToast("Permissions are not granted.")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode != REQUEST_PERMISSION_REQUEST_CODE) {
            showToast("Some permissions are not granted.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        menu!!.findItem(R.id.change_avatar).isVisible = isMyProfile()
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
        if (isMyProfile()) {
            return
        }

        val following = followButton.tag as Boolean
        if (following) {
            runChangeFollowingTask(false)
        } else {
            runChangeFollowingTask(true)
        }
    }

    private fun isMyProfile(): Boolean = userID == DefaultAPI.userID

    private fun setUserData(user: User) {
        findViewById<TextView>(R.id.profile_user_name).text = user.toString()
        findViewById<CollapsingToolbarLayout>(R.id.profile_collapsing_toolbar).title = user.userName

        val ratingString = "%.0f%%".format(user.rating * 100)
        findViewById<TextView>(R.id.profile_user_rating).text = ratingString
        findViewById<TextView>(R.id.profile_user_following).text = Utils.compactHugeNumber(user.following)
        findViewById<TextView>(R.id.profile_user_followers).text = Utils.compactHugeNumber(user.followers)

        initializeFollowingButton(user.iFollow)
        loadUserAvatar(user)
    }

    private fun loadUserAvatar(user: User) {
        Glide.with(applicationContext)
            .load(user.avatarLink)
            .apply(RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_avatar))
            .into(findViewById(R.id.profile_user_avatar))
    }

    private fun initializeFollowingButton(following: Boolean) {
        if (!isMyProfile()) {
            followButton.visibility = VISIBLE
            followButton.isEnabled = true

            followButton.text = if (following) "Unfollow" else "Follow"
            followButton.tag = following
        } else {
            followButton.visibility = GONE
            followButton.isEnabled = false
        }
    }

    private fun getUserIDFromIntent() {
        if (intent.extras != null) {
            userID = intent.extras.getInt("userID")
        } else {
            userID = DefaultAPI.userID!!
        }
    }

    private fun runLoadUserDataTask() {
        APIAsyncTask<User>()
            .request {
                DefaultAPI.userAPI.getUserData(userID)
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
        if (isMyProfile()) {
            if (EasyPermissions.hasPermissions(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {

                val galleryConfig = GalleryConfig.Build()
                    .singlePhoto(true)
                    .build()

                GalleryActivity.openActivity(this, SELECTING_IMAGE_REQUEST_CODE, galleryConfig)
            } else {
                EasyPermissions.requestPermissions(this,
                    "We need them to save cropped images",
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
            .doBefore { showToast("Uploading new avatar") }
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
                    showToast("Avatar uploaded")
                    runLoadUserDataTask()
                }
            }
            .execute()
    }

    private fun runGetProofsTask() {
        APIAsyncTask<List<Proof>>()
            .request {
                DefaultAPI.proofAPI.getProofsByUserID(userID)
            }
            .handleResult { it.forEach { userProofList.addView(UserProofCard(this, it)) } }
            .uiErrorView(UIErrorView.Builder().with(this).create())
            .execute()
    }

    private fun runLoadQuestTask() {
        APIAsyncTask<List<Quest>>()
            .request {
                DefaultAPI.questAPI.getSuggestedByUserQuests(userID)
            }
            .handleResult { it.forEach { userQuestsList.addView(SuggestedQuestElement(this, it)) } }
            .uiErrorView(UIErrorView.Builder().with(this).create())
            .execute()
    }

    private fun runChangeFollowingTask(follow: Boolean) {
        APIAsyncTask<Boolean>()
            .request {
                if (follow) {
                    DefaultAPI.userAPI.follow(userID)
                } else {
                    DefaultAPI.userAPI.unfollow(userID)
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
