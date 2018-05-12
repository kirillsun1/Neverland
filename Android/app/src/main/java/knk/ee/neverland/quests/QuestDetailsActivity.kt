package knk.ee.neverland.quests

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import knk.ee.neverland.R
import knk.ee.neverland.activities.SubmitProofActivity
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Proof
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.ViewProgressController
import pub.devrel.easypermissions.EasyPermissions

class QuestDetailsActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val SELECTING_IMAGE_REQUEST_CODE = 1200
    private val REQUEST_PERMISSION_REQUEST_CODE = 1201
    private val SELECTING_IMAGE_SUCCESS_RESULT_CODE = -1

    @BindView(R.id.drop_quest)
    lateinit var dropQuestButton: Button

    @BindView(R.id.submit_proof)
    lateinit var submitProofButton: Button

    @BindView(R.id.dropping_progress)
    lateinit var droppingProgress: ProgressBar

    private lateinit var simpleProofsListAdapter: SimpleProofsListAdapter
    private lateinit var buttonsProgressController: ViewProgressController
    private var questID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_details)
        ButterKnife.bind(this)

        simpleProofsListAdapter = SimpleProofsListAdapter(this)

        questID = intent.extras!!.getInt("questID")
        val questName = intent.extras!!.getString("questTitle")
        val questDesc = intent.extras!!.getString("questDesc")
        val questAuthor = intent.extras!!.getString("questAuthor")
        val questCreatedDate = intent.extras!!.getString("questCreatedDate")

        title = questName
        findViewById<TextView>(R.id.quest_desc).text = questDesc
        findViewById<TextView>(R.id.quest_author).text = questAuthor
        findViewById<TextView>(R.id.quest_created_date).text = questCreatedDate
        findViewById<ListView>(R.id.quest_proofs).adapter = simpleProofsListAdapter

        buttonsProgressController =
                ViewProgressController(droppingProgress, dropQuestButton, submitProofButton)

        runGetProofsTask(questID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECTING_IMAGE_REQUEST_CODE && resultCode == SELECTING_IMAGE_SUCCESS_RESULT_CODE) {
            val photos = data!!.getSerializableExtra(GalleryActivity.PHOTOS) as List<*>
            openSubmittingProofActivity(photos[0] as String)
        }

        if (proofIsSubmitted(requestCode, resultCode)) {
            showToast(getString(R.string.proof_submitted_message))
            finish()
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
        if (requestCode == REQUEST_PERMISSION_REQUEST_CODE) {
            println(perms)
            openSelectingImageActivity()
        } else {
            showToast("Some permissions are not granted.")
        }
    }

    @OnClick(R.id.drop_quest)
    fun onDropQuestButtonClick() {
        askConfirmationAndDropQuest(questID)
    }

    @OnClick(R.id.submit_proof)
    fun onSubmitProofButtonClick() {
        openSelectingImageActivity()
    }

    private fun askConfirmationAndDropQuest(questID: Int) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.dropping_quest_confirmation))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.yes), { dialogInterface: DialogInterface, _: Int ->
                runDropQuestTask(questID)
                dialogInterface.cancel()
            })
            .setNegativeButton(R.string.no, { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.cancel()
            })
            .create()
            .show()
    }


    private fun openSelectingImageActivity() {
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

    private fun openSubmittingProofActivity(pathToImage: String) {
        val intent = Intent(this, SubmitProofActivity::class.java)

        intent.putExtra("pathToImage", pathToImage)
        intent.putExtra("questID", questID)

        startActivityForResult(intent, Constants.SUBMITTING_PROOF_REQUEST_CODE)
    }

    private fun proofIsSubmitted(requestCode: Int, resultCode: Int): Boolean =
        requestCode == Constants.SUBMITTING_PROOF_REQUEST_CODE && resultCode == Constants.SUCCESS_CODE

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun runDropQuestTask(questID: Int) {
        var success = false

        APIAsyncTask<Boolean>()
            .doBefore {
                buttonsProgressController.showProgress()
            }
            .request {
                DefaultAPI.questAPI.dropQuest(questID)
                success = true
                true
            }
            .uiErrorView(UIErrorView.Builder().with(this)
                .messageOnAPIFail(R.string.error_failed_dropping_quest)
                .create())
            .doAfter {
                if (success) {
                    finish()
                }
                buttonsProgressController.hideProgress()
            }
            .execute()
    }

    private fun runGetProofsTask(questID: Int) {
        APIAsyncTask<List<Proof>>()
            .request { DefaultAPI.proofAPI.getProofsByQuestID(questID) }
            .handleResult { simpleProofsListAdapter.addProofs(it) }
            .uiErrorView(UIErrorView.Builder().with(this).create())
            .execute()
    }
}
