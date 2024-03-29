package com.app.glossa_v2.MicrophoneModel

import android.widget.TextView
import com.app.glossa_v2.R
import com.app.glossa_v2.ui.TranslatorActivity
import com.google.android.material.snackbar.Snackbar
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback
import com.ibm.watson.speech_to_text.v1.websocket.RecognizeCallback
import java.lang.Exception
import java.lang.RuntimeException


class MicrophoneSetup(activity: TranslatorActivity, inputText:TextView): BaseRecognizeCallback(), RecognizeCallback {

    private var activity_ = activity

    private var input_ = inputText



    private fun showMicText(text: String) {

        activity_.runOnUiThread {
            input_.text = text
        }
    }

    private fun disConnected() {

        activity_.runOnUiThread {   /*
        As text changes it will appear in a dialog
        **/
            Snackbar.make(activity_.findViewById(R.id.textToBeTranslatedView),activity_.getString(R.string.recording_stopped),Snackbar.LENGTH_LONG).show()
            //show
        }
    }


    override fun onTranscription(speechResults: SpeechRecognitionResults) {
        println(speechResults)
        if (speechResults.results != null && speechResults.results.isNotEmpty()) {
            val text = speechResults.results[0].alternatives[0].transcript
            showMicText(text)
        }

    }

    override fun onError(e: Exception?) {
        super.onError(e)
    }

    override fun onDisconnected() {
        super.onDisconnected()
        disConnected()
    }

    override fun onInactivityTimeout(runtimeException: RuntimeException?) {
        super.onInactivityTimeout(runtimeException)
    }
}