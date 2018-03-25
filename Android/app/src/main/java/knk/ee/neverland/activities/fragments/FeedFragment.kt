package knk.ee.neverland.activities.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFeedListView()
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
    }

    private fun showMessage(message: String) {
        Toast.makeText(view!!.context, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetProofsTask : AsyncTask<Void, Void, List<Proof>?>() {

        override fun doInBackground(vararg p0: Void?): List<Proof>? {
            try {
                return DefaultAPI.proofAPI.getProofs(FeedScope.WORLD)
            } catch (ex: NetworkException) {
                failWithNetworkException(ex.code)
                return null
            } catch (ex: APIException) {
                failWithAPIException(ex.code)
                return null
            }
        }

        override fun onPostExecute(result: List<Proof>?) {
            if (result != null) {
                feedElementAdapter!!.addProofs(result)
            }
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
                Constants.FAIL_CODE -> showMessage(getString(R.string.error_incorrect_field))
                else -> showMessage(String.format(getString(R.string.error_unexpected_code), code))
            }
        }
    }
}
