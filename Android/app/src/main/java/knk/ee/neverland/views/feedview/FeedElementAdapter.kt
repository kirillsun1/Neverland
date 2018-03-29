package knk.ee.neverland.views.feedview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import knk.ee.neverland.R
import knk.ee.neverland.models.Proof

class FeedElementAdapter(context: Context) : BaseAdapter() {
    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val feedElementList: MutableList<Proof> = mutableListOf()

    override fun getCount(): Int {
        return feedElementList.size
    }

    override fun getItem(i: Int): Any {
        return feedElementList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        var view = convertView
        val element = getItem(i) as Proof
        val viewHolder: ViewHolder

        if (view == null) {
            view = layoutInflater.inflate(R.layout.feed_element, viewGroup, false)

            viewHolder = ViewHolder()
            viewHolder.userAvatar = view!!.findViewById(R.id.feed_user_avatar)
            viewHolder.questName = view.findViewById(R.id.feed_quest_name)
            viewHolder.userName = view.findViewById(R.id.feed_user_name)
            viewHolder.ratingBar = view.findViewById(R.id.feed_rating_bar)
            viewHolder.proofImage = view.findViewById(R.id.feed_proof_image)
            viewHolder.comment = view.findViewById(R.id.feed_proof_comment)

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.userName!!.text = element.sender.userName
        viewHolder.questName!!.text = element.quest.title
        viewHolder.ratingBar!!.progress = 77 // TODO: Rating
        viewHolder.comment!!.visibility = if (element.comment == null
            || element.comment.isBlank()) GONE else VISIBLE;
        viewHolder.comment!!.text = element.comment

        Glide.with(view)
            .load(element.imageLink)
            .transition(DrawableTransitionOptions.withCrossFade(view.resources.getInteger(
                R.integer.feed_fade_animation_duration)))
            .into(viewHolder.proofImage!!)

        return view
    }

    fun updateList(result: List<Proof>) {
        feedElementList.clear()
        feedElementList.addAll(result)
        notifyDataSetChanged()
    }

    private class ViewHolder {
        internal var userName: TextView? = null
        internal var questName: TextView? = null
        internal var userAvatar: ImageView? = null
        internal var ratingBar: ProgressBar? = null
        internal var proofImage: ImageView? = null
        internal var comment: TextView? = null
    }
}
