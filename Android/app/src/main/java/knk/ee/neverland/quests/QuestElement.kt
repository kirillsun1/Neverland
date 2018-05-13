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
import org.joda.time.LocalDateTime

@Layout(R.layout.quest_element)
class QuestElement(private val context: Context, private val quest: Quest) {
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

        changeTakeQuestIsEnabledProperty()
    }

    @Click(R.id.take_quest_button)
    fun takeQuest() {
        var success = false
        APIAsyncTask<Boolean>()
            .doBefore { blockTakeQuestButton() }
            .request {
                DefaultAPI.questAPI.takeQuest(quest.id)
                success = true
                true
            }
            .uiErrorView(UIErrorView.Builder()
                .messageWhenAPIError(Constants.QUEST_ALREADY_TAKEN_CODE, R.string.error_quest_is_already_taken)
                .with(context)
                .create())
            .doAfter {
                if (success) {
                    quest.timeTaken = LocalDateTime.now()
                }
                changeTakeQuestIsEnabledProperty()
            }
            .execute()
    }

    private fun blockTakeQuestButton() {
        takeQuest.isEnabled = false
    }

    private fun changeTakeQuestIsEnabledProperty() {
        takeQuest.isEnabled = quest.timeTaken == null // TODO: quest is taken?
    }
}