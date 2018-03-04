package knk.ee.neverland.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import knk.ee.neverland.R
import knk.ee.neverland.activities.AllQuestsActivity
import knk.ee.neverland.questview.MyQuestElementAdapter

class QuestsFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view.findViewById<View>(R.id.quests_listview) as ListView).adapter = MyQuestElementAdapter(view.context)

        view.findViewById<View>(R.id.quests_addquest)
                .setOnClickListener { view ->
                    val intent = Intent(view.context, AllQuestsActivity::class.java)
                    startActivity(intent)
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quests, container, false)
    }


}
