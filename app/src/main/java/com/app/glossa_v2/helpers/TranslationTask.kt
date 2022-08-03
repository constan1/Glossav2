package com.app.glossa_v2.helpers

import android.app.Dialog
import android.os.AsyncTask
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

    private var translationModel:  Boolean = false

    private var srcLanguage_result : String = String()
    private var targetLanguage_result : String = String()

    private var progressDialog = dialog
    override fun onPreExecute() {

        srcLanguage_result = when(srcLanguage_){

            "English"-> Language.ENGLISH
            "Italian" -> Language.ITALIAN
            "German" -> Language.GERMAN
            "Spanish" -> Language.SPANISH
            "French" -> Language.FRENCH
            "Portuguese" -> Language.PORTUGUESE
            else -> {Language.ENGLISH}
        }

       targetLanguage_result = when(targetLanguage_){

            "Czech" -> Language.CZECH
            "Italian" -> Language.ITALIAN
            "German" -> Language.GERMAN
            "English"-> Language.ENGLISH
            "Spanish" -> Language.SPANISH
            "French" -> Language.FRENCH
            "Dutch" -> Language.DUTCH
            "Polish" -> Language.POLISH
            "Portuguese" -> Language.PORTUGUESE
            "Romanian" -> Language.ROMANIAN
            "Swedish" -> Language.SWEDISH
           else -> {""}
       }

        if(srcLanguage_result ==  Language.GERMAN && (targetLanguage_result ==Language.ENGLISH || targetLanguage_result == Language.FRENCH || targetLanguage_result==Language.ITALIAN)){
            translationModel = true
        }
        else if (srcLanguage_result == Language.SPANISH &&(targetLanguage_result ==Language.ENGLISH || targetLanguage_result == Language.FRENCH)){
            translationModel = true
        }
        else if(srcLanguage_result == Language.FRENCH &&(targetLanguage_result == Language.ENGLISH ||  targetLanguage_result == Language.GERMAN || targetLanguage_result == Language.SPANISH )){
            translationModel =  true
        }
        else if(srcLanguage_result == Language.ITALIAN && (targetLanguage_result==Language.ENGLISH || targetLanguage_result == Language.GERMAN))
        {
            translationModel = true
        }
        else if(srcLanguage_result == Language.PORTUGUESE && (targetLanguage_result == Language.ENGLISH)){
            translationModel = true
        }
        else translationModel = srcLanguage_result == Language.ENGLISH


        translationService = weakContext.get()
            ?.let { initTranslationService.initLanguageTranslatorService(it) }!!
    }

    override fun doInBackground(vararg params: String?): String {
        //Instantiate loading

        if(translationModel) {
            ShowTranslation.showProgress(progressDialog, weakContext.get()!!)
            val translateOptions = TranslateOptions.Builder()
                .addText(params[0])
                .source(srcLanguage_result)
                .target(targetLanguage_result)
                .build()
            val result: TranslationResult =
                translationService.translate(translateOptions).execute().result

            translatedText = result.translations[0].translation

            return translatedText
        }
        translatedText = "false"
        return translatedText

    }

    override fun onPostExecute(result: String?) {

        if(translatedText == "false"){
            ShowTranslation.showTranslationError(weakContext.get()!!)
        }
        else {

            ShowTranslation.showTranslation(
                translatedText,
                weakContext.get()!!.findViewById(R.id.translatedTextView),
                weakContext.get()!!
            )
            progressDialog.let { ShowTranslation.hideProgress(it, weakContext.get()!!) }
        }
    }
}

