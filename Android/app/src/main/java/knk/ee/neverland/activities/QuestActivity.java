package knk.ee.neverland.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import knk.ee.neverland.R;
import knk.ee.neverland.questview.QuestPictureAdapter;

public class QuestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        setTitle(getIntent().getExtras().getString("questName"));
        //((TextView) findViewById(R.id.quest_quest_name))
        //        .setText(getIntent().getExtras().getString("questName"));
        ((TextView) findViewById(R.id.quest_quest_desc))
                .setText(getIntent().getExtras().getString("questDesc"));


        ((GridView) findViewById(R.id.quest_images)).setAdapter(new QuestPictureAdapter(this));
    }

}
