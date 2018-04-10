package knk.ee.neverland.views.questview;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class QuestPictureAdapter extends BaseAdapter {

    private Context context;


    public QuestPictureAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        // imageView.setImageResource(someImages[i]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthOfView = displayMetrics.widthPixels / 3;

        imageView.setLayoutParams(new GridView.LayoutParams(widthOfView, widthOfView));
        return imageView;
    }
}
