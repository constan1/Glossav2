package com.app.glossa_v2.helpers

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.widget.Button
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.app.glossa_v2.R
import com.app.glossa_v2.TranslatorActivity
import com.google.android.material.snackbar.Snackbar

class ShowSpeech(activity: TranslatorActivity, dialog: Dialog) {

    private var activity_ = activity

    private lateinit var readItToMe : Button

    private var dialog_ = dialog

        fun showReadBackUI(){

            activity_.runOnUiThread{
                readItToMe = activity_.findViewById<Button>(R.id.readItToMeButton)
                readItToMe.isEnabled=false
                readItToMe.setBackgroundResource(R.color.grey)
                readItToMe.text = ""
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
                readItToMe = activity_.findViewById<Button>(R.id.readItToMeButton)
                readItToMe.isEnabled = true
                readItToMe.setBackgroundResource(R.color.white)
                readItToMe.text = "Read It To Me"
                dialog_.let { if(it.isShowing)it.cancel() }
            }
        }

    fun showError(){
        activity_.runOnUiThread{
            Snackbar.make(activity_.findViewById(R.id.textToBeTranslated)," Language Not Supported For Read Back At This Time",
                Snackbar.LENGTH_LONG).show()
        }
    }


}