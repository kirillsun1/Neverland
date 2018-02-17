package knk.ee.neverland.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import knk.ee.neverland.R;
import knk.ee.neverland.feed.FeedElement;

public class FeedElementAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<FeedElement> feedElementList;

    public FeedElementAdapter(Context context, List<FeedElement> feedElementList) {
        this.context = context;
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
            viewHolder.userAvatar = convertView.findViewById(R.id.user_avatar);
            viewHolder.questName = convertView.findViewById(R.id.quest_name);
            viewHolder.userName = convertView.findViewById(R.id.user_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.userName.setText(element.getUserName());
        viewHolder.questName.setText(element.getQuestName());

        return convertView;
    }

    private static class ViewHolder {
        TextView userName;
        TextView questName;
        ImageView userAvatar;
    }
}
