package com.app.glossa_v2.helpers

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.app.glossa_v2.R
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import com.ibm.watson.speech_to_text.v1.SpeechToText
import java.lang.Exception

class recordingDialog() {
   private lateinit var capture: MicrophoneInputStream

   private lateinit var  microphoneHelper: MicrophoneHelper

   private lateinit var speechService: SpeechToText
    fun showResetPasswordDialog(activity: Activity){



        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogBack  = dialog.findViewById<ImageView>(R.id.back)
        val dialogMic = dialog.findViewById<LottieAnimationView>(R.id.animation)

         microphoneHelper = MicrophoneHelper(activity)

        capture = microphoneHelper.getInputStream(true)

        speechService = SpeechToText_.setUp(activity)

        SpeechToText_.startInputStream(activity,dialog.findViewById<TextView>(R.id.textTranslatedRealTime),capture,speechService)

        dialogBack.setOnClickListener {
            SpeechToText_.stopInputStream(microphoneHelper)
            dialog.dismiss()
        }

        dialogMic.setOnClickListener {

            dialog.findViewById<LottieAnimationView>(R.id.animation).loop(false)
            SpeechToText_.stopInputStream(microphoneHelper)
        }

        dialog.findViewById<TextView>(R.id.textTranslatedRealTime).addTextChangedListener(object :
           textWatcher() {

        })


        dialog.show()
    }

}