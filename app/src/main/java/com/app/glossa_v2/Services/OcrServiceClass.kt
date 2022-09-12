package com.app.glossa_v2.Services

import android.graphics.Bitmap
import android.widget.TextView
import com.app.glossa_v2.ui.TranslatorActivity

interface OcrServiceClass {

    fun detectProcessText(bitmap: Bitmap, translatorActivity: TranslatorActivity, textView: TextView)
}