package com.app.glossa_v2.helpers

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import com.app.glossa_v2.R
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text

class ShowTranslation {

    companion object {

        fun showTranslation(translation: String, translatedText: TextView, activity: Activity) {
            activity.runOnUiThread(Runnable { translatedText.text = translation })
        }

        fun showProgress(progress:Dialog, activity: Activity) {
            activity.runOnUiThread {
                progress.let {
                    it.show()
                    it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                    it.setContentView(R.layout.custom_progress_layout)
                    it.setCancelable(false)
                    it.setCanceledOnTouchOutside(false)
                }
            }
        }


        fun hideProgress(loading: Dialog, activity: Activity){
            activity.runOnUiThread {
                loading.let { if(it.isShowing)it.cancel() }
            }
        }

        fun showTranslationError(activity: Activity){
            activity.runOnUiThread {
                val dialog : Dialog = Dialog(activity)
                dialog.setContentView(R.layout.custom_error_translation_dialog)
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)
                dialog.show()

            }
        }
    }
}