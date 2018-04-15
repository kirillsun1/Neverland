package knk.ee.neverland.activities.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.models.Proof
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.views.feedview.FeedElementAdapter

class FeedFragment : Fragment() {
    private lateinit var feedElementAdapter: FeedElementAdapter
    private lateinit var feedListSwipeLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFeedListView()

        feedListSwipeLayout = view.findViewById(R.id.feed_list_swipe_layout)
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
            .handleResult { result -> feedElementAdapter.updateList(result!!) }
            .setContext(view!!.context)
            .showMessages(true)
            .after { feedListSwipeLayout.isRefreshing = false }
            .onAPIFailMessage { R.string.error_getting_proofs_failed }
            .finish()
            .execute()
    }

    private fun initializeFeedListView() {
        val feedListView = view!!.findViewById<ListView>(R.id.feed_listview)
        feedElementAdapter = FeedElementAdapter(context!!)
        feedListView.adapter = feedElementAdapter
        // feedListView.emptyView = // TODO: !!!
    }
}
