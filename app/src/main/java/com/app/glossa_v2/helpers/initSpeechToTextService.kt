package com.app.glossa_v2.helpers

import android.content.Context
import android.speech.tts.TextToSpeech
import com.app.glossa_v2.R
import com.ibm.cloud.sdk.core.security.Authenticator
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.speech_to_text.v1.SpeechToText

class initSpeechToTextService {

    companion object {


        fun initSpeechToText(context: Context): SpeechToText {
            val authenticator: Authenticator =
                IamAuthenticator(context.getString(R.string.speech_text_apikey))
            val service = SpeechToText(authenticator)
            service.serviceUrl = context.getString(R.string.speech_text_url)
            return service
        }
    }

}