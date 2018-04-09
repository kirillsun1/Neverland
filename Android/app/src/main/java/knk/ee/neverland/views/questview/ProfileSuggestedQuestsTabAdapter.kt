package knk.ee.neverland.views.questview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import knk.ee.neverland.R
import knk.ee.neverland.models.Quest

class ProfileSuggestedQuestsTabAdapter(val context: Context) : BaseAdapter() {

    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val questsList: MutableList<Quest> = mutableListOf()

    fun addQuests(quests: List<Quest>) {
        questsList.addAll(quests)
        notifyDataSetChanged()
    }

    override fun getItem(p0: Int): Any = questsList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

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

        viewHolder.questName!!.text = element.title
        viewHolder.questDesc!!.text = element.description
        viewHolder.takeQuestButton!!.setOnClickListener {
            // DefaultAPI.questAPI.takeQuest(element.id)
        }

        return convertView
    }

    private class ViewHolder {
        internal var questName: TextView? = null
        internal var questDesc: TextView? = null
        internal var takeQuestButton: Button? = null
    }
}
