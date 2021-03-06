package knk.ee.neverland.feed

import android.app.AlertDialog
import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.mindorks.placeholderview.annotations.Click
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.varunest.sparkbutton.SparkButton
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Proof
import knk.ee.neverland.models.Rating
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView

@Layout(R.layout.user_proof)
class UserProofCard(private val context: Context, private val proof: Proof) {

    @View(R.id.quest_name)
    lateinit var questName: TextView

    @View(R.id.proof_image)
    lateinit var proofImage: ImageView

    @View(R.id.proof_comment)
    lateinit var proofComment: TextView

    @View(R.id.rating_bar)
    lateinit var ratingBar: ProgressBar

    @View(R.id.vote_for)
    lateinit var voteForButton: SparkButton

    @View(R.id.vote_against)
    lateinit var voteAgainstButton: SparkButton

    @View(R.id.votes_for)
    lateinit var votesFor: TextView

    @View(R.id.votes_against)
    lateinit var votesAgainst: TextView

    @Resolve
    fun onResolve() {
        questName.text = proof.quest.title

        proofComment.visibility = shouldCommentBeVisible()
        proofComment.text = proof.comment

        updateRating(proof.rating, playVoteButtonAnimations = false)

        loadProofImage()
    }

    @Click(R.id.vote_for)
    fun voteForProof() {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.vote_confirmation))
            .setMessage(context.getString(R.string.vote_for_confirmation_text))
            .setNegativeButton(context.getString(R.string.no), { dialogInterface, _ ->
                dialogInterface.cancel()
            })
            .setPositiveButton(context.getString(R.string.yes), { dialogInterface, _ ->
                runChangeVoteTask { DefaultAPI.voteAPI.voteFor(proof.id) }
                dialogInterface.dismiss()
            })
            .create()
            .show()
    }

    @Click(R.id.vote_against)
    fun voteAgainstProof() {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.vote_confirmation))
            .setMessage(context.getString(R.string.vote_against_confirmation_text))
            .setNegativeButton(context.getString(R.string.no), { dialogInterface, _ ->
                dialogInterface.cancel()
            })
            .setPositiveButton(context.getString(R.string.yes), { dialogInterface, _ ->
                runChangeVoteTask { DefaultAPI.voteAPI.voteAgainst(proof.id) }
                dialogInterface.dismiss()
            })
            .create()
            .show()
    }

    private fun runChangeVoteTask(changeVoteMethod: () -> Rating) {
        APIAsyncTask<Rating>()
            .doBefore { blockRatingButtons() }
            .request(changeVoteMethod)
            .handleResult {
                updateRating(it)
                proof.rating = it
            }
            .uiErrorView(UIErrorView.Builder()
                .messageOnAPIFail(R.string.failed_to_update_rating)
                .with(context)
                .create())
            .execute()
    }

    private fun shouldCommentBeVisible() =
        if (proof.comment == null || proof.comment.isBlank()) android.view.View.GONE else android.view.View.VISIBLE

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

    private fun updateRating(rating: Rating, playVoteButtonAnimations: Boolean = true) {
        ratingBar.progress = rating.intValue()

        votesFor.text = rating.votesFor.toString()
        votesAgainst.text = rating.votesAgainst.toString()

        voteForButton.isEnabled = !rating.iVoted()
        voteAgainstButton.isEnabled = !rating.iVoted()

        voteForButton.isChecked = rating.myVoteIsFor()
        voteAgainstButton.isChecked = rating.myVoteIsAgainst()

        if (rating.iVoted() && playVoteButtonAnimations) {
            if (rating.myVoteIsFor()) {
                voteForButton.playAnimation()
            }

            if (rating.myVoteIsAgainst()) {
                voteAgainstButton.playAnimation()
            }
        }
    }
}