package knk.ee.neverland.views.questview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import knk.ee.neverland.R
import knk.ee.neverland.models.Quest
import java.util.*

class TakenQuestsListAdapter(val context: Context) : BaseAdapter() {
    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var questsList: List<Quest> = ArrayList()

    override fun getCount(): Int {
        return questsList.size
    }

    override fun getItem(i: Int): Any {
        return questsList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var convertView = view
        val element = getItem(i) as Quest
        val viewHolder: ViewHolder

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.taken_quest_element, viewGroup, false)

            viewHolder = ViewHolder()
            viewHolder.questName = convertView!!.findViewById(R.id.my_quests_quest_name)
            viewHolder.questTimeTaken = convertView.findViewById(R.id.my_quests_time_taken)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.loadFromQuest(context, element)

        return convertView
    }

    private class ViewHolder {
        internal lateinit var questName: TextView
        internal lateinit var questTimeTaken: TextView

        fun loadFromQuest(context: Context, quest: Quest) {
            questName.text = quest.title
            questTimeTaken.text = context.getString(R.string.quest_taken_data)
                .format(quest.timeTaken.toString())
        }
    }
}
