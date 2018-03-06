package knk.ee.neverland.questview

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import knk.ee.neverland.R
import knk.ee.neverland.api.Constants
import knk.ee.neverland.models.Quest
import java.time.format.DateTimeFormatter
import java.util.*

class MyQuestElementAdapter(val context: Context) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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

        viewHolder.questName!!.text = element.title
        viewHolder.questTimeTaken!!.text = ""

        return convertView
    }

    private class ViewHolder {
        internal var questName: TextView? = null
        internal var questTimeTaken: TextView? = null
    }
}
