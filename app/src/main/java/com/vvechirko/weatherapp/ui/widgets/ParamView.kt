package com.vvechirko.weatherapp.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.vvechirko.weatherapp.R
import kotlinx.android.synthetic.main.view_param.view.*

class ParamView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    var value: CharSequence?
        get() = tvValue.text
        set(value) {
            tvValue.text = value
        }

    var hint: CharSequence?
        get() = tvHint.text
        set(value) {
            tvHint.text = value
        }

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.view_param, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.ParamView)
        value = a.getText(R.styleable.ParamView_value)
        hint = a.getText(R.styleable.ParamView_hint)
        a.recycle()
    }
}