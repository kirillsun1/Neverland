package knk.ee.neverland.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.gordonwong.materialsheetfab.DimOverlayFrameLayout
import com.gordonwong.materialsheetfab.MaterialSheetFab
import knk.ee.neverland.R
import knk.ee.neverland.activities.AllQuestsActivity
import knk.ee.neverland.activities.CreateQuestActivity
import knk.ee.neverland.customviews.CustomFloatingActionButton
import knk.ee.neverland.questview.MyQuestElementAdapter

class QuestsFragment : Fragment() {

    private var materialSheetFab: MaterialSheetFab<CustomFloatingActionButton>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view.findViewById<View>(R.id.quests_listview) as ListView).adapter = MyQuestElementAdapter(view.context)

        setupFAB()
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
}
