package knk.ee.neverland.groups

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.feed.ProofCard
import knk.ee.neverland.models.Group
import knk.ee.neverland.models.Proof
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.Utils
import kotlinx.android.synthetic.main.activity_group_details.toolbar

class GroupDetailsActivity : AppCompatActivity() {

    lateinit var group: Group

    @BindView(R.id.group_avatar)
    lateinit var groupAvatar: CircularImageView

    @BindView(R.id.group_name)
    lateinit var groupName: TextView

    @BindView(R.id.group_admin)
    lateinit var groupAdminName: TextView

    @BindView(R.id.group_admin_avatar)
    lateinit var groupAdminAvatar: CircularImageView

    @BindView(R.id.group_proofs)
    lateinit var groupProofsList: PlaceHolderView

    @BindView(R.id.group_subscribers)
    lateinit var subscribers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)
        setSupportActionBar(toolbar)
        ButterKnife.bind(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        groupProofsList.isNestedScrollingEnabled = true

        group = intent.extras.get("group") as Group
        setGroupData()
        loadGroupProofs()
    }

    private fun setGroupData() {
        title = group.name
        groupName.text = group.name
        groupAdminName.text = "${group.admin} (${group.admin.userName})"
        subscribers.text = "${Utils.compactHugeNumber(group.quantity)} subscriber(s)"

        loadGroupAvatar()
        loadAdminAvatar()
    }

    private fun loadGroupAvatar() {
        Glide.with(applicationContext)
            .load(group.avatarLink)
            .apply(RequestOptions()
                .skipMemoryCache(true)
                .placeholder(R.drawable.no_avatar_group))
            .into(groupAvatar)
    }

    private fun loadAdminAvatar() {
        Glide.with(applicationContext)
            .load(group.admin.avatarLink)
            .apply(RequestOptions()
                .skipMemoryCache(true)
                .placeholder(R.drawable.no_avatar))
            .into(groupAdminAvatar)
    }

    private fun loadGroupProofs() {
        APIAsyncTask<List<Proof>>()
            .request { DefaultAPI.proofAPI.getProofs(FeedScope.FOLLOWING) }
            .handleResult {
                it.forEach { groupProofsList.addView(ProofCard(this, it)) }
            }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .execute()
    }
}
