package knk.ee.neverland.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.models.Proof
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.utils.Constants

class FeedFragment : Fragment() {
    @BindView(R.id.feed_list)
    lateinit var feedList: PlaceHolderView

    @BindView(R.id.feed_list_swipe_layout)
    lateinit var feedListSwipeLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        feedList.builder
            .setHasFixedSize(false)
            .setItemViewCacheSize(Constants.ELEMENT_NUMBER_TO_START_RECYCLING_FROM)

        feedListSwipeLayout.setOnRefreshListener({
            runGetProofsTask(true)
        })

        view.findViewById<Toolbar>(R.id.toolbar).title = getString(R.string.feed_fragment_title)

        runGetProofsTask(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    private fun runGetProofsTask(updating: Boolean) {
        APIAsyncRequest.Builder<List<Proof>>()
            .before { feedListSwipeLayout.isRefreshing = true && updating }
            .request { DefaultAPI.proofAPI.getProofs(FeedScope.WORLD) }
            .handleResult { result -> result!!.forEach { feedList.addView(FeedElement(view!!.context, it)) } }
            .setContext(view!!.context)
            .showMessages(true)
            .after { feedListSwipeLayout.isRefreshing = false }
            .onAPIFailMessage { R.string.error_getting_proofs_failed }
            .finish()
            .execute()
    }
}
