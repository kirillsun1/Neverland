package knk.ee.neverland.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.GridView
import android.widget.TextView

import knk.ee.neverland.R
import knk.ee.neverland.questview.QuestPictureAdapter

class QuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)

        title = intent.extras!!.getString("questName")
        //((TextView) findViewById(R.id.quest_quest_name))
        //        .setText(getIntent().getExtras().getString("questName"));
        (findViewById<View>(R.id.quest_quest_desc) as TextView).text = intent.extras!!.getString("questDesc")


        (findViewById<View>(R.id.quest_images) as GridView).adapter = QuestPictureAdapter(this)
    }

}
