package knk.ee.neverland.feed;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import knk.ee.neverland.R;

public class FeedElementView extends View {
    private ProgressBar progressBar;

    public FeedElementView(Context context) {
        super(context);

        progressBar = findViewById(R.id.rating_bar);

        findViewById(R.id.add_rating).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeRating(1);
            }
        });

        findViewById(R.id.take_rating).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeRating(-1);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void changeRating(int value) {
        int current = progressBar.getProgress();
        current += value;
        if (current < progressBar.getMin()) {
            current = progressBar.getMin();
        }
        if (current > progressBar.getMax()) {
            current = progressBar.getMax();
        }
        progressBar.setProgress(current);
    }


}
