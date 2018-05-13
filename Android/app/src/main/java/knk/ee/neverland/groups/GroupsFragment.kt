package knk.ee.neverland.groups

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Group
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView

class GroupsFragment : Fragment() {

    @BindView(R.id.groups_list)
    lateinit var groupsList: PlaceHolderView

    private var getMyGroupsTask: APIAsyncTask<List<Group>>? = null
    private var createGroupTask: APIAsyncTask<Boolean>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.groups_fragment_title)
        toolbar.inflateMenu(R.menu.groups_menu)
        toolbar.menu.findItem(R.id.create_group).setOnMenuItemClickListener {
            openCreateGroupDialog()
            true
        }
        toolbar.menu.findItem(R.id.find_group).setOnMenuItemClickListener {
            openFindGroupActivity()
            true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getMyGroupsTask?.stopIfRunning()
        createGroupTask?.stopIfRunning()
    }

    override fun onResume() {
        super.onResume()
        runGetMyGroupsTask()
    }

    private fun openCreateGroupDialog() {
        val createGroupDialog = CreateGroupDialog(view!!.context)

        createGroupDialog.onGroupCreated = { groupName ->
            runCreateGroupTask(groupName)
        }

        createGroupDialog.show()
    }

    private fun openFindGroupActivity() {
        val intent = Intent(view!!.context.applicationContext, FindGroupActivity::class.java)
        startActivity(intent)
    }

    private fun runGetMyGroupsTask() {
        getMyGroupsTask = APIAsyncTask<List<Group>>()
            .request { DefaultAPI.groupAPI.getMyGroups() }
            .uiErrorView(UIErrorView.Builder()
                .with(view!!.context)
                .create())
            .handleResult {
                groupsList.removeAllViews()
                it.forEach { groupsList.addView(GroupElement(view!!.context, it)) }
            }
        getMyGroupsTask!!.execute()
    }

    private fun runCreateGroupTask(groupName: String) {
        createGroupTask = APIAsyncTask<Boolean>()
            .request {
                DefaultAPI.groupAPI.createGroup(groupName)
                true
            }
            .uiErrorView(UIErrorView.Builder()
                .with(view!!.context)
                .create())
            .doAfter { runGetMyGroupsTask() }
        createGroupTask!!.execute()
    }
}
