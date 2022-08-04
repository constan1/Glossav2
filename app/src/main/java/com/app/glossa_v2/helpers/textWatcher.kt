package com.app.glossa_v2.helpers

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.app.glossa_v2.R
import com.app.glossa_v2.TranslatorActivity

abstract class textWatcher(activity: TranslatorActivity): TextWatcher {

    private var activity_ = activity

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


    }

    override fun afterTextChanged(s: Editable?) {

        if(!activity_.findViewById<EditText>(R.id.textPreTranslation).text.isEmpty()){
            activity_.findViewById<Button>(R.id.translateAndStopButton).isEnabled = true
            activity_.findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.main)
        }
    }

}