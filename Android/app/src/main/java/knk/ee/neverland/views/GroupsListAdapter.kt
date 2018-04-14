package knk.ee.neverland.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import knk.ee.neverland.R
import knk.ee.neverland.models.Group

class GroupsListAdapter(val context: Context) : BaseAdapter() {
    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var groups = ArrayList<Group>()

    override fun getCount(): Int = groups.size

    override fun getItem(index: Int): Any = groups[index]

    override fun getItemId(position: Int): Long = position.toLong()

    fun refreshGroups(groups: List<Group>) {
        this.groups.clear()
        this.groups.addAll(groups)
        notifyDataSetChanged()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var convertView = view
        val element = getItem(i) as Group
        val viewHolder: ViewHolder

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.group_element, viewGroup, false)

            viewHolder = ViewHolder()
            viewHolder.groupName = convertView!!.findViewById(R.id.group_name)
            viewHolder.groupCreator = convertView.findViewById(R.id.group_creator)
            viewHolder.groupAvatar = convertView.findViewById(R.id.group_avatar)


            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.loadFromGroup(context, element)

        return convertView
    }

    private class ViewHolder {
        internal var groupName: TextView? = null
        internal var groupCreator: TextView? = null
        internal var groupAvatar: ImageView? = null

        fun loadFromGroup(context: Context, group: Group) {
            groupName!!.text = group.name
            groupCreator!!.text = "Created by ${group.admin}"
            loadAvatar(context, group)
        }

        private fun loadAvatar(context: Context, group: Group) {
            Glide.with(context)
                .load(group.admin.avatarLink)
                .apply(RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.logo))
                .transition(DrawableTransitionOptions.withCrossFade(context.resources.getInteger(
                    R.integer.feed_fade_animation_duration)))
                .into(groupAvatar!!)
        }
    }
}
