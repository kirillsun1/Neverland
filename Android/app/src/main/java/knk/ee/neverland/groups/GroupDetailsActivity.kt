package knk.ee.neverland.groups

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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
import com.mikhaellopez.circularimageview.CircularImageView
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Group
import knk.ee.neverland.models.Quest
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.profile.ProfileActivity
import knk.ee.neverland.quests.CreateQuestActivity
import knk.ee.neverland.quests.QuestElement
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.Utils
import kotlinx.android.synthetic.main.activity_group_details.toolbar
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class GroupDetailsActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val REQUEST_PERMISSION_REQUEST_CODE = 1201

    lateinit var group: Group

    @BindView(R.id.group_avatar)
    lateinit var groupAvatar: CircularImageView

    @BindView(R.id.group_name)
    lateinit var groupName: TextView

    @BindView(R.id.group_admin)
    lateinit var groupAdminName: TextView

    @BindView(R.id.group_admin_avatar)
    lateinit var groupAdminAvatar: CircularImageView

    @BindView(R.id.group_quests)
    lateinit var groupQuestsList: PlaceHolderView

    @BindView(R.id.group_subscribers)
    lateinit var subscribers: TextView

    @BindView(R.id.group_leave)
    lateinit var groupLeave: Button

    @BindView(R.id.group_admin_layout)
    lateinit var groupAdminLayout: ConstraintLayout

    @BindView(R.id.group_suggest_quest)
    lateinit var suggestQuest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)
        setSupportActionBar(toolbar)
        ButterKnife.bind(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        group = intent.extras.get("group") as Group
        setGroupData()
        runLoadGroupQuestsTask()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getFirstImageOrNull(data)
            runUploadAvatarTask(image.path)
        }

        if (requestCode == Constants.SUBMIT_NEW_QUEST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showToast("Quest created")
            runLoadGroupQuestsTask()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        menu!!.findItem(R.id.change_avatar).isVisible = isUserAdmin()
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

    @OnClick(R.id.group_admin_layout)
    fun onGroupAdminClick() {
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        intent.putExtra("user", group.admin)
        startActivity(intent)
    }

    @OnClick(R.id.group_suggest_quest)
    fun openSuggestQuestActivity() {
        val intent = Intent(applicationContext, CreateQuestActivity::class.java)
        intent.putExtra("group", group)
        startActivityForResult(intent, Constants.SUBMIT_NEW_QUEST_REQUEST_CODE)
    }

    @OnClick(R.id.group_leave)
    fun onLeaveGroupClick() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to leave this group?")
            .setPositiveButton(R.string.yes, { dialogInterface, i ->
                runLeaveGroupTask()
                dialogInterface.dismiss()
            })
            .setNegativeButton(R.string.no, { dialogInterface, i ->
                dialogInterface.cancel()
            })
            .create()
            .show()
    }

    private fun runLoadGroupQuestsTask() {
        APIAsyncTask<List<Quest>>()
            .request {
                DefaultAPI.questAPI.getGroupQuests(group.id)
            }
            .handleResult {
                groupQuestsList.removeAllViews()
                it.forEach { groupQuestsList.addView(QuestElement(this, it)) }
            }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .execute()
    }

    private fun setGroupData(loadAvatar: Boolean = true, loadAdminAvatar: Boolean = true) {
        title = group.name
        groupName.text = group.name
        groupAdminName.text = getString(R.string.group_admin_text).format(group.admin, group.admin.userName)
        subscribers.text = getString(R.string.group_subscribers).format(Utils.compactHugeNumber(group.quantity))

        if (loadAvatar) {
            loadGroupAvatar()
        }

        if (loadAdminAvatar) {
            loadAdminAvatar()
        }
    }

    private fun loadGroupAvatar() {
        Glide.with(applicationContext)
            .load(group.avatarLink)
            .apply(RequestOptions()
                .skipMemoryCache(true)
                .placeholder(R.drawable.no_avatar_group))
            .into(groupAvatar)
    }

    private fun loadAdminAvatar() {
        Glide.with(applicationContext)
            .load(group.admin.avatarLink)
            .apply(RequestOptions()
                .skipMemoryCache(true)
                .placeholder(R.drawable.no_avatar))
            .into(groupAdminAvatar)
    }

    private fun isUserAdmin(): Boolean = group.admin.id == DefaultAPI.userID

    private fun startUploadingAvatar() {
        if (isUserAdmin()) {
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
                    .theme(R.style.AppTheme_NoActionBar)
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun runUploadAvatarTask(path: String) {
        val avatarFile = File(path)

        var success = false
        APIAsyncTask<Boolean>()
            .doBefore { showToast(getString(R.string.group_uploading_avatar)) }
            .request {
                DefaultAPI.groupAPI.uploadAvatar(group.id, avatarFile)
                success = true
                true
            }
            .uiErrorView(UIErrorView.Builder().with(this)
                .messageOnAPIFail(R.string.error_uploading_avatar)
                .create())
            .doAfter {
                if (success) {
                    showToast(getString(R.string.group_avatar_uploaded))
                    runUpdateGroupDataTask(group.id)
                }
            }
            .execute()
    }

    private fun runUpdateGroupDataTask(groupID: Int) {
        APIAsyncTask<Group>()
            .request {
                DefaultAPI.groupAPI.getGroup(groupID)
            }
            .handleResult {
                group = it
                setGroupData(loadAdminAvatar = false)
            }
            .uiErrorView(UIErrorView.Builder().with(this)
                .messageOnAPIFail(R.string.error_getting_user_data)
                .create())
            .execute()

    }

    private fun runLeaveGroupTask() {
        var left = false
        APIAsyncTask<Boolean>()
            .request {
                DefaultAPI.groupAPI.unsubscribe(group.id)
                left = true
                true
            }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .doAfter {
                if (left) {
                    finish()
                }
            }
            .execute()
    }
}
