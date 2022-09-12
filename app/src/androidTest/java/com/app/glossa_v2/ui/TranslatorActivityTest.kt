package com.app.glossa_v2.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.app.glossa_v2.R
import com.app.glossa_v2.SpeechToText.SpeechToText_
import com.app.glossa_v2.SpeechToText.initSpeechToTextService
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.speech_to_text.v1.SpeechToText
import com.ibm.watson.speech_to_text.v1.model.GetLanguageModelOptions
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TranslatorActivityTest{



    private var activityRule: ActivityTestRule<TranslatorActivity> = ActivityTestRule(TranslatorActivity::class.java)
    private lateinit var translateActivity: TranslatorActivity
    private lateinit var speechToText : SpeechToText
    private lateinit var speechToText_this : SpeechToText_
    private lateinit var microphoneHelperThis : MicrophoneHelper



    @Before
    fun setup(){

        activityRule.launchActivity(null)

        translateActivity = activityRule.activity

        speechToText = initSpeechToTextService.initSpeechToText(translateActivity)
        microphoneHelperThis = MicrophoneHelper(translateActivity)
        speechToText_this = SpeechToText_("English")
    }


    @Test
    fun speechToText_initiateSpeechToText_initiatedSuccessfully(){
        Assert.assertNotNull(speechToText)
    }



    @Test
    fun speechToText_setupInputStream_setupSuccessfully(){
        val speechToTextThis = speechToText_this.setUp(translateActivity)
        Assert.assertEquals(speechToText.serviceUrl,speechToTextThis.serviceUrl)
    }

    


}