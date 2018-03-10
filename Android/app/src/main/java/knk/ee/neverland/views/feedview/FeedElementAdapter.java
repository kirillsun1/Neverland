package knk.ee.neverland.views.feedview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import knk.ee.neverland.R;

public class FeedElementAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<FeedElement> feedElementList;

    public FeedElementAdapter(Context context, List<FeedElement> feedElementList) {
        this.feedElementList = feedElementList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return feedElementList.size();
    }

    @Override
    public Object getItem(int i) {
        return feedElementList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        FeedElement element = (FeedElement) getItem(i);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.feed_element, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.userAvatar = convertView.findViewById(R.id.feed_user_avatar);
            viewHolder.questName = convertView.findViewById(R.id.feed_quest_name);
            viewHolder.userName = convertView.findViewById(R.id.feed_user_name);
            viewHolder.ratingBar = convertView.findViewById(R.id.feed_rating_bar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.userName.setText(element.getUserName());
        viewHolder.questName.setText(element.getQuestName());
        viewHolder.ratingBar.setProgress(element.getValue());

        return convertView;
    }

    private static class ViewHolder {
        TextView userName;
        TextView questName;
        ImageView userAvatar;
        ProgressBar ratingBar;
    }
}
