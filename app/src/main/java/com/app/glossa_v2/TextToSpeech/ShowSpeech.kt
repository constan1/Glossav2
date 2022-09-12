package com.app.glossa_v2.TextToSpeech

import android.app.Dialog
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.app.glossa_v2.R
import com.app.glossa_v2.ui.TranslatorActivity
import com.google.android.material.snackbar.Snackbar

class ShowSpeech(activity: TranslatorActivity, dialog: Dialog) {

    private var activity_ = activity

    private var dialog_ = dialog

        fun showReadBackUI(){

            activity_.runOnUiThread{
                dialog_.let {
                    it.show()
                    it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                    it.setContentView(R.layout.custom_speaker_dialog)
                    it.setCancelable(false)
                    it.setCanceledOnTouchOutside(false)
                }

            }
        }

        fun hideReadBackUI(){

            activity_.runOnUiThread{
                dialog_.let { if(it.isShowing)it.cancel() }
            }
        }

    fun showError(){
        activity_.runOnUiThread{
            Snackbar.make(activity_.findViewById(R.id.textToBeTranslatedView),activity_.getString(R.string.language_not_supported_for_readback),
                Snackbar.LENGTH_LONG).show()
        }
    }


}