package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.QuestToSubmit
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.utils.Constants

class CreateQuestActivity : AppCompatActivity() {
    private var questToSubmit: QuestToSubmit? = null

    private var questTitleView: EditText? = null
    private var questDescView: EditText? = null
    private var saveButton: Button? = null
    private var submittingProgress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quest)

        questTitleView = (findViewById(R.id.create_quest_title))
        questDescView = (findViewById(R.id.create_quest_desc))
        saveButton = findViewById(R.id.create_quest_save)
        submittingProgress = findViewById(R.id.submitting_progress)

        blockSaveButton(false)

        saveButton!!.setOnClickListener {
            if (validateFields()) {
                setDataToTheQuestObject()
                blockSaveButton(true)
                SubmitQuestTask().execute()
            }
        }
    }

    private fun validateFields(): Boolean {
        if (questTitleView!!.text.isBlank()) {
            questTitleView!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!isQuestNameCorrect(questTitleView!!.text.toString())) {
            questTitleView!!.error = getString(R.string.error_submit_quest_field_length)
            return false
        }

        if (questDescView!!.text.isBlank()) {
            questDescView!!.error = getString(R.string.error_field_required)
            return false
        }

        if (!isQuestDescCorrect(questDescView!!.text.toString())) {
            questDescView!!.error = getString(R.string.error_submit_quest_field_length)
            return false
        }

        return true
    }

    private fun showToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    private fun setDataToTheQuestObject() {
        questToSubmit = QuestToSubmit(questTitleView!!.text.toString(),
            questDescView!!.text.toString(), 0) // TODO: groupID!
    }

    private fun isQuestNameCorrect(name: String) =
        name.length >= Constants.QUEST_NAME_MINIMUM_SYMBOLS
                && name.length <= Constants.QUEST_NAME_MAXIMUM_SYMBOLS

    private fun isQuestDescCorrect(description: String) =
        description.length >= Constants.QUEST_DESC_MINIMUM_SYMBOLS
                && description.length <= Constants.QUEST_DESC_MAXIMUM_SYMBOLS

    private fun blockSaveButton(block: Boolean) {
        saveButton?.isEnabled = !block
        saveButton?.visibility = if (block) GONE else VISIBLE
        submittingProgress?.visibility = if (!block) GONE else VISIBLE
    }

    @SuppressLint("StaticFieldLeak")
    private inner class SubmitQuestTask : AsyncTask<Void, Void, Int>() {

        override fun doInBackground(vararg p0: Void?): Int {
            try {
                DefaultAPI.questAPI.submitNewQuest(questToSubmit!!)
                return Constants.SUCCESS_CODE
            } catch (ex: APIException) {
                return ex.code
            }
        }

        override fun onPostExecute(code: Int?) {
            when (code) {
                Constants.BAD_REQUEST_TO_API_CODE -> showToast(getString(R.string.error_invalid_api_request))
                Constants.NETWORK_ERROR_CODE -> showToast(getString(R.string.error_network_down))
                Constants.FAIL_CODE -> showToast(getString(R.string.error_submit_failed))
                Constants.SUCCESS_CODE -> finish()
                else -> showToast(String.format("%s %d", getString(R.string.error_unexpected_code), code))
            }
            blockSaveButton(false)
        }
    }
}
