package knk.ee.neverland.groups

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.mikhaellopez.circularimageview.CircularImageView
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.feed.ProofCard
import knk.ee.neverland.models.Group
import knk.ee.neverland.models.Proof
import knk.ee.neverland.network.APIAsyncTask
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

    @BindView(R.id.group_proofs)
    lateinit var groupProofsList: PlaceHolderView

    @BindView(R.id.group_subscribers)
    lateinit var subscribers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)
        setSupportActionBar(toolbar)
        ButterKnife.bind(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        group = intent.extras.get("group") as Group
        setGroupData()
        loadGroupProofs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getFirstImageOrNull(data)
            runUploadAvatarTask(image.path)
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
        showToast("Permissions are not granted.")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode != REQUEST_PERMISSION_REQUEST_CODE) {
            showToast("Some permissions are not granted.")
        }
    }

    private fun setGroupData(loadAvatar: Boolean = true, loadAdminAvatar: Boolean = true) {
        title = group.name
        groupName.text = group.name
        groupAdminName.text = "${group.admin} (${group.admin.userName})"
        subscribers.text = "${Utils.compactHugeNumber(group.quantity)} subscriber(s)"

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

    private fun loadGroupProofs() {
        APIAsyncTask<List<Proof>>()
            .toPool("loadProofs", "GroupDetail")
            .request { DefaultAPI.proofAPI.getProofs(FeedScope.FOLLOWING) }
            .handleResult {
                it.forEach { groupProofsList.addView(ProofCard(this, it)) }
            }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .execute()
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
                    "We need them to save cropped images",
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
            .toPool("uploadAvatar", "GroupDetail")
            .doBefore { showToast("Uploading new avatar") }
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
                    showToast("Avatar uploaded")
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
}
