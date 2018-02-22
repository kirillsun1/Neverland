package knk.ee.neverland.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import knk.ee.neverland.R;

public class InputView extends FrameLayout {

    private String inputViewText;
    private String inputViewHint;
    private Drawable inputViewIcon;
    private int inputTextFontColor;

    public InputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputView, 0, 0);
        inputViewText = typedArray.getString(R.styleable.InputView_inputBoxText);
        inputViewHint = typedArray.getString(R.styleable.InputView_inputBoxHint);
        inputViewIcon = typedArray.getDrawable(R.styleable.InputView_inputBoxIcon);
        inputTextFontColor = typedArray.getColor(R.styleable.InputView_inputBoxFontColor,
                context.getResources().getColor(R.color.colorPrimaryDark));
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        addView(LayoutInflater.from(getContext()).inflate(R.layout.input_view, this, false));

        ((ImageView) findViewById(R.id.inputbox_icon)).setImageDrawable(inputViewIcon);
        TextView textView = findViewById(R.id.inputbox_text);
        textView.setText(inputViewText);
        textView.setTextColor(inputTextFontColor);
        textView.setHint(inputViewHint);
    }
}
