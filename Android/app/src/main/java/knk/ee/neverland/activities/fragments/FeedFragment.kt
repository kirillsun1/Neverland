package knk.ee.neverland.activities.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.models.Proof
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.views.feedview.FeedElementAdapter

class FeedFragment : Fragment() {
    var feedElementAdapter: FeedElementAdapter? = null
    var feedListSwipeLayout: SwipeRefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFeedListView()

        feedListSwipeLayout = view.findViewById(R.id.feed_list_swipe_layout)
        feedListSwipeLayout!!.setOnRefreshListener({
            feedListSwipeLayout!!.isRefreshing = true
            GetProofsTask().execute()
        })

        GetProofsTask().execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    private fun initializeFeedListView() {
        val feedListView = view!!.findViewById<ListView>(R.id.feed_listview)
        feedElementAdapter = FeedElementAdapter(context!!)
        feedListView.adapter = feedElementAdapter
        // feedListView.emptyView = // TODO: !!!
    }

    private fun showMessage(message: String) {
        if (isAdded) {
            Toast.makeText(view!!.context, message, Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetProofsTask : AsyncTask<Void, Void, List<Proof>?>() {

        private var networkErrorCode: Int? = null
        private var apiErrorCode: Int? = null

        override fun doInBackground(vararg p0: Void?): List<Proof>? {
            try {
                return DefaultAPI.proofAPI.getProofs(FeedScope.WORLD)
            } catch (ex: NetworkException) {
                networkErrorCode = ex.code
                return null
            } catch (ex: APIException) {
                apiErrorCode = ex.code
                return null
            }
        }

        override fun onPostExecute(result: List<Proof>?) {
            if (result != null) {
                feedElementAdapter!!.updateList(result)
            } else {
                if (networkErrorCode != null) {
                    failWithNetworkException(networkErrorCode!!)
                }

                if (apiErrorCode != null) {
                    failWithAPIException(apiErrorCode!!)
                }
            }

            feedListSwipeLayout!!.isRefreshing = false
        }

        private fun failWithAPIException(code: Int) {
            when (code) {
                Constants.FAIL_CODE -> showMessage(getString(R.string.error_getting_proofs_failed))
                else -> showMessage(String.format(getString(R.string.error_unexpected_code), code))
            }
        }

        private fun failWithNetworkException(code: Int) {
            when (code) {
                Constants.BAD_REQUEST_TO_API_CODE -> showMessage(getString(R.string.error_invalid_api_request))
                Constants.NETWORK_ERROR_CODE -> showMessage(getString(R.string.error_network_down))
                Constants.NETWORK_TIMEOUT -> showMessage(getString(R.string.error_slow_network))
                else -> showMessage(String.format(getString(R.string.error_unexpected_code), code))
            }
        }
    }
}
