package knk.ee.neverland.questview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

import knk.ee.neverland.R
import knk.ee.neverland.models.Quest

class QuestElementAdapter(val context: Context) : BaseAdapter(), Filterable {
    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val questsList: MutableList<Quest> = mutableListOf()
    private var filteredList: MutableList<Quest> = mutableListOf()

    private var filter: QuestsFilter? = null

    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getItem(i: Int): Quest {
        return filteredList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    fun removeQuest(i: Int) {
        val quest = getItem(i)
        questsList.remove(quest)
        filteredList.remove(quest)
        notifyDataSetChanged()
    }

    fun addQuests(otherList: List<Quest>) {
        questsList.clear()
        questsList.addAll(otherList)
        filteredList.addAll(otherList)
    }

    override fun getFilter(): Filter {
        if (this.filter == null) {
            this.filter = QuestsFilter()
        }

        return this.filter!!
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var convertView = view
        val element = getItem(i)
        val viewHolder: ViewHolder

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.quest_element, viewGroup, false)

            viewHolder = ViewHolder()
            viewHolder.questName = convertView.findViewById(R.id.quests_quest_name)
            viewHolder.questDesc = convertView.findViewById(R.id.quests_quest_desc)
            viewHolder.questAuthor = convertView.findViewById(R.id.quests_author)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.questName!!.text = element.title
        viewHolder.questDesc!!.text = element.description
        viewHolder.questAuthor!!.text = context.getString(R.string.quest_author,
                element.creator.firstName, element.creator.secondName)

        return convertView!!
    }

    private class ViewHolder {
        internal var questName: TextView? = null
        internal var questDesc: TextView? = null
        internal var questAuthor: TextView? = null
    }

    private inner class QuestsFilter : Filter() {
        override fun performFiltering(searchText: CharSequence?): FilterResults {
            val newList = questsList.filter {
                it.title.toLowerCase().contains(searchText.toString().toLowerCase())
                        || it.description.toLowerCase().contains(searchText.toString().toLowerCase())
            }

            val filterResults = FilterResults()

            if (newList.isNotEmpty()) {
                filterResults.count = newList.count()
                filterResults.values = newList
            } else {
                filterResults.count = questsList.size
                filterResults.values = questsList
            }

            return filterResults
        }

        override fun publishResults(newText: CharSequence?, result: FilterResults?) {
            filteredList = result!!.values as MutableList<Quest>
            notifyDataSetChanged()
        }
    }
}
