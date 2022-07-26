package com.app.glossa_v2.helpers

import android.app.Activity
import android.content.Context
import android.widget.TextView
import org.w3c.dom.Text

class ShowTranslation {

    companion object{

       fun showTranslation(translation: String, translatedText:TextView, activity: Activity) {
           activity.runOnUiThread(Runnable { translatedText.text = translation })
        }
    }
}