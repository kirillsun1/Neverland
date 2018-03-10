package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.models.Quest
import knk.ee.neverland.views.questview.QuestElementAdapter

class AllQuestsActivity : AppCompatActivity() {
    private var questListAdapter: QuestElementAdapter? = null

    private var takingQuest: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_quests)

        initializeQuestsList()
        UpdateQuestsTask().execute()
    }

    private fun initializeQuestsList() {
        val questsListView = findViewById<ListView>(R.id.all_quests_listview)

        questListAdapter = QuestElementAdapter(this)
        questsListView.adapter = questListAdapter

        questsListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (!takingQuest) {
                askConfirmationAndTakeQuest(position)
            }
        }

        findViewById<EditText>(R.id.quest_search_bar).addTextChangedListener(QuestTextWatcher())
    }

    private fun askConfirmationAndTakeQuest(pos: Int) {
        val quest = questListAdapter!!.getItem(pos)
        val message = getString(R.string.taking_quest_confirmation).format(quest.title)

        AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.yes), { dialogInterface: DialogInterface, _: Int ->
                    takingQuest = true
                    TakeQuestTask(quest.id, pos).execute()
                    dialogInterface.cancel()
                })
                .setNegativeButton(getString(R.string.no), { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                })
                .create()
                .show()
    }

    private fun showToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class UpdateQuestsTask : AsyncTask<Void, Void, Boolean>() {
        private var questsListGot: List<Quest>? = null

        override fun doInBackground(vararg p0: Void?): Boolean {
            try {
                questsListGot = DefaultAPI.questAPI.getQuestsToTake()
                return true
            } catch (ex: QuestAPIException) {
                return false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                questListAdapter!!.addQuests(questsListGot!!)
            } else {
                showToast(getString(R.string.error_failed_getting_quests))
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class TakeQuestTask(val questID: Int, val positionInAdapter: Int) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg p0: Void?): Boolean {
            try {
                DefaultAPI.questAPI.takeQuest(questID)
                return true
            } catch (ex: QuestAPIException) {
                return false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                questListAdapter!!.removeQuest(positionInAdapter)
                questListAdapter!!.notifyDataSetChanged()

            } else {
                showToast(getString(R.string.error_failed_taking_quest))
            }
            takingQuest = false
        }
    }

    private inner class QuestTextWatcher : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            questListAdapter!!.filter.filter(p0.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }
}
