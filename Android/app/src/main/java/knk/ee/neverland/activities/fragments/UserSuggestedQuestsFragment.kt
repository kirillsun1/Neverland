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
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.models.Quest
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.views.questview.ProfileSuggestedQuestsTabAdapter

class UserSuggestedQuestsFragment : Fragment() {

    private var profileSuggestedQuestsFragment: ProfileSuggestedQuestsTabAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileSuggestedQuestsFragment = ProfileSuggestedQuestsTabAdapter(view.context!!)

        view.findViewById<ListView>(R.id.profile_list_view).adapter = profileSuggestedQuestsFragment

        LoadQuestsTask().execute(arguments!!.getInt("userID"))
    }

    private fun showMessage(message: String) {
        if (isAdded) {
            Toast.makeText(view!!.context, message, Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LoadQuestsTask : AsyncTask<Int, Void, List<Quest>?>() {

        private var networkErrorCode: Int? = null
        private var apiErrorCode: Int? = null

        override fun doInBackground(vararg p0: Int?): List<Quest>? {
            return try {
                DefaultAPI.questAPI.getSuggestedByUserQuests(p0[0]!!)
            } catch (ex: NetworkException) {
                networkErrorCode = ex.code
                null
            } catch (ex: APIException) {
                apiErrorCode = ex.code
                null
            }
        }

        override fun onPostExecute(result: List<Quest>?) {
            if (result != null) {
                profileSuggestedQuestsFragment!!.addQuests(result)
            } else {
                if (networkErrorCode != null) {
                    failWithNetworkException(networkErrorCode!!)
                }

                if (apiErrorCode != null) {
                    failWithAPIException(apiErrorCode!!)
                }
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
                Constants.NETWORK_TIMEOUT -> showMessage(getString(R.string.error_slow_network))
                else -> showMessage(String.format(getString(R.string.error_unexpected_code), code))
            }
        }
    }
}
