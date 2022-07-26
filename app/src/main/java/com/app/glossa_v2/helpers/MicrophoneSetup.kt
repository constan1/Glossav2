package com.app.glossa_v2.helpers

import android.app.Activity
import android.graphics.Color
import android.widget.TextView
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback
import com.ibm.watson.speech_to_text.v1.websocket.RecognizeCallback
import java.lang.Exception
import java.lang.RuntimeException


class MicrophoneSetup(activity: Activity, input:TextView): BaseRecognizeCallback(), RecognizeCallback {

    var activity_ = activity
    var input_ = input

    var listening = false

    private fun showMicText(text: String) {

        activity_.runOnUiThread { input_.text = text }
    }
    private fun disableMicButton() {

        listening = false
    }

    override fun onTranscription(speechResults: SpeechRecognitionResults) {
        println(speechResults)
        if (speechResults.results != null && !speechResults.results.isEmpty()) {
            val text = speechResults.results[0].alternatives[0].transcript
            showMicText(text)
        }
    }

    override fun onError(e: Exception?) {
        disableMicButton()
    }

    override fun onDisconnected() {
        super.onDisconnected()
        disableMicButton()
    }

    override fun onInactivityTimeout(runtimeException: RuntimeException?) {
        super.onInactivityTimeout(runtimeException)
    }
}