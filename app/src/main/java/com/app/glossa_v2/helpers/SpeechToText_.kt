package com.app.glossa_v2.helpers

import android.app.Activity
import android.widget.TextView
import com.app.glossa_v2.R
import com.app.glossa_v2.TranslatorActivity
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import com.ibm.watson.speech_to_text.v1.SpeechToText
import java.lang.Exception

class SpeechToText_ {

    companion object{
        fun setUp(activity: Activity) : SpeechToText{
            return initSpeechToTextService.initSpeechToText(activity)!!
        }

        fun startInputStream(activity: TranslatorActivity,input: TextView,capture: MicrophoneInputStream, speechService: SpeechToText){
            Thread {
                try {
                    speechService.recognizeUsingWebSocket(
                        webSocketOptions.getRecognizeOptions(capture),
                        com.app.glossa_v2.helpers.MicrophoneSetup(activity,input)

                    )
                } catch (e: Exception) {
                }

            }.start()
        }

        fun stopInputStream( microphoneHelper: MicrophoneHelper){
            microphoneHelper.closeInputStream()
        }
    }
}