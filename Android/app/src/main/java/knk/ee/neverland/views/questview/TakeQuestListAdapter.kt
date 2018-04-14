package knk.ee.neverland.views.questview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView

import knk.ee.neverland.R
import knk.ee.neverland.models.Quest
import knk.ee.neverland.utils.Constants

class TakeQuestListAdapter(val context: Context) : BaseAdapter(), Filterable {
    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        if (this.filter == null) {
            this.filter = QuestsFilter()
        }

        return this.filter!!
    }

    override fun getViewTypeCount(): Int = Constants.ELEMENT_NUMBER_TO_START_RECYCLING_FROM

    override fun getItemViewType(position: Int): Int = position

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
            viewHolder.userAvatar = convertView.findViewById(R.id.user_avatar)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.loadFromQuest(context, element)

        return convertView!!
    }

    private class ViewHolder {
        internal var questName: TextView? = null
        internal var questDesc: TextView? = null
        internal var questAuthor: TextView? = null
        internal var userAvatar: CircularImageView? = null

        fun loadFromQuest(context: Context, quest: Quest) {
            questName!!.text = quest.title
            questDesc!!.text = quest.description
            questAuthor!!.text = context.getString(R.string.quest_author,
                quest.author.firstName, quest.author.secondName)

            loadAvatar(context, quest)
        }

        private fun loadAvatar(context: Context, quest: Quest) {
            Glide.with(context)
                .load(quest.author.avatarLink)
                .apply(RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.logo))
                .transition(DrawableTransitionOptions.withCrossFade(context.resources.getInteger(
                    R.integer.feed_fade_animation_duration)))
                .into(userAvatar!!)
        }
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
