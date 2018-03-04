package knk.ee.neverland.questview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import knk.ee.neverland.R;

public class MyQuestElementAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<MyQuestElement> questElementList;

    public MyQuestElementAdapter(Context context) {  //, List<QuestElement> questElementList) {
        // this.questElementList = questElementList;

        questElementList = new ArrayList<>();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return questElementList.size();
    }

    @Override
    public Object getItem(int i) {
        return questElementList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MyQuestElement element = (MyQuestElement) getItem(i);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.quest_element, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.questName = convertView.findViewById(R.id.quests_quest_name);
            viewHolder.questDesc = convertView.findViewById(R.id.quests_quest_desc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.questName.setText(element.getQuestName());
        viewHolder.questDesc.setText(element.getQuestDescription());

        return convertView;
    }

    private static class ViewHolder {
        TextView questName;
        TextView questDesc;
    }
}
