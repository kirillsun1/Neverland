package knk.ee.neverland.quests

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.QuestToSubmit
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.ViewProgressController

class CreateQuestActivity : AppCompatActivity() {
    @BindView(R.id.create_quest_title)
    lateinit var questTitleView: EditText

    @BindView(R.id.create_quest_desc)
    lateinit var questDescView: EditText

    @BindView(R.id.create_quest_save)
    lateinit var saveButton: Button

    @BindView(R.id.submitting_progress)
    lateinit var submittingProgress: ProgressBar

    lateinit var viewProgressController: ViewProgressController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quest)
        ButterKnife.bind(this)

        viewProgressController = ViewProgressController(submittingProgress, saveButton)
    }

    @OnClick(R.id.create_quest_save)
    fun saveButtonAction() {
        if (validateFields()) {
            runSubmitQuestTask(getQuestToSubmit())
        }
    }

    private fun validateFields(): Boolean {
        if (questTitleView.text.isBlank()) {
            questTitleView.error = getString(R.string.error_field_required)
            return false
        }

        if (!isQuestNameCorrect(questTitleView.text.toString())) {
            questTitleView.error = getString(R.string.error_submit_quest_field_length)
            return false
        }

        if (questDescView.text.isBlank()) {
            questDescView.error = getString(R.string.error_field_required)
            return false
        }

        if (!isQuestDescCorrect(questDescView.text.toString())) {
            questDescView.error = getString(R.string.error_submit_quest_field_length)
            return false
        }

        return true
    }

    private fun getQuestToSubmit(): QuestToSubmit {
        return QuestToSubmit(questTitleView.text.toString(),
            questDescView.text.toString(), 0) // TODO: groupID!
    }

    private fun isQuestNameCorrect(name: String) =
        name.length >= Constants.QUEST_NAME_MINIMUM_SYMBOLS
                && name.length <= Constants.QUEST_NAME_MAXIMUM_SYMBOLS

    private fun isQuestDescCorrect(description: String) =
        description.length >= Constants.QUEST_DESC_MINIMUM_SYMBOLS
                && description.length <= Constants.QUEST_DESC_MAXIMUM_SYMBOLS

    private fun runSubmitQuestTask(questToSubmit: QuestToSubmit) {
        var success = false

        APIAsyncTask<Boolean>()
            .doBefore { viewProgressController.showProgress() }
            .request {
                DefaultAPI.questAPI.submitNewQuest(questToSubmit)
                success = true
                true
            }
            .uiErrorView(UIErrorView.Builder().with(this)
                .messageOnAPIFail(R.string.error_submit_failed)
                .create())
            .doAfter {
                if (success) {
                    finish()
                }
                viewProgressController.hideProgress()
            }
            .execute()
    }
}
