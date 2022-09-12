package com.app.glossa_v2.Implementation

import android.app.Dialog
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.app.glossa_v2.R
import com.app.glossa_v2.Services.TranslationServiceClass
import com.app.glossa_v2.ui.TranslatorActivity
import com.app.glossa_v2.SpeechToText.SpeechToText_
import com.app.glossa_v2.TextToSpeech.SynthesisTask
import com.app.glossa_v2.TranslationModel.TranslationTask
import com.app.glossa_v2.helpers.initTextToSpeechService
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer
import com.ibm.watson.speech_to_text.v1.SpeechToText


//We Will not use Dagger-hilt for dependency injection. We will input dependencies within manual containers.

//speechToText_: SpeechToText_,speechToText: SpeechToText,microphoneHelper: MicrophoneHelper
class ServiceImpl(
): TranslationServiceClass {

    private lateinit var speechToText_this : SpeechToText_
    private lateinit var speechToTextThis : SpeechToText
    private lateinit var microphoneHelperThis : MicrophoneHelper
    private lateinit var textService: com.ibm.watson.text_to_speech.v1.TextToSpeech


    override fun initiateSpinners(
        activity: TranslatorActivity,
        sourceSpinner: Spinner,
        targetSpinner: Spinner
    ) {

        val sourceAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            activity,
            R.array.languages_spinner1,
            android.R.layout.simple_spinner_item
        )

        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceSpinner.adapter = sourceAdapter


        val targetAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            activity,
            R.array.languages_spinner2,
            android.R.layout.simple_spinner_item
        )
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        targetSpinner.adapter = targetAdapter

    }

    override fun record(translatorActivity: TranslatorActivity, sourceLanguage: String, targetLanguage:String, textView: TextView){

        speechToText_this = SpeechToText_(sourceLanguage)

        microphoneHelperThis = MicrophoneHelper(translatorActivity)
        speechToTextThis = speechToText_this.setUp(translatorActivity)

        speechToText_this.startInputStream(
            translatorActivity,
            textView,
            microphoneHelperThis.getInputStream(true),
            speechToTextThis
        )
    }

    override fun translateAndStop(translatorActivity: TranslatorActivity, editText: EditText, sourceLanguage: String, targetLanguage: String, loading: Dialog?) {

        TranslationTask(translatorActivity, sourceLanguage, targetLanguage, loading!!).execute(
            editText.text.toString()
        )
        speechToText_this.stopInputStream(microphoneHelperThis)
    }

    override fun stopRecording() {
        speechToText_this.stopInputStream(microphoneHelperThis)
    }

    override fun textToSpeech(translatorActivity: TranslatorActivity, textView: TextView, sourceLanguage: String, targetLanguage: String, speakerDialog:Dialog?) {
        textService = initTextToSpeechService.initTextToSpeech(translatorActivity)
        SynthesisTask(translatorActivity, targetLanguage, StreamPlayer(), textService, speakerDialog!!).execute(
           textView.text.toString()
        )
    }


}

