package knk.ee.neverland.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.models.Proof
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.utils.ViewProgressController

class FeedFragment : Fragment() {
    @BindView(R.id.feed_list)
    lateinit var feedList: PlaceHolderView

    @BindView(R.id.feed_list_swipe_layout)
    lateinit var feedListSwipeLayout: SwipeRefreshLayout

    @BindView(R.id.feed_loading_progress)
    lateinit var feedLoadingProgess: ProgressBar

    private var getProofsTask: APIAsyncTask<List<Proof>>? = null

    private var selectedFeedScope: FeedScope = FeedScope.WORLD

    private lateinit var viewProgressController: ViewProgressController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        feedList.builder
            .setHasFixedSize(false)
            .setItemViewCacheSize(Constants.ELEMENT_NUMBER_TO_START_RECYCLING_FROM)

        feedListSwipeLayout.setOnRefreshListener({
            runGetProofsTask(true)
        })

        viewProgressController = ViewProgressController(feedLoadingProgess, feedList)
        viewProgressController.showProgress()

        view.findViewById<Spinner>(R.id.feed_spinner)
            .onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
                selectedFeedScope = when (pos) {
                    0 -> FeedScope.WORLD
                    1 -> FeedScope.GROUPS
                    2 -> FeedScope.FOLLOWING
                    else -> FeedScope.WORLD
                }
                runGetProofsTask(false, showProgress = true)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getProofsTask?.stopIfRunning()
    }

    private fun runGetProofsTask(updating: Boolean, showProgress: Boolean = false) {
        if (getProofsTask == null) {
            getProofsTask = APIAsyncTask<List<Proof>>()
                .doBefore {
                    feedListSwipeLayout.isRefreshing = true && updating
                    if (showProgress) {
                        viewProgressController.showProgress()
                    }
                }
                .request { DefaultAPI.proofAPI.getProofs(selectedFeedScope) }
                .handleResult { result ->
                    feedList.removeAllViews()
                    result.forEach {
                        feedList.addView(ProofCard(view!!.context, it))
                    }
                }
                .uiErrorView(UIErrorView.Builder().with(view!!.context)
                    .messageOnAPIFail(R.string.error_getting_proofs_failed)
                    .create())
                .doAfter {
                    feedListSwipeLayout.isRefreshing = false
                    if (showProgress) {
                        viewProgressController.hideProgress()
                    }
                }
        }

        getProofsTask!!.execute()
    }
}
