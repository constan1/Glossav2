package com.app.glossa_v2

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.glossa_v2.helpers.spinnerPartner
import com.google.android.material.snackbar.Snackbar

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import android.widget.AdapterView
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.app.glossa_v2.helpers.SpeechToText_
import com.app.glossa_v2.helpers.TranslationTask
import com.app.glossa_v2.helpers.textWatcher
import com.ibm.watson.speech_to_text.v1.SpeechToText
import org.w3c.dom.Text


class TranslatorActivity : AppCompatActivity(){



    private lateinit var spinnerPartner: spinnerPartner

    private var clipboardCopy : Boolean = true

    var sourceLanguage : String = "English"
    var targetLanguage : String = "Spanish"
    private var loading : Dialog? = null
    private lateinit var  microphoneHelper: MicrophoneHelper
    private lateinit var capture: MicrophoneInputStream
    private lateinit var speechService: SpeechToText

    override fun onPause() {
        super.onPause()
        SpeechToText_.stopInputStream(microphoneHelper)

            findViewById<Button>(R.id.voiceButton).isEnabled = true
            findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.color.white)
            findViewById<Button>(R.id.voiceButton).text = "Voice"
            SpeechToText_.stopInputStream(microphoneHelper)
            findViewById<LottieAnimationView>(R.id.animation).isVisible = false
            findViewById<Button>(R.id.translateAndStopButton).isEnabled = false
            findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.grey)
            findViewById<Button>(R.id.voiceButton).isEnabled = true

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        loading = Dialog(this)

        spinnerPartner= spinnerPartner()
        microphoneHelper = MicrophoneHelper(this)
        val sourceSpinner: Spinner = findViewById(R.id.sourceLanguageSpinner)
        val targetSpinner: Spinner = findViewById(R.id.targetLanguageSpinner)

        spinnerPartner.initiateSpinners(this,sourceSpinner,targetSpinner)

        sourceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                    val selected: String? =
                        java.lang.String.valueOf(sourceSpinner.adapter.getItem(position))

                    if (selected != null) {
                        sourceLanguage = selected
                    }


            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        targetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                    val selected: String? =
                        java.lang.String.valueOf(targetSpinner.adapter.getItem(position))
                    if (selected != null) {
                        targetLanguage = selected
                    }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        findViewById<Button>(R.id.voiceButton).setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                // Requesting the permission
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), MicrophoneHelper.REQUEST_PERMISSION)


            } else {
                findViewById<LottieAnimationView>(R.id.animation).isVisible = true
                findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.green)
                findViewById<Button>(R.id.translateAndStopButton).isEnabled = true
                findViewById<ImageView>(R.id.close).visibility = View.VISIBLE


                it.isEnabled = false
                it.setBackgroundResource(R.color.grey)
                findViewById<Button>(R.id.voiceButton).text = ""
                microphoneHelper = MicrophoneHelper(this)
                capture = microphoneHelper.getInputStream(true)
                speechService = SpeechToText_.setUp(this)
                SpeechToText_.startInputStream(this,findViewById(R.id.translatedTextbox_view),capture,speechService)
                Snackbar.make(findViewById(R.id.textToBeTranslated),"Recording In Progress!",Snackbar.LENGTH_LONG).show()
            }


        }

        findViewById<Button>(R.id.translateAndStopButton).setOnClickListener {
            findViewById<Button>(R.id.voiceButton).isEnabled = true
            findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.color.white)
            findViewById<Button>(R.id.voiceButton).text = "Voice"
            SpeechToText_.stopInputStream(microphoneHelper)
            findViewById<LottieAnimationView>(R.id.animation).isVisible = false
            findViewById<Button>(R.id.translateAndStopButton).isEnabled = false
            findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.grey)
            findViewById<Button>(R.id.voiceButton).isEnabled = true
            findViewById<ImageView>(R.id.close).visibility = View.GONE
            //translate here.

            TranslationTask(this,sourceLanguage,targetLanguage,loading!!).execute(findViewById<TextView>(R.id.translatedTextbox_view).text.toString())

        }

        findViewById<ImageView>(R.id.close).setOnClickListener {
            findViewById<Button>(R.id.voiceButton).isEnabled = true
            findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.color.white)
            findViewById<Button>(R.id.voiceButton).text = "Voice"
            SpeechToText_.stopInputStream(microphoneHelper)
            findViewById<LottieAnimationView>(R.id.animation).isVisible = false
            findViewById<Button>(R.id.translateAndStopButton).isEnabled = false
            findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.grey)
            findViewById<Button>(R.id.voiceButton).isEnabled = true
            findViewById<ImageView>(R.id.close).visibility = View.GONE

        }


        findViewById<ImageView>(R.id.toClipBoard).setOnClickListener {

            if(clipboardCopy) {


                findViewById<ConstraintLayout>(R.id.translatedTextLayour_view1).setBackgroundResource(R.drawable.background_color_main)
                clipboardCopy = !clipboardCopy
                val clip = ClipData.newPlainText("copied Text", findViewById<TextView>(R.id.translatedTextbox_view).text)
                clipboard.setPrimaryClip(clip)

                Snackbar.make(findViewById(R.id.textToBeTranslated),"Copied To Clipboard!",Snackbar.LENGTH_LONG).show()
            }
            else {
                findViewById<ConstraintLayout>(R.id.translatedTextLayour_view1).setBackgroundColor(Color.WHITE)
                clipboardCopy = !clipboardCopy
            }
        }
        findViewById<TextView>(R.id.translatedTextbox_view).addTextChangedListener(object:
        textWatcher(){

        })
    }



}