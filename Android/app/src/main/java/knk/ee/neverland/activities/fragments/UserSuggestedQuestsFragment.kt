package knk.ee.neverland.activities.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Quest
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.views.questview.ProfileSuggestedQuestsTabAdapter

class UserSuggestedQuestsFragment : Fragment() {

    private var profileSuggestedQuestsFragment: ProfileSuggestedQuestsTabAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userID = arguments!!.getInt("userID")

        profileSuggestedQuestsFragment = ProfileSuggestedQuestsTabAdapter(view.context!!, userID)

        view.findViewById<ListView>(R.id.profile_list_view).adapter = profileSuggestedQuestsFragment

        runLoadQuestTask(userID)
    }

    private fun runLoadQuestTask(userID: Int) {
        APIAsyncRequest.Builder<List<Quest>>()
            .request { DefaultAPI.questAPI.getSuggestedByUserQuests(userID) }
            .handleResult { profileSuggestedQuestsFragment!!.addQuests(it!!) }
            .setContext(view!!.context)
            .showMessages(true)
            .finish()
            .execute()
    }
}
