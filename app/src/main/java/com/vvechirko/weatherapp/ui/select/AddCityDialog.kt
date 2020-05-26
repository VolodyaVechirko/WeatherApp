package com.vvechirko.weatherapp.ui.select

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import com.vvechirko.weatherapp.R

class AddCityDialog(context: Context, val onDone: (text: String) -> Unit) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add_city)

        window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setGravity(Gravity.CENTER)
        }

        findViewById<View>(R.id.btnPositive).setOnClickListener {
            val input = findViewById<EditText>(R.id.editText).text.toString()
            onDone(input)
            dismiss()
        }
    }
}