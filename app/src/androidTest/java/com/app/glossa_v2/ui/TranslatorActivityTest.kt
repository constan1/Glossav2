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
import com.app.glossa_v2.R
import com.app.glossa_v2.SpeechToText.SpeechToText_
import com.app.glossa_v2.SpeechToText.initSpeechToTextService
import com.app.glossa_v2.TranslationModel.TranslationTask
import com.app.glossa_v2.TranslationModel.initTranslationService
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.util.Language
import com.ibm.watson.speech_to_text.v1.SpeechToText
import com.ibm.watson.speech_to_text.v1.model.GetLanguageModelOptions
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.w3c.dom.Text

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





    @Before
    fun setup(){

        activityRule.launchActivity(null)

        translateActivity = activityRule.activity

        speechToText = initSpeechToTextService.initSpeechToText(translateActivity)
        translationService = initTranslationService.initLanguageTranslatorService(translateActivity)!!
        microphoneHelperThis = MicrophoneHelper(translateActivity)
        speechToText_this = SpeechToText_("Spanish")
     speakerDialog_= translateActivity.speakerDialog!!

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

    }

    @Test
    fun translate_translateText_textTranslatedSuccessfully(){

     TranslationTask(translateActivity,"English",
           "Spanish",speakerDialog_)
            .execute("Hello, how are you?")

        Thread.sleep(3000)

        Assert.assertEquals("Hola, ¿cómo estás?",(translateActivity.findViewById<TextView>(R.id.translatedTextView)).text.toString())
    }



}