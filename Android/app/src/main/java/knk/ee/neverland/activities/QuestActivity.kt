package knk.ee.neverland.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.yalantis.ucrop.UCrop
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Proof
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.views.questview.SimpleProofsListAdapter
import java.io.File
import java.util.logging.Logger

class QuestActivity : AppCompatActivity() {
    private val logger = Logger.getLogger(QuestActivity::class.java.simpleName)

    private var droppingQuest: Boolean = false

    private lateinit var dropQuestButton: Button
    private lateinit var droppingProgress: ProgressBar
    private lateinit var simpleProofsListAdapter: SimpleProofsListAdapter

    private var questID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)

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

        dropQuestButton = findViewById(R.id.drop_quest)
        droppingProgress = findViewById(R.id.dropping_progress)

        dropQuestButton.setOnClickListener {
            askConfirmationAndDropQuest(questID)
        }

        findViewById<Button>(R.id.submit_proof).setOnClickListener {
            openSelectingImageActivity()
        }

        changeDroppingQuestProperty(false)

        runGetProofsTask(questID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (croppingProofSuccessful(resultCode, requestCode)) {
            val resultUri = UCrop.getOutput(data!!)
            openSubmittingProofActivity(resultUri!!.path)
            logger.fine("Cropping successful: ${resultUri.path}")
        }

        if (croppingProofFailed(requestCode, resultCode)) {
            showToast("${getString(R.string.crop_image_error)}: $resultCode")
            logger.severe("Cropping failed with code [$resultCode]: ${UCrop.getError(data!!)}")
        }

        if (proofIsSubmitted(requestCode, resultCode)) {
            showToast(getString(R.string.proof_submitted_message))
            finish()
        }
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

    private fun changeDroppingQuestProperty(dropping: Boolean) {
        droppingQuest = dropping

        dropQuestButton.isEnabled = !dropping
        dropQuestButton.visibility = if (dropping) GONE else VISIBLE
        droppingProgress.visibility = if (!dropping) GONE else VISIBLE
    }

    private fun openSelectingImageActivity() {
        val pickSetup = PickSetup()

        PickImageDialog
            .build(pickSetup)
            .setOnPickResult {
                if (it.error == null) {
                    startCroppingProof(it.path)
                }
            }
            .setOnPickCancel { }
            .show(this)
    }

    private fun startCroppingProof(path: String) {
        val options = UCrop.Options()

        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)
        options.setToolbarColor(colorPrimary)
        options.setActiveWidgetColor(colorPrimary)
        options.setToolbarTitle(getString(R.string.crop_image_title))

        val proofImageFile = File(path)

        val croppedImageFileName = getCroppedImagePath(path)
        val croppedImageFile = File(croppedImageFileName)

        logger.info("Trying to crop image [$path] to [$croppedImageFileName]")

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

    private fun openSubmittingProofActivity(pathToImage: String) {
        val intent = Intent(this, SubmitProofActivity::class.java)

        intent.putExtra("pathToImage", pathToImage)
        intent.putExtra("questID", questID)

        startActivityForResult(intent, Constants.SUBMITTING_PROOF_REQUEST_CODE)
    }

    private fun croppingProofSuccessful(resultCode: Int, requestCode: Int) =
        resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP

    private fun croppingProofFailed(requestCode: Int, resultCode: Int) =
        requestCode == UCrop.REQUEST_CROP && resultCode == UCrop.RESULT_ERROR

    private fun proofIsSubmitted(requestCode: Int, resultCode: Int): Boolean =
        requestCode == Constants.SUBMITTING_PROOF_REQUEST_CODE && resultCode == Constants.SUCCESS_CODE

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun runDropQuestTask(questID: Int) {
        if (!droppingQuest) {
            var success = false

            APIAsyncRequest.Builder<Boolean>()
                .before {
                    droppingQuest = true
                    changeDroppingQuestProperty(true)
                }
                .request {
                    DefaultAPI.questAPI.dropQuest(questID)
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
                    changeDroppingQuestProperty(false)
                    droppingQuest = false
                }
                .finish()
                .execute()
        }
    }

    private fun runGetProofsTask(questID: Int) {
        APIAsyncRequest.Builder<List<Proof>>()
            .request { DefaultAPI.proofAPI.getProofsByQuestID(questID) }
            .handleResult { simpleProofsListAdapter.addProofs(it!!) }
            .setContext(this)
            .showMessages(true)
            .finish()
            .execute()
    }
}
