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

import knk.ee.neverland.R;
import knk.ee.neverland.feed.FeedElementAdapter;
import knk.ee.neverland.feed.FeedElement;

public class FeedFragment extends Fragment {
    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeFeedListView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    private void initializeFeedListView() {
        ListView feedListView = getView().findViewById(R.id.feed_listview);
        feedListView.setAdapter(new FeedElementAdapter(getContext(), createDummyList()));
    }

    private List<FeedElement> createDummyList() {
        List<FeedElement> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            list.add(new FeedElement());
        }

        return list;
    }
}
