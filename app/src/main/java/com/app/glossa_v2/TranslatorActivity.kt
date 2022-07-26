package com.app.glossa_v2

import android.R.attr
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import com.app.glossa_v2.helpers.spinnerPartner
import com.google.android.material.snackbar.Snackbar
import android.R.attr.label

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.glossa_v2.helpers.recordingDialog
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import java.lang.Exception

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import java.util.jar.Manifest
import android.widget.AdapterView





class TranslatorActivity : AppCompatActivity(){



    private lateinit var spinnerPartner: spinnerPartner

    var clipboardCopy : Boolean = true

    var sourceLanguage : String = "English"
    var targetLanguage : String = "Spanish"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


        spinnerPartner= spinnerPartner()

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
                val alert = recordingDialog(this)
                alert.showDialog(sourceLanguage, targetLanguage)
            }



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
    }



}