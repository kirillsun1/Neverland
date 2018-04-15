package knk.ee.neverland.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import com.bumptech.glide.Glide
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.yalantis.ucrop.UCrop
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.GroupToCreate
import knk.ee.neverland.utils.APIAsyncRequest
import kotlinx.android.synthetic.main.activity_create_group.toolbar
import java.io.File

class CreateGroupActivity : AppCompatActivity() {

    private var creatingGroupInProcess: Boolean = false
    private var avatarPath: String = ""
    private lateinit var groupNameBox: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        groupNameBox = findViewById(R.id.group_name)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (croppingAvatarFailed(requestCode, resultCode)) {
            throw UCrop.getError(data!!)!!
        }

        if (croppingAvatarSuccessful(requestCode, resultCode)) {
            avatarPath = UCrop.getOutput(data!!)!!.path!!
            setSelectedImage(avatarPath)
        }
    }

    fun createGroup(view: View) {
        val groupToCreate = makeGroupToCreate()

        groupNameBox.error = null
        if (validateGroupName(groupToCreate.groupName)) {
            askConfirmationAndCreateGroup(groupToCreate)
        }
    }

    private fun validateGroupName(groupName: String): Boolean {
        val valid = groupName.replace(" ", "").length > 3

        if (!valid) {
            groupNameBox.error = "Incorrect group name"
        }

        return valid
    }

    fun changeAvatar(view: View) {
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

    private fun setSelectedImage(path: String) {
        val imageFile = File(path)

        Glide.with(this)
            .load(imageFile)
            .into(findViewById(R.id.group_avatar))
    }

    private fun makeGroupToCreate(): GroupToCreate {
        return GroupToCreate(
            groupNameBox.text!!.toString()
        )
    }

    private fun askConfirmationAndCreateGroup(groupToCreate: GroupToCreate) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.creating_group_confirmation))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.yes), { dialogInterface: DialogInterface, _: Int ->
                runCreateGroupTask(groupToCreate)
                dialogInterface.cancel()
            })
            .setNegativeButton(R.string.no, { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.cancel()
            })
            .create()
            .show()
    }

    private fun runCreateGroupTask(groupToCreate: GroupToCreate) {
        if (!creatingGroupInProcess) {
            var success = false

            APIAsyncRequest.Builder<Boolean>()
                .before {
                    creatingGroupInProcess = true
                }
                .request {
                    DefaultAPI.groupAPI.createGroup(groupToCreate)
                    success = true
                    true
                }
                .onAPIFailMessage { R.string.error_failed_dropping_quest }
                .setContext(this)
                .showMessages(true)
                .after {
                    if (success) {
                        finish()
                    }
                    creatingGroupInProcess = false
                }
                .finish()
                .execute()
        }
    }
}
