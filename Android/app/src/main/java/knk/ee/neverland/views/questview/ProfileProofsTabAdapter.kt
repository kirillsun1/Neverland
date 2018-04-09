package knk.ee.neverland.views.questview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import knk.ee.neverland.R
import knk.ee.neverland.models.Proof

class ProfileProofsTabAdapter(val context: Context) : BaseAdapter() {

    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val proofsList: MutableList<Proof> = mutableListOf()

    fun addProofs(proofs: List<Proof>) {
        proofsList.addAll(proofs)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any = proofsList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = proofsList.size

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var convertView = view
        val element = getItem(position) as Proof
        val viewHolder: ViewHolder

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.simple_proof, viewGroup, false)

            viewHolder = ViewHolder()
            viewHolder.questName = convertView!!.findViewById(R.id.quest_title)
            viewHolder.proofImage = convertView.findViewById(R.id.proof_image)
            viewHolder.rating = convertView.findViewById(R.id.proof_rating)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.questName!!.text = element.quest.title
        viewHolder.rating!!.progress = 77 // TODO: rating!

        Glide.with(context)
            .load(element.imageLink)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .transition(DrawableTransitionOptions.withCrossFade(context.resources.getInteger(
                R.integer.feed_fade_animation_duration)))
            .into(viewHolder.proofImage!!)

        return convertView
    }

    private class ViewHolder {
        internal var questName: TextView? = null
        internal var proofImage: ImageView? = null
        internal var rating: ProgressBar? = null
    }
}
