package knk.ee.neverland.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import knk.ee.neverland.R

class InputView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private val inputViewText: String?
    private val inputViewHint: String?
    private val inputViewIcon: Drawable?
    private val inputTextFontColor: Int

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputView, 0, 0)
        inputViewText = typedArray.getString(R.styleable.InputView_inputBoxText)
        inputViewHint = typedArray.getString(R.styleable.InputView_inputBoxHint)
        inputViewIcon = typedArray.getDrawable(R.styleable.InputView_inputBoxIcon)
        inputTextFontColor = typedArray.getColor(R.styleable.InputView_inputBoxFontColor,
                ContextCompat.getColor(context, R.color.colorPrimaryDark))
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        addView(LayoutInflater.from(context).inflate(R.layout.input_view, this, false))

        (findViewById<View>(R.id.inputbox_icon) as ImageView).setImageDrawable(inputViewIcon)
        val textView = findViewById<TextView>(R.id.inputbox_text)
        textView.text = inputViewText
        textView.setTextColor(inputTextFontColor)
        textView.hint = inputViewHint
    }
}
