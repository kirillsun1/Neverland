package knk.ee.neverland.activities.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.gordonwong.materialsheetfab.DimOverlayFrameLayout
import com.gordonwong.materialsheetfab.MaterialSheetFab
import knk.ee.neverland.R
import knk.ee.neverland.activities.AllQuestsActivity
import knk.ee.neverland.activities.CreateQuestActivity
import knk.ee.neverland.activities.QuestActivity
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.models.Quest
import knk.ee.neverland.views.CustomFloatingActionButton
import knk.ee.neverland.views.questview.MyQuestElementAdapter

class QuestsFragment : Fragment() {
    private var materialSheetFab: MaterialSheetFab<CustomFloatingActionButton>? = null
    private var myQuestElementAdapter: MyQuestElementAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<View>(R.id.quests_listview) as ListView
        myQuestElementAdapter = MyQuestElementAdapter(view.context)
        listView.adapter = myQuestElementAdapter

        listView.setOnItemClickListener { adapterView: AdapterView<*>, _: View, position: Int, _: Long ->
            openQuestActivity(adapterView.adapter.getItem(position) as Quest)
        }

        setupFAB()
    }

    override fun onResume() {
        super.onResume()
        GetMyQuestsTask().execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quests, container, false)
    }

    private fun setupFAB() {
        val fab = view!!.findViewById(R.id.quests_menu) as CustomFloatingActionButton
        val sheetView = view!!.findViewById(R.id.fab_sheet) as CardView
        val overlay = view!!.findViewById(R.id.overlay) as DimOverlayFrameLayout
        val sheetColor = ContextCompat.getColor(view!!.context, R.color.custom_fab_sheet_color)
        val fabColor = ContextCompat.getColor(view!!.context, R.color.custom_fab_color)

        // Initialize material sheet FAB
        materialSheetFab = MaterialSheetFab<CustomFloatingActionButton>(fab, sheetView, overlay,
                sheetColor, fabColor)

        view!!.findViewById<TextView>(R.id.fab_sheet_item_suggest_quest).setOnClickListener {
            openSuggestQuestActivity()
        }

        view!!.findViewById<TextView>(R.id.fab_sheet_item_find_quest).setOnClickListener {
            openFindQuestActivity()
        }
    }

    private fun openQuestActivity(quest: Quest) {
        val intent = Intent(view!!.context, QuestActivity::class.java)

        intent.putExtra("questID", quest.id)
        intent.putExtra("questTitle", quest.title)
        intent.putExtra("questDesc", quest.description)
        intent.putExtra("questAuthor", "${quest.creator.firstName} ${quest.creator.secondName}")
        intent.putExtra("questCreatedDate", quest.timeCreated.toString())

        startActivity(intent)
    }

    private fun openSuggestQuestActivity() {
        val suggestIntent = Intent(view!!.context, CreateQuestActivity::class.java)
        startActivity(suggestIntent)
        materialSheetFab?.hideSheet()
    }

    private fun openFindQuestActivity() {
        val findIntent = Intent(view!!.context, AllQuestsActivity::class.java)
        startActivity(findIntent)
        materialSheetFab?.hideSheet()
    }

    private fun showToast(message: String) {
        Toast.makeText(view!!.context, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetMyQuestsTask : AsyncTask<Void, Void, Boolean>() {
        private var listOfQuests: List<Quest> = emptyList()

        override fun doInBackground(vararg p0: Void?): Boolean {
            try {
                listOfQuests = DefaultAPI.questAPI.getMyQuests()
                println(listOfQuests)
                return true
            } catch (ex: QuestAPIException) {
                return false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                myQuestElementAdapter!!.questsList = listOfQuests
                myQuestElementAdapter!!.notifyDataSetChanged()
            } else {
                showToast(getString(R.string.error_failed_getting_quests))
            }
        }
    }
}
