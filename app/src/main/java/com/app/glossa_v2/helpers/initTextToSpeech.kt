package com.app.glossa_v2.helpers

import android.content.Context
import android.provider.Settings.Global.getString
import com.app.glossa_v2.R
import com.ibm.cloud.sdk.core.security.Authenticator
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.text_to_speech.v1.TextToSpeech

class initTextToSpeechService {

    companion object{


        fun initTextToSpeech(context: Context): TextToSpeech{
            val authenticator: Authenticator =
                IamAuthenticator(context.getString(R.string.text_speech_apikey))
            val service = com.ibm.watson.text_to_speech.v1.TextToSpeech(authenticator)
            service.serviceUrl = context.getString(R.string.text_speech_url)
            return service

        }
    }
}