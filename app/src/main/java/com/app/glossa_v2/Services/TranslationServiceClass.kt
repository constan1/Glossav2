package com.app.glossa_v2.Services

import android.app.Dialog
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.app.glossa_v2.ui.TranslatorActivity

interface TranslationServiceClass {

    fun initiateSpinners(activity: TranslatorActivity, sourceSpinner: Spinner, targetSpinner:Spinner)

    fun record(translatorActivity: TranslatorActivity, sourceLanguage: String, targetLanguage: String, textView: TextView)

    fun translateAndStop(translatorActivity: TranslatorActivity, editText: EditText, sourceLanguage: String, targetLanguage: String, loading: Dialog?)

    fun stopRecording()

    fun textToSpeech(translatorActivity: TranslatorActivity, textView: TextView, sourceLanguage: String, targetLanguage: String, speakerDialog:Dialog?)
}