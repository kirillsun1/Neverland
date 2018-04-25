package knk.ee.neverland.quests

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.gordonwong.materialsheetfab.DimOverlayFrameLayout
import com.gordonwong.materialsheetfab.MaterialSheetFab
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Quest
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView
import knk.ee.neverland.views.CustomFloatingActionButton

class TakenQuestsFragment : Fragment() {
    private lateinit var materialSheetFab: MaterialSheetFab<CustomFloatingActionButton>
    private lateinit var takenQuestsListAdapter: TakenQuestsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<View>(R.id.quests_listview) as ListView
        takenQuestsListAdapter = TakenQuestsListAdapter(view.context)
        listView.adapter = takenQuestsListAdapter

        listView.setOnItemClickListener { adapterView: AdapterView<*>, _: View, position: Int, _: Long ->
            openQuestActivity(adapterView.adapter.getItem(position) as Quest)
        }

        view.findViewById<Toolbar>(R.id.toolbar).title = getString(R.string.quests_fragment_title)

        setupFAB()

        runGetMyQuestsTask()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quests, container, false)
    }

    override fun onResume() {
        super.onResume()

        runGetMyQuestsTask()
    }

    private fun setupFAB() {
        val fab = view!!.findViewById(R.id.quests_menu) as CustomFloatingActionButton
        val sheetView = view!!.findViewById(R.id.fab_sheet) as CardView
        val overlay = view!!.findViewById(R.id.overlay) as DimOverlayFrameLayout
        val sheetColor = ContextCompat.getColor(view!!.context, R.color.custom_fab_sheet_color)
        val fabColor = ContextCompat.getColor(view!!.context, R.color.custom_fab_color)

        // Initialize material sheet FAB
        materialSheetFab = MaterialSheetFab(fab, sheetView, overlay,
            sheetColor, fabColor)

        view!!.findViewById<TextView>(R.id.fab_sheet_item_suggest_quest).setOnClickListener {
            openSuggestQuestActivity()
        }

        view!!.findViewById<TextView>(R.id.fab_sheet_item_find_quest).setOnClickListener {
            openFindQuestActivity()
        }
    }


    private fun runGetMyQuestsTask() {
        APIAsyncTask<List<Quest>>()
            .request { DefaultAPI.questAPI.getMyQuests() }
            .handleResult {
                takenQuestsListAdapter.questsList = it
                takenQuestsListAdapter.notifyDataSetChanged()
            }
            .uiErrorView(UIErrorView.Builder().with(view!!.context).create())
            .execute()
    }

    private fun openQuestActivity(quest: Quest) {
        val intent = Intent(activity!!.applicationContext, QuestDetailsActivity::class.java)

        intent.putExtra("questID", quest.id)
        intent.putExtra("questTitle", quest.title)
        intent.putExtra("questDesc", quest.description)
        intent.putExtra("questAuthor", "${quest.author.firstName} ${quest.author.secondName}")
        intent.putExtra("questCreatedDate", quest.timeCreated.toString())

        startActivity(intent)
    }

    private fun openSuggestQuestActivity() {
        val suggestIntent = Intent(activity!!.applicationContext, CreateQuestActivity::class.java)
        startActivity(suggestIntent)
        materialSheetFab.hideSheet()
    }

    private fun openFindQuestActivity() {
        val findIntent = Intent(activity!!.applicationContext, TakeQuestActivity::class.java)
        startActivity(findIntent)
        materialSheetFab.hideSheet()
    }
}
