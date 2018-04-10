package knk.ee.neverland.views.feedview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView
import knk.ee.neverland.R
import knk.ee.neverland.activities.ProfileActivity
import knk.ee.neverland.models.Proof
import knk.ee.neverland.utils.Constants

class FeedElementAdapter(val context: Context) : BaseAdapter() {
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

    override fun getViewTypeCount(): Int = Constants.ELEMENT_NUMBER_TO_START_RECYCLING_FROM

    override fun getItemViewType(position: Int): Int = position

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
            viewHolder.buttonFor = view.findViewById(R.id.feed_rating_plus)
            viewHolder.buttonAgainst = view.findViewById(R.id.feed_rating_minus)

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.loadFromProof(context, element)

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
        internal var userAvatar: CircularImageView? = null
        internal var ratingBar: ProgressBar? = null
        internal var proofImage: ImageView? = null
        internal var comment: TextView? = null
        internal var buttonFor: Button? = null
        internal var buttonAgainst: Button? = null

        fun loadFromProof(context: Context, proof: Proof) {
            userName!!.text = proof.sender.userName
            questName!!.text = proof.quest.title
            ratingBar!!.max = proof.votesFor + proof.votesAgainst
            ratingBar!!.progress = proof.votesFor
            comment!!.visibility = shouldCommentBeVisible(proof, proof.comment)
            comment!!.text = proof.comment

            loadAvatar(context, proof)
            loadProofImage(context, proof)

            setActionsToOpenUserActivity(context, proof)
        }

        private fun shouldCommentBeVisible(proof: Proof, comment: String?) =
            if (proof.comment == null || comment!!.isBlank()) GONE else VISIBLE

        private fun loadAvatar(context: Context, proof: Proof) {
            Glide.with(context)
                .load(proof.sender.avatarLink)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade(context.resources.getInteger(
                    R.integer.feed_fade_animation_duration)))
                .into(userAvatar!!)
        }

        private fun loadProofImage(context: Context, proof: Proof) {
            Glide.with(context)
                .load(proof.imageLink)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade(context.resources.getInteger(
                    R.integer.feed_fade_animation_duration)))
                .into(proofImage!!)
        }

        private fun setActionsToOpenUserActivity(context: Context, proof: Proof) {
            val openUserProfileListener: View.OnClickListener = View.OnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("userID", proof.sender.id)
                context.startActivity(intent)
            }

            userName!!.setOnClickListener(openUserProfileListener)
            userAvatar!!.setOnClickListener(openUserProfileListener)
        }
    }
}
