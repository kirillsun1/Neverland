package knk.ee.neverland.activities.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import knk.ee.neverland.R
import knk.ee.neverland.views.feedview.FeedElementAdapter
import java.util.*

class FeedFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFeedListView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    private fun initializeFeedListView() {
        val feedListView = view!!.findViewById<ListView>(R.id.feed_listview)
        feedListView.adapter = FeedElementAdapter(context, ArrayList())
    }
}
