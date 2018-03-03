package knk.ee.neverland.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ListView
import knk.ee.neverland.R
import knk.ee.neverland.questview.QuestElement
import knk.ee.neverland.questview.QuestElementAdapter
import java.util.*

class AllQuestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_quests)

        fillQuestsListWithDummyQuests()
    }

    private fun fillQuestsListWithDummyQuests() {
        val questsListView = findViewById<ListView>(R.id.allquests_listview)
        questsListView.adapter = QuestElementAdapter(this, ArrayList())

        questsListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
            val intent = Intent(applicationContext, QuestActivity::class.java)

            val questElement = adapterView.getItemAtPosition(position) as QuestElement

            intent.putExtra("questName", questElement.questName)
            intent.putExtra("questDesc", questElement.questDescription)

            startActivity(intent)
        }
    }
}
