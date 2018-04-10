package knk.ee.neverland.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.QuestToSubmit
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.utils.Constants

class CreateQuestActivity : AppCompatActivity() {
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
                runSubmitQuestTask(getQuestToSubmit())
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

    private fun getQuestToSubmit(): QuestToSubmit {
        return QuestToSubmit(questTitleView!!.text.toString(),
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

    private fun runSubmitQuestTask(questToSubmit: QuestToSubmit) {
        var success = false

        APIAsyncRequest.Builder<Boolean>()
            .before { blockSaveButton(true) }
            .request {
                DefaultAPI.questAPI.submitNewQuest(questToSubmit)
                success = true
                true
            }
            .onAPIFailMessage { R.string.error_submit_failed }
            .setContext(this)
            .showMessages(true)
            .after {
                blockSaveButton(false)
                if (success) {
                    finish()
                }
            }
            .finish()
            .execute()
    }
}
