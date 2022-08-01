package com.app.glossa_v2.helpers

import android.app.Dialog
import android.os.AsyncTask
import com.app.glossa_v2.TranslatorActivity
import com.ibm.cloud.sdk.core.http.HttpMediaType
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer
import com.ibm.watson.language_translator.v3.util.Language
import com.ibm.watson.text_to_speech.v1.TextToSpeech
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions
import java.lang.ref.WeakReference

class SynthesisTask(activity: TranslatorActivity, targetLanguage: String, player : StreamPlayer, textService: TextToSpeech, dialog:Dialog):
    AsyncTask<String, Void, String>() {


    private var targetLanguage_  = targetLanguage

    private var player_ = player

    private var textService_ = textService

    var result: String = String()

    private var weakContext : WeakReference<TranslatorActivity> = WeakReference(activity)
    private var dialog_ = dialog



    override fun onPreExecute() {
      result =  when(targetLanguage_){
            "English" -> SynthesizeOptions.Voice.EN_US_LISAV3VOICE
            "French" -> SynthesizeOptions.Voice.FR_FR_NICOLASV3VOICE
            "German" -> SynthesizeOptions.Voice.DE_DE_BIRGITV3VOICE
            "Spanish" -> SynthesizeOptions.Voice.ES_LA_SOFIAV3VOICE
            "Portuguese" -> SynthesizeOptions.Voice.PT_BR_ISABELAV3VOICE
            "Italian"->SynthesizeOptions.Voice.IT_IT_FRANCESCAV3VOICE
            "Dutch"->SynthesizeOptions.Voice.NL_NL_EMMAVOICE
            else -> ""
        }
    }
    override fun doInBackground(vararg params: String?): String {

        if (result.isEmpty()) {

            ShowSpeech(weakContext.get()!!,dialog_).showError()

            return "Not Synthesized"
        } else {
            ShowSpeech(weakContext.get()!!,dialog_).showReadBackUI()
            val synthesizeOptions = SynthesizeOptions.Builder()
                .text(params[0])
                .voice(result)
                .accept(HttpMediaType.AUDIO_WAV)
                .build()
            player_.playStream(textService_.synthesize(synthesizeOptions).execute().result)
            //show animation.
            return "Did synthesize"
        }
    }

    override fun onPostExecute(result: String?) {
       ShowSpeech(weakContext.get()!!,dialog_).hideReadBackUI()
    }
}