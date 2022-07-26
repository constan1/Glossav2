package com.app.glossa_v2.helpers

import android.content.Context
import com.app.glossa_v2.R
import com.ibm.cloud.sdk.core.security.Authenticator
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.language_translator.v3.LanguageTranslator

class initTranslationService {

    companion object {


        fun initLanguageTranslatorService(context: Context): LanguageTranslator? {
            val authenticator: Authenticator =
                IamAuthenticator(context.getString(R.string.language_translator_apikey))
            val service = LanguageTranslator("2018-05-01", authenticator)
            service.serviceUrl = context.getString(R.string.language_translator_url)
            return service
        }
    }

}