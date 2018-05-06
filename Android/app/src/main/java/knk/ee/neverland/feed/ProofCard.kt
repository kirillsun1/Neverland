package knk.ee.neverland.feed

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView
import com.mindorks.placeholderview.annotations.Click
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Proof
import knk.ee.neverland.models.Rating
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.profile.ProfileActivity
import knk.ee.neverland.utils.UIErrorView

@Layout(R.layout.proof_card)
class ProofCard(private val context: Context, private val proof: Proof) {
    @View(R.id.user_avatar)
    lateinit var userAvatar: CircularImageView

    @View(R.id.user_name)
    lateinit var userName: TextView

    @View(R.id.quest_name)
    lateinit var questName: TextView

    @View(R.id.proof_image)
    lateinit var proofImage: ImageView

    @View(R.id.proof_comment)
    lateinit var proofComment: TextView

    @View(R.id.rating_bar)
    lateinit var ratingBar: ProgressBar

    @View(R.id.feed_rating_plus)
    lateinit var voteForButton: Button

    @View(R.id.feed_rating_minus)
    lateinit var voteAgainstButton: Button

    @Resolve
    fun onResolve() {
        userName.text = proof.sender.toString()
        questName.text = proof.quest.title

        proofComment.visibility = shouldCommentBeVisible()
        proofComment.text = proof.comment

        updateRating(proof.rating)

        loadAvatar()
        loadProofImage()
    }

    @Click(R.id.user_avatar)
    fun onUserAvatarClick() {
        openUserProfile()
    }

    @Click(R.id.user_name)
    fun onUserNameClick() {
        openUserProfile()
    }

    @Click(R.id.feed_rating_plus)
    fun voteForProof() {
        APIAsyncTask<Rating>()
            .doBefore { blockRatingButtons() }
            .request { DefaultAPI.voteAPI.voteFor(proof.id) }
            .handleResult { updateRating(it) }
            .uiErrorView(UIErrorView.Builder()
                .messageOnAPIFail(R.string.failed_to_update_rating)
                .with(context)
                .create())
            .doAfter { blockRatingButtons() }
            .execute()
    }

    @Click(R.id.feed_rating_minus)
    fun voteAgainstProof() {
        APIAsyncTask<Rating>()
            .doBefore { blockRatingButtons() }
            .request { DefaultAPI.voteAPI.voteAgainst(proof.id) }
            .handleResult { updateRating(it) }
            .uiErrorView(UIErrorView.Builder()
                .messageOnAPIFail(R.string.failed_to_update_rating)
                .with(context)
                .create())
            .doAfter { blockRatingButtons() }
            .execute()
    }

    private fun openUserProfile() {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra("userID", proof.sender.id)
        context.startActivity(intent)
    }

    private fun shouldCommentBeVisible() =
        if (proof.comment == null || proof.comment.isBlank()) android.view.View.GONE else android.view.View.VISIBLE

    private fun loadAvatar() {
        Glide.with(context)
            .load(proof.sender.avatarLink)
            .apply(RequestOptions()
                .placeholder(R.drawable.no_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(userAvatar)
    }

    private fun loadProofImage() {
        Glide.with(context)
            .load(proof.imageLink)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .transition(DrawableTransitionOptions.withCrossFade(context.resources.getInteger(
                R.integer.feed_fade_animation_duration)))
            .thumbnail(0.1f)
            .into(proofImage)
    }

    private fun blockRatingButtons() {
        voteForButton.isEnabled = false
        voteAgainstButton.isEnabled = false
    }

    private fun updateRating(rating: Rating) {
        ratingBar.progress = rating.intValue()

        voteForButton.isEnabled = rating.myVoteIsAgainst()
        voteAgainstButton.isEnabled = rating.myVoteIsFor()
    }
}