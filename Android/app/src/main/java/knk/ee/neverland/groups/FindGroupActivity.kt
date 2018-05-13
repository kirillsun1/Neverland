package knk.ee.neverland.groups

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.mindorks.placeholderview.PlaceHolderView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Group
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView

class FindGroupActivity : AppCompatActivity() {

    @BindView(R.id.groups_list)
    lateinit var groupsList: PlaceHolderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_group)
        setSupportActionBar(findViewById(R.id.toolbar))
        ButterKnife.bind(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Join group"

        loadGroupsListTask()
    }

    private fun loadGroupsListTask() {
        APIAsyncTask<List<Group>>()
            .request { DefaultAPI.groupAPI.getGroupsToJoin() }
            .handleResult {
                groupsList.removeAllViews()
                it.forEach { groupsList.addView(JoinGroupElement(this, it)) }
            }
            .uiErrorView(UIErrorView.Builder()
                .with(this)
                .create())
            .execute()
    }
}
