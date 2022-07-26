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
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.glossa_v2.helpers.recordingDialog
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import java.lang.Exception

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import java.util.jar.Manifest


class TranslatorActivity : AppCompatActivity(){



    private lateinit var spinnerPartner: spinnerPartner

    var trigger1 :Boolean = true
    var clipboardCopy : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


        spinnerPartner= spinnerPartner()

        val sourceSpinner: Spinner = findViewById(R.id.sourceLanguageSpinner)
        val targetSpinner: Spinner = findViewById(R.id.targetLanguageSpinner)

        spinnerPartner.initiateSpinners(this,sourceSpinner,targetSpinner)


        findViewById<Button>(R.id.voiceButton).setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                // Requesting the permission
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), MicrophoneHelper.REQUEST_PERMISSION)
            } else {
                val alert = recordingDialog()
                alert.showResetPasswordDialog(this)
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