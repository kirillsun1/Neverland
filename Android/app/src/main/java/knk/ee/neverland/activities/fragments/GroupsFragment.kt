package knk.ee.neverland.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import knk.ee.neverland.R
import knk.ee.neverland.activities.CreateGroupActivity
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Group
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.views.GroupsListAdapter

class GroupsFragment : Fragment() {

    private var groupsListAdapter: GroupsListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.groups_fragment_title)
        toolbar.inflateMenu(R.menu.groups_menu)
        toolbar.menu.findItem(R.id.create_group).setOnMenuItemClickListener {
            startActivity(Intent(context, CreateGroupActivity::class.java))
            true
        }

        groupsListAdapter = GroupsListAdapter(view.context!!)
        view.findViewById<ListView>(R.id.groups_list).adapter = groupsListAdapter

        runGetMyGroupsTask()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onResume() {
        super.onResume()

        runGetMyGroupsTask()
    }

    private fun runGetMyGroupsTask() {
        APIAsyncRequest.Builder<List<Group>>()
            .request { DefaultAPI.groupAPI.getMyGroups() }
            .handleResult {
                groupsListAdapter!!.refreshGroups(it!!)
            }
            .setContext(view!!.context)
            .showMessages(true)
            .finish()
            .execute()
    }
}
