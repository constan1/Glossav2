package com.app.glossa_v2.helpers

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.widget.TextView
import com.app.glossa_v2.R
import com.app.glossa_v2.TranslatorActivity
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import com.ibm.watson.language_translator.v3.model.TranslationResult
import com.ibm.watson.language_translator.v3.util.Language
import java.lang.ref.WeakReference

class TranslationTask(activity: TranslatorActivity, srcLanguage: String, targetLanguage:String, dialog: Dialog) :
    AsyncTask<String?, Void?, String>() {


    private lateinit var translationService : LanguageTranslator
    private var weakContext : WeakReference<TranslatorActivity> = WeakReference(activity)

    private var srcLanguage_ = srcLanguage
    private var targetLanguage_ = targetLanguage

    private var translatedText : String = String()

    private var progressDialog = dialog
    override fun onPreExecute() {


        when(srcLanguage_){

            "English"-> Language.ENGLISH
            "Bosnian" -> Language.BOSNIAN
            "Czech" -> Language.CZECH
            "German" -> Language.GERMAN
            "Spanish" -> Language.SPANISH
            "French" -> Language.FRENCH
            "Dutch" -> Language.DUTCH
            "Polish" -> Language.POLISH
            "Portugues" -> Language.PORTUGUESE
            "Romanian" -> Language.ROMANIAN
            "Swedish" -> Language.SWEDISH
        }

        when(targetLanguage_){

            "Bosnian" -> Language.BOSNIAN
            "Czech" -> Language.CZECH
            "German" -> Language.GERMAN
            "English"-> Language.ENGLISH
            "Spanish" -> Language.SPANISH
            "French" -> Language.FRENCH
            "Dutch" -> Language.DUTCH
            "Polish" -> Language.POLISH
            "Portugues" -> Language.PORTUGUESE
            "Romanian" -> Language.ROMANIAN
            "Swedish" -> Language.SWEDISH
        }

        translationService = weakContext.get()
            ?.let { initTranslationService.initLanguageTranslatorService(it) }!!
    }

    override fun doInBackground(vararg params: String?): String {
        //Instantiate loading

         ShowTranslation.showProgress(progressDialog,weakContext.get()!!)
        val translateOptions = TranslateOptions.Builder()
            .addText(params[0])
            .source(srcLanguage_)
            .target(targetLanguage_)
            .build()
        val result: TranslationResult =
            translationService.translate(translateOptions).execute().result

        translatedText = result.translations[0].translation

        return translatedText
    }

    override fun onPostExecute(result: String?) {
       ShowTranslation.showTranslation(translatedText,weakContext.get()!!.findViewById(R.id.translatedTextbox_view),weakContext.get()!!)
        progressDialog.let { ShowTranslation.hideProgress(it,weakContext.get()!!) }
    }
}

