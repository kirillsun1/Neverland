package knk.ee.neverland.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.models.Quest

class CreateQuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quest)

        findViewById<Button>(R.id.createquest_save).setOnClickListener {
            try {
                DefaultAPI.questAPI.submitNewQuest(makeQuest())
            } catch (ex: QuestAPIException) {
                showError(ex.message.orEmpty())
            }
        }
    }

    fun makeQuest(): Quest {
        val quest = Quest()

        quest.name = (findViewById<EditText>(R.id.quest_title)).text.toString()
        quest.description = (findViewById<EditText>(R.id.quest_desc)).text.toString()

        return quest
    }

    fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}
