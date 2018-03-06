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
import knk.ee.neverland.api.Constants
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.models.Quest

class CreateQuestActivity : AppCompatActivity() {
    private val questToSubmit: Quest = Quest()

    private var questTitleView: EditText? = null
    private var questDescView: EditText? = null
    private var saveButton: Button? = null
    private var submittingProgress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quest)

        questTitleView = (findViewById<EditText>(R.id.quest_title))
        questDescView = (findViewById<EditText>(R.id.quest_desc))
        saveButton = findViewById<Button>(R.id.createquest_save)
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
        questToSubmit.title = questTitleView!!.text.toString()
        questToSubmit.description = questDescView!!.text.toString()
    }

    private fun isQuestNameCorrect(name: String) = name.length >= Constants.QUEST_NAME_MINIMUM_SYMBOLS
            && name.length <= Constants.QUEST_NAME_MAXIMUM_SYMBOLS

    private fun isQuestDescCorrect(description: String) = description.length >= Constants.QUEST_DESC_MINIMUM_SYMBOLS
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
                DefaultAPI.questAPI.submitNewQuest(questToSubmit)
                return Constants.SUCCESS
            } catch (ex: QuestAPIException) {
                return ex.code
            }
        }

        override fun onPostExecute(code: Int?) {
            when (code) {
                Constants.BAD_REQUEST_TO_API -> showToast(getString(R.string.error_invalid_api_request))
                Constants.NETWORK_ERROR -> showToast(getString(R.string.error_network_down))
                Constants.FAILED -> showToast(getString(R.string.error_submit_failed))
                Constants.SUCCESS -> finish()
                else -> showToast(String.format("%s %d", getString(R.string.error_unexpected_code), code))
            }
            blockSaveButton(false)
        }
    }
}
