package knk.ee.neverland.activities.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Proof
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.views.questview.ProfileProofsTabAdapter

class UserProofsFragment : Fragment() {

    private var profileProofsTabAdapter: ProfileProofsTabAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileProofsTabAdapter = ProfileProofsTabAdapter(view.context!!)

        view.findViewById<ListView>(R.id.profile_list_view).adapter = profileProofsTabAdapter

        runGetProofsTask(arguments!!.getInt("userID"))
    }

    private fun runGetProofsTask(userID: Int) {
        APIAsyncRequest.Builder<List<Proof>>()
            .request { DefaultAPI.proofAPI.getProofsByUserID(userID) }
            .handleResult { profileProofsTabAdapter!!.addProofs(it!!) }
            .setContext(view!!.context)
            .showMessages(true)
            .finish()
            .execute()
    }
}
