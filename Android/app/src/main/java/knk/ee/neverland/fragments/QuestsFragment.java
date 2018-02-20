package knk.ee.neverland.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import knk.ee.neverland.R;
import knk.ee.neverland.activities.AllQuestsActivity;
import knk.ee.neverland.questview.MyQuestElementAdapter;


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

        ((ListView) view.findViewById(R.id.quests_listview))
                .setAdapter(new MyQuestElementAdapter(view.getContext()));

        view.findViewById(R.id.quests_addquest)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), AllQuestsActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quests, container, false);
    }


}
