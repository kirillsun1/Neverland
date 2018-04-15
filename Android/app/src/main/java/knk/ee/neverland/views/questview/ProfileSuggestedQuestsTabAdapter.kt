package knk.ee.neverland.views.questview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Quest
import knk.ee.neverland.utils.APIAsyncRequest

class ProfileSuggestedQuestsTabAdapter(val context: Context, val userID: Int) : BaseAdapter() {

    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val questsList: MutableList<Quest> = mutableListOf()

    fun addQuests(quests: List<Quest>) {
        questsList.addAll(quests)
        notifyDataSetChanged()
    }

    override fun getItem(index: Int): Any = questsList[index]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = questsList.size

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var convertView = view
        val element = getItem(position) as Quest
        val viewHolder: ViewHolder

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.suggested_by_user_quest, viewGroup, false)

            viewHolder = ViewHolder()
            viewHolder.questName = convertView!!.findViewById(R.id.quest_title)
            viewHolder.questDesc = convertView.findViewById(R.id.quest_desc)
            viewHolder.takeQuestButton = convertView.findViewById(R.id.take_quest_button)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.loadFromQuest(context, element)

        return convertView
    }

    private inner class ViewHolder {
        internal lateinit var questName: TextView
        internal lateinit var questDesc: TextView
        internal lateinit var takeQuestButton: Button

        private var takingQuest: Boolean = false

        fun loadFromQuest(context: Context, quest: Quest) {
            questName.text = quest.title
            questDesc.text = quest.description
            takeQuestButton.setOnClickListener {
                runTakingQuestTask(context, quest)
            }

            hideTakeQuestButtonIfQuestIsTaken(quest)
        }

        private fun runTakingQuestTask(context: Context, quest: Quest) {
            if (!takingQuest) {
                APIAsyncRequest.Builder<Boolean>()
                    .before { takingQuest = true }
                    .request {
                        DefaultAPI.questAPI.takeQuest(quest.id)
                        true
                    }
                    .onAPIFailMessage { R.string.error_quest_is_already_taken }
                    .setContext(context)
                    .showMessages(true)
                    .after {
                        takingQuest = false
                    }
                    .finish()
                    .execute()
            }
        }

        private fun hideTakeQuestButtonIfQuestIsTaken(quest: Quest) {
            if (quest.isTaken()) {
                takeQuestButton.visibility = GONE
            }
        }
    }
}
