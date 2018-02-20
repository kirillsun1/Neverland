package knk.ee.neverland.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import knk.ee.neverland.R;
import knk.ee.neverland.questview.QuestElement;
import knk.ee.neverland.questview.QuestElementAdapter;

public class AllQuestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quests);

        fillQuestsListWithDummyQuests();
    }

    private void fillQuestsListWithDummyQuests() {
        ListView questsListView = findViewById(R.id.allquests_listview);
        questsListView.setAdapter(new QuestElementAdapter(this, createDummyList()));

        questsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), QuestActivity.class);

                QuestElement questElement = (QuestElement) adapterView.getItemAtPosition(position);

                intent.putExtra("questName", questElement.getQuestName());
                intent.putExtra("questDesc", questElement.getQuestDescription());

                startActivity(intent);
            }
        });
    }

    private List<QuestElement> createDummyList() {
        List<QuestElement> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            list.add(new QuestElement(
                    "I am quest here!",
                    "In up so discovery my middleton eagerness dejection explained. Estimating excellence ye contrasted insensible as. Oh up unsatiable advantages decisively as at interested. Present suppose in esteems in demesne colonel it to. End horrible she landlord screened stanhill. Repeated offended you opinions off dissuade ask packages screened. She alteration everything sympathize impossible his get compliment. Collected few extremity suffering met had sportsman. "
            ));
        }

        return list;
    }
}
