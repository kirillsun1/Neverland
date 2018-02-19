package knk.ee.neverland.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import knk.ee.neverland.R;
import knk.ee.neverland.feedview.FeedElementAdapter;
import knk.ee.neverland.questview.QuestElement;
import knk.ee.neverland.questview.QuestElementAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestsFragment extends Fragment {
    public QuestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillQuestsListWithDummyQuests();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quests, container, false);
    }

    private void fillQuestsListWithDummyQuests() {
        ListView questsListView = getView().findViewById(R.id.quests_listview);
        questsListView.setAdapter(new QuestElementAdapter(getContext(), createDummyList()));
    }

    private List<QuestElement> createDummyList() {
        List<QuestElement> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            list.add(new QuestElement(
                    getRandomString(getRandomNumber(5, 40)),
                    getRandomString(getRandomNumber(30, 500))
            ));
        }

        return list;
    }

    private Random random = new Random();
    private int getRandomNumber(int min, int max) {
        return min + random.nextInt(1 + max - min);
    }

    private String getRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException("length should be >= 1");

        char[] randomStringChars = new char[length];

        for (int i = 0; i < randomStringChars.length; i++) {
            randomStringChars[i] = (char) getRandomNumber(48, 122);
        }

        return new String(randomStringChars);
    }
}
