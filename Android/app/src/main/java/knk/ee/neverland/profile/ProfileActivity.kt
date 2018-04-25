package knk.ee.neverland.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mindorks.placeholderview.PlaceHolderView
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.yalantis.ucrop.UCrop
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.feed.ProofCard
import knk.ee.neverland.models.Proof
import knk.ee.neverland.models.Quest
import knk.ee.neverland.models.User
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.Utils
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private var userID: Int? = null

    @BindView(R.id.user_proofs)
    lateinit var userProofList: PlaceHolderView

    @BindView(R.id.user_quests)
    lateinit var userQuestsList: PlaceHolderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ButterKnife.bind(this)
        setSupportActionBar(findViewById(R.id.profile_toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        switchToCompletedQuestsTab()

        getUserIDFromIntent()
        hideFollowButtonIfOwnProfile()

        runLoadUserDataTask()
        runLoadQuestTask()
        runGetProofsTask()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (croppingAvatarFailed(requestCode, resultCode)) {
            throw UCrop.getError(data!!)!!
        }

        if (croppingAvatarSuccessful(requestCode, resultCode)) {
            runUploadAvatarTask(UCrop.getOutput(data!!)!!.path!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        menu!!.findItem(R.id.change_avatar).isVisible = userID == null
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

    // @Click(R.id.completed_quests_tab_button)
    fun switchToCompletedQuestsTab() {
        userProofList.visibility = VISIBLE
        userQuestsList.visibility = GONE
    }

    // @Click(R.id.suggested_quests_tab_button)
    fun switchToSuggestedQuestsTab() {
        userQuestsList.visibility = VISIBLE
        userProofList.visibility = GONE
    }

    private fun setUserData(user: User) {
        findViewById<TextView>(R.id.profile_user_name).text = user.toString()
        findViewById<CollapsingToolbarLayout>(R.id.profile_collapsing_toolbar).title = user.userName

        val ratingString = "%.0f%%".format(user.rating * 100)
        findViewById<TextView>(R.id.profile_user_rating).text = ratingString
        findViewById<TextView>(R.id.profile_user_following).text = Utils.compactHugeNumber(user.following)
        findViewById<TextView>(R.id.profile_user_followers).text = Utils.compactHugeNumber(user.followers)

        loadUserAvatar(user)
    }

    private fun loadUserAvatar(user: User) {
        Glide.with(this)
            .load(user.avatarLink)
            .transition(DrawableTransitionOptions.withCrossFade(resources.getInteger(
                R.integer.feed_fade_animation_duration)))
            .into(findViewById(R.id.profile_user_avatar))
    }

    private fun hideFollowButtonIfOwnProfile() {
        val followButton = findViewById<Button>(R.id.profile_follow_button)

        followButton.visibility = if (userID == null) GONE else VISIBLE
        followButton.isEnabled = userID != null
        // TODO: Following
    }

    private fun getUserIDFromIntent() {
        if (intent.extras != null) {
            userID = intent.extras.getInt("userID")
        }
    }

    private fun runLoadUserDataTask() {
        APIAsyncTask<User>()
            .request {
                if (userID == null)
                    DefaultAPI.userAPI.getMyData()
                else
                    DefaultAPI.userAPI.getUserData(userID!!)
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
        if (userID == null) {
            val pickSetup = PickSetup()

            PickImageDialog
                .build(pickSetup)
                .setOnPickResult {
                    if (it.error == null) {
                        startCroppingAvatar(it.path)
                    }
                }
                .setOnPickCancel { }
                .show(this)
        }
    }

    private fun startCroppingAvatar(path: String) {
        val options = UCrop.Options()

        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)
        options.setToolbarColor(colorPrimary)
        options.setActiveWidgetColor(colorPrimary)
        options.setToolbarTitle(getString(R.string.crop_image_title))

        val proofImageFile = File(path)

        val croppedImageFileName = getCroppedImagePath(path)
        val croppedImageFile = File(croppedImageFileName)

        UCrop.of(Uri.fromFile(proofImageFile), Uri.fromFile(croppedImageFile))
            .withAspectRatio(1F, 1F)
            .withOptions(options)
            .start(this)
    }

    private fun getCroppedImagePath(path: String): String {
        val pathToDir = path.substring(0, path.lastIndexOf("/") + 1)
        val pathFormat = path.substring(path.lastIndexOf(".") + 1)
        return "${pathToDir}neverland_proof${System.nanoTime()}.$pathFormat"
    }

    private fun croppingAvatarSuccessful(requestCode: Int, resultCode: Int) =
        resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP

    private fun croppingAvatarFailed(requestCode: Int, resultCode: Int) =
        requestCode == UCrop.REQUEST_CROP && resultCode == UCrop.RESULT_ERROR

    private fun runUploadAvatarTask(avatarPath: String) {
        val avatarFile = File(avatarPath)

        var success = false
        APIAsyncTask<Boolean>()
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
                    runLoadUserDataTask()
                }
            }
            .execute()
    }

    private fun runGetProofsTask() {
        APIAsyncTask<List<Proof>>()
            .request {
                if (userID == null)
                    DefaultAPI.proofAPI.getMyProofs()
                else
                    DefaultAPI.proofAPI.getProofsByUserID(userID!!)
            }
            .handleResult { it.forEach { userProofList.addView(ProofCard(this, it)) } }
            .uiErrorView(UIErrorView.Builder().with(this).create())
            .execute()
    }

    private fun runLoadQuestTask() {
        APIAsyncTask<List<Quest>>()
            .request {
                if (userID == null)
                    DefaultAPI.questAPI.getSuggestedByMeQuests()
                else
                    DefaultAPI.questAPI.getSuggestedByUserQuests(userID!!)
            }
            .handleResult { it.forEach { userQuestsList.addView(SuggestedQuestElement(this, it)) } }
            .uiErrorView(UIErrorView.Builder().with(this).create())
            .execute()
    }
}
