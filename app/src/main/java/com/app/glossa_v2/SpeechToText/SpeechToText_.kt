package com.app.glossa_v2.SpeechToText

import android.app.Activity
import android.widget.TextView
import com.app.glossa_v2.MicrophoneModel.MicrophoneSetup
import com.app.glossa_v2.ui.TranslatorActivity
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import com.ibm.watson.speech_to_text.v1.SpeechToText
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions
import java.lang.Exception

class SpeechToText_(srcLanguage: String){


    private val srcLanguage_ = srcLanguage

    var result : String = String()

        fun setUp(activity: Activity) : SpeechToText{

           result =  when(srcLanguage_){

                "English"-> RecognizeOptions.Model.EN_US_BROADBANDMODEL
                "Italian" -> RecognizeOptions.Model.IT_IT_BROADBANDMODEL
                "German" -> RecognizeOptions.Model.DE_DE_BROADBANDMODEL
                "Spanish" -> RecognizeOptions.Model.ES_AR_BROADBANDMODEL
                "French" -> RecognizeOptions.Model.FR_CA_BROADBANDMODEL
                "Portuguese" -> RecognizeOptions.Model.PT_BR_BROADBANDMODEL
               else -> {""}
           }
            return initSpeechToTextService.initSpeechToText(activity)
        }

        fun startInputStream(activity: TranslatorActivity, input: TextView, capture: MicrophoneInputStream, speechService: SpeechToText){
            Thread {
                try {
                    speechService.recognizeUsingWebSocket(
                        webSocketOptions.getRecognizeOptions(capture, result),
                        MicrophoneSetup(activity,input)

                    )
                } catch (e: Exception) {
                }

            }.start()
        }

        fun stopInputStream( microphoneHelper: MicrophoneHelper){
            microphoneHelper.closeInputStream()
        }

}