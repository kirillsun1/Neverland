package knk.ee.neverland.quests

import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.mindorks.placeholderview.annotations.Click
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Quest
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.UIErrorView

@Layout(R.layout.suggested_by_user_quest)
class SuggestedQuestElement(private val context: Context, private val quest: Quest) {
    @View(R.id.quest_title)
    lateinit var questTitle: TextView

    @View(R.id.quest_desc)
    lateinit var questDescription: TextView

    @View(R.id.take_quest_button)
    lateinit var takeQuest: Button

    @Resolve
    fun onResolve() {
        questTitle.text = quest.title
        questDescription.text = quest.description
    }

    @Click(R.id.take_quest_button)
    fun takeQuest() {
        APIAsyncTask<Boolean>()
            .toPool("takeQuest", "profileTasks")
            .doBefore { blockTakeQuestButton() }
            .request {
                DefaultAPI.questAPI.takeQuest(quest.id)
                true
            }
            .uiErrorView(UIErrorView.Builder()
                .messageWhenAPIError(Constants.QUEST_ALREADY_TAKEN_CODE, R.string.error_quest_is_already_taken)
                .with(context)
                .create())
            .doAfter { unblockTakeQuestButton() }
            .execute()
    }

    private fun blockTakeQuestButton() {
        takeQuest.isEnabled = false
    }

    private fun unblockTakeQuestButton() {
        takeQuest.isEnabled = true
    }
}