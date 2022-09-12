package com.app.glossa_v2.Implementation

import android.graphics.Bitmap
import android.util.SparseArray
import android.widget.TextView
import androidx.core.util.isNotEmpty
import com.app.glossa_v2.R
import com.app.glossa_v2.Services.OcrServiceClass
import com.app.glossa_v2.ui.TranslatorActivity
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.lang.StringBuilder

class OcrServiceImpl: OcrServiceClass {
    override fun detectProcessText(bitmap: Bitmap, translatorActivity: TranslatorActivity, textView: TextView) {
        val recognizer: TextRecognizer = TextRecognizer.Builder(translatorActivity.applicationContext).build()
        if(recognizer.isOperational){
            val frame: Frame = Frame.Builder().setBitmap(bitmap).build()
            val sparseArray: SparseArray<TextBlock> = recognizer.detect(frame)
            val sb = StringBuilder()

            if(sparseArray.isNotEmpty()){

                for(i in 0 until sparseArray.size()){
                    val myItem: TextBlock = sparseArray.valueAt(i)
                    sb.append(myItem.value)
                    sb.append("\n")
                }
                //translation button
                textView.text = sb.toString()
            }
            else {
                textView.text = translatorActivity.getString(R.string.no_text_detected)
            }
        }
    }
}