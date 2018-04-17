package knk.ee.neverland.quests

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Quest
import knk.ee.neverland.utils.APIAsyncRequest

class TakeQuestActivity : AppCompatActivity() {
    private lateinit var takeQuestListAdapter: TakeQuestListAdapter
    private lateinit var questListSwipper: SwipeRefreshLayout

    private var takingQuest: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_quest)

        initializeQuestsList()
        runUpdateQuestsTask(false)
    }

    private fun initializeQuestsList() {
        val questsListView = findViewById<ListView>(R.id.all_quests_list_view)

        takeQuestListAdapter = TakeQuestListAdapter(this)
        questsListView.adapter = takeQuestListAdapter

        questsListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (!takingQuest) {
                askConfirmationAndTakeQuest(position)
            }
        }

        findViewById<EditText>(R.id.quest_search_bar).addTextChangedListener(QuestTextWatcher())

        questListSwipper = findViewById(R.id.all_quests_list_view_swiper)
        questListSwipper.setOnRefreshListener {
            runUpdateQuestsTask(true)
        }
    }

    private fun askConfirmationAndTakeQuest(pos: Int) {
        val quest = takeQuestListAdapter.getItem(pos)
        val message = getString(R.string.taking_quest_confirmation).format(quest.title)

        AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.yes), { dialogInterface: DialogInterface, _: Int ->
                runTakeQuestTask(quest.id, pos)
                dialogInterface.cancel()
            })
            .setNegativeButton(getString(R.string.no), { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.cancel()
            })
            .create()
            .show()
    }

    private fun runUpdateQuestsTask(updating: Boolean) {
        APIAsyncRequest.Builder<List<Quest>>()
            .before {
                if (updating) {
                    questListSwipper.isRefreshing = true
                }
            }
            .request { DefaultAPI.questAPI.getQuestsToTake() }
            .handleResult { takeQuestListAdapter.addQuests(it!!) }
            .onAPIFailMessage { R.string.error_failed_getting_quests }
            .setContext(this)
            .showMessages(true)
            .after {
                questListSwipper.isRefreshing = false
            }
            .finish()
            .execute()

    }

    private fun runTakeQuestTask(questID: Int, positionInAdapter: Int) {
        if (!takingQuest) {
            APIAsyncRequest.Builder<Boolean>()
                .before { takingQuest = true }
                .request {
                    DefaultAPI.questAPI.takeQuest(questID)
                    true
                }
                .onAPIFailMessage { R.string.error_failed_taking_quest }
                .setContext(this)
                .showMessages(true)
                .after {
                    takeQuestListAdapter.removeQuest(positionInAdapter)
                    takingQuest = false
                }
                .finish()
                .execute()
        }
    }

    private inner class QuestTextWatcher : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            takeQuestListAdapter.filter.filter(p0.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }
}
