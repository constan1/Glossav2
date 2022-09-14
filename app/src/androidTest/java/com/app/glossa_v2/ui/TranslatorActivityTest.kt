package com.app.glossa_v2.ui

import android.app.Dialog
import android.os.Looper
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.app.glossa_v2.Implementation.OcrServiceImpl
import com.app.glossa_v2.R
import com.app.glossa_v2.Services.OcrServiceClass
import com.app.glossa_v2.SpeechToText.SpeechToText_
import com.app.glossa_v2.SpeechToText.initSpeechToTextService
import com.app.glossa_v2.TextToSpeech.SynthesisTask
import com.app.glossa_v2.TranslationModel.ShowTranslation
import com.app.glossa_v2.TranslationModel.TranslationTask
import com.app.glossa_v2.TranslationModel.initTranslationService
import com.app.glossa_v2.helpers.initTextToSpeechService
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.util.Language
import com.ibm.watson.speech_to_text.v1.SpeechToText
import com.ibm.watson.speech_to_text.v1.model.GetLanguageModelOptions
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions
import com.ibm.watson.text_to_speech.v1.TextToSpeech
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TranslatorActivityTest{


    private var activityRule: ActivityTestRule<TranslatorActivity> = ActivityTestRule(TranslatorActivity::class.java)
    private lateinit var translateActivity: TranslatorActivity
    private lateinit var speechToText : SpeechToText
    private lateinit var speechToText_this : SpeechToText_
    private lateinit var microphoneHelperThis : MicrophoneHelper
    private lateinit var translationService : LanguageTranslator
    private lateinit var speakerDialog_: Dialog
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var ocrService: OcrServiceClass


    @Before
    fun setup(){

        activityRule.launchActivity(null)

        translateActivity = activityRule.activity

        speechToText = initSpeechToTextService.initSpeechToText(translateActivity)
        translationService = initTranslationService.initLanguageTranslatorService(translateActivity)!!
        microphoneHelperThis = MicrophoneHelper(translateActivity)
        speechToText_this = SpeechToText_("Spanish")
        textToSpeech =  initTextToSpeechService.initTextToSpeech(translateActivity)
        speakerDialog_= translateActivity.speakerDialog!!
        ocrService = OcrServiceImpl()

    }


/**=================================================================**/
    /** Test Whether Mic records successfully and returns an input stream **/
    @Test
    fun speechToText_initiateSpeechToText_initiatedSuccessfully(){
        Assert.assertNotNull(speechToText)
    }

    @Test
    fun speechToText_setupInputStream_setupSuccessfully(){
        val speechToTextThis = speechToText_this.setUp(translateActivity)
        Assert.assertEquals(speechToText.serviceUrl,speechToTextThis.serviceUrl)
    }

    @Test
    fun speechToText_startInputStream_startedSuccessfully(){
        val speechToTextThis = speechToText_this.setUp(translateActivity)
        speechToText_this.startInputStream(translateActivity, translateActivity.findViewById(R.id.textPreTranslation), microphoneHelperThis.getInputStream(true),speechToTextThis)

        Assert.assertEquals(RecognizeOptions.Model.ES_AR_BROADBANDMODEL, speechToText_this.result)
    }

    @Test
    fun speechToText_stopInputStream_stoppedSuccessfully(){
        speechToText_this.stopInputStream(microphoneHelperThis)
        Assert.assertEquals("",speechToText_this.result)
    }


    /**=================================================================**/
    /** Test Whether translation-service works. **/

    @Test
    fun translate_initiateLanguageTranslator_initiatedSuccessfully(){
        Assert.assertNotNull(translationService)
    }

    //Test the show translation dialog
    @Test
    fun translate_showTranslationDialog_dialogShowsSuccessfully(){
        ShowTranslation.showProgress(speakerDialog_,translateActivity)
        Thread.sleep(3000)

    }

    @Test
    fun translate_cancelTranslationDialog_dialogCancelledSuccessfully(){
        ShowTranslation.showProgress(speakerDialog_,translateActivity)
        Thread.sleep(3000)
        ShowTranslation.hideProgress(speakerDialog_,translateActivity)
        Thread.sleep(2000)
    }

    @Test
    fun translate_showTranslationText_showTranslationTextSuccessful(){
        ShowTranslation.showTranslation("Hola",translateActivity.findViewById<TextView>(R.id.translatedTextView),translateActivity)
        Thread.sleep(2000)
    }



    @Test
    fun translate_translateText_textTranslatedSuccessfully(){

     TranslationTask(translateActivity,"English",
           "Spanish",speakerDialog_)
            .execute("Hello, how are you?")

        Thread.sleep(3000)

        Assert.assertEquals("Hola, ¿cómo estás?",(translateActivity.findViewById<TextView>(R.id.translatedTextView)).text.toString())
    }


    /**=================================================================**/
    /** Test text to speech. **/


    @Test
    fun textToSpeech_initTextToSpeech_initSuccessful(){
        Assert.assertNotNull(textToSpeech)
    }

    @Test
    fun textToSpeech_playBack_playBackSuccessful(){
        translateActivity.findViewById<TextView>(R.id.translatedTextView).text = "Hola, ¿cómo estás?"
        SynthesisTask(translateActivity,"Spanish", StreamPlayer(),textToSpeech,speakerDialog_).execute(
            translateActivity.findViewById<TextView>(R.id.translatedTextView).text.toString()
        )
        Thread.sleep(7000)

    }

    /**=================================================================**/
    /** Detect and process text. **/


}