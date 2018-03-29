package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.esafirm.imagepicker.features.ImagePicker
import com.yalantis.ucrop.UCrop
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.views.questview.QuestPictureAdapter
import java.io.File

class QuestActivity : AppCompatActivity() {
    private var droppingQuest: Boolean = false

    private var dropQuestButton: Button? = null
    private var droppingProgress: ProgressBar? = null

    private var questID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)

        val pictureAdapter = QuestPictureAdapter(this)

        questID = intent.extras!!.getInt("questID")
        val questName = intent.extras!!.getString("questTitle")
        val questDesc = intent.extras!!.getString("questDesc")
        val questAuthor = intent.extras!!.getString("questAuthor")
        val questCreatedDate = intent.extras!!.getString("questCreatedDate")

        title = questName
        (findViewById<View>(R.id.quest_desc) as TextView).text = questDesc
        (findViewById<View>(R.id.quest_author) as TextView).text = questAuthor
        (findViewById<View>(R.id.quest_created_date) as TextView).text = questCreatedDate
        (findViewById<View>(R.id.quest_images) as GridView).adapter = pictureAdapter

        dropQuestButton = findViewById<Button>(R.id.drop_quest)
        droppingProgress = findViewById<ProgressBar>(R.id.dropping_progress)

        dropQuestButton!!.setOnClickListener {
            if (!droppingQuest) {
                askConfirmationAndDropQuest(questID)
            }
        }

        findViewById<Button>(R.id.submit_proof).setOnClickListener {
            openSelectingProofImageActivity()
        }

        changeDroppingQuestProperty(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getImages(data).firstOrNull()
            openCroppingActivity(image!!.path)
        }

        if (croppingProofSuccessful(resultCode, requestCode)) {
            val resultUri = UCrop.getOutput(data!!)
            openSubmittingProofActivity(resultUri!!.path)
        }

        if (croppingProofFailed(requestCode, resultCode)) {
            showToast(getString(R.string.crop_image_error))
        }

        // TODO: If proof was sent?

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun askConfirmationAndDropQuest(questID: Int) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.dropping_quest_confirmation))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.yes), { dialogInterface: DialogInterface, _: Int ->
                changeDroppingQuestProperty(true)
                DropQuestTask(questID).execute()
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

        dropQuestButton!!.isEnabled = !dropping
        dropQuestButton!!.visibility = if (dropping) GONE else VISIBLE
        droppingProgress!!.visibility = if (!dropping) GONE else VISIBLE
    }

    private fun openSelectingProofImageActivity() {
        val imageTitle = getString(R.string.proof_select_image)

        ImagePicker.create(this)
            .single()
            .showCamera(true)
            .theme(R.style.AppTheme_NoActionBar)
            .toolbarImageTitle(imageTitle)
            .start()
    }

    private fun openCroppingActivity(path: String?) {
        val options = UCrop.Options()

        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)
        options.setToolbarColor(colorPrimary)
        options.setActiveWidgetColor(colorPrimary)
        options.setToolbarTitle(getString(R.string.crop_image_title))

        UCrop.of(Uri.fromFile(File(path)), Uri.fromFile(File(path + "_neverland")))
            .withAspectRatio(1F, 1F)
            .withOptions(options)
            .start(this)
    }

    private fun openSubmittingProofActivity(pathToImage: String) {
        val intent = Intent(this, SubmitProofActivity::class.java)

        intent.putExtra("pathToImage", pathToImage)
        intent.putExtra("questID", questID)

        startActivity(intent)
    }

    private fun croppingProofSuccessful(resultCode: Int, requestCode: Int) =
        resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP

    private fun croppingProofFailed(requestCode: Int, resultCode: Int) =
        requestCode == UCrop.REQUEST_CROP && resultCode == UCrop.RESULT_ERROR

    private fun showToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class DropQuestTask(val questID: Int) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg p0: Void?): Boolean {
            try {
                DefaultAPI.questAPI.dropQuest(questID)
                return true
            } catch (ex: APIException) {
                return false
            } catch (ex: NetworkException) {
                return false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                finish()
            } else {
                showToast(getString(R.string.error_failed_dropping_quest))
            }
            changeDroppingQuestProperty(false)
        }
    }
}
