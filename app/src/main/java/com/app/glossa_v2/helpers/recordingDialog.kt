package com.app.glossa_v2.helpers

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.app.glossa_v2.R
import com.app.glossa_v2.TranslatorActivity
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import com.ibm.watson.speech_to_text.v1.SpeechToText

class recordingDialog(activity: TranslatorActivity){
   private lateinit var capture: MicrophoneInputStream

   private lateinit var  microphoneHelper: MicrophoneHelper

   private lateinit var speechService: SpeechToText


    private var activity_ = activity



    fun showDialog(srcLanguage: String, targetLanguage: String){

        val dialog = Dialog(activity_)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogMic = dialog.findViewById<LottieAnimationView>(R.id.animation)
        val dialogTranslateButton = dialog.findViewById<Button>(R.id.translateButtonDialog)
        val dialogStopRecording = dialog.findViewById<Button>(R.id.stopRecordingButton)
        val dialogTextBox = dialog.findViewById<TextView>(R.id.textTranslatedRealTime)
        val dialogTranslatedText = activity_.findViewById<TextView>(R.id.translatedTextbox_view)



         microphoneHelper = MicrophoneHelper(activity_)
        capture = microphoneHelper.getInputStream(true)
        speechService = SpeechToText_.setUp(activity_)
        SpeechToText_.startInputStream(activity_,dialogTextBox,capture,speechService)


        dialogStopRecording.setOnClickListener {
            SpeechToText_.stopInputStream(microphoneHelper)
            dialogTranslateButton.isEnabled = true
            dialogTranslateButton.setBackgroundResource(R.color.blue_)
            dialogStopRecording.setBackgroundResource(R.color.grey)
            dialogStopRecording.isEnabled = false
            dialogMic.cancelAnimation()
        }


        dialogMic.setOnClickListener {
            SpeechToText_.stopInputStream(microphoneHelper)
        }

        dialog.findViewById<TextView>(R.id.textTranslatedRealTime).addTextChangedListener(object :
           textWatcher() {

        })

        dialog.findViewById<ImageView>(R.id.cancelDialog).setOnClickListener {
            SpeechToText_.stopInputStream(microphoneHelper)
            dialog.dismiss()
        }

        dialogTranslateButton.setOnClickListener {

            TranslationTask(activity_,srcLanguage,targetLanguage).execute(dialogTextBox.text.toString())

            dialog.dismiss()
        }


        dialog.show()
    }
}
