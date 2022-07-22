package com.app.glossa_v2

import android.R.attr
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import android.graphics.drawable.GradientDrawable
import android.R.attr.button
import androidx.constraintlayout.widget.ConstraintLayout


class TranslatorActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener{

    var trigger1 :Boolean = true
    val ButtonColors = intArrayOf(Color.parseColor("#04c5fd"), Color.parseColor("#a9e9fb"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)



        val sourceSpinner: Spinner = findViewById(R.id.sourceLanguageSpinner)
        val sourceAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.languages,
            android.R.layout.simple_spinner_item
        )
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceSpinner.adapter = sourceAdapter
        sourceSpinner.onItemSelectedListener = this


        val targetSpinner: Spinner = findViewById(R.id.targetLanguageSpinner)
        val targetAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.languages,
            android.R.layout.simple_spinner_item
        )
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        targetSpinner.adapter = targetAdapter
        targetSpinner.onItemSelectedListener = this
        

        findViewById<Button>(R.id.voiceButton).setOnClickListener {
            if (trigger1) {
                findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.drawable.voice_color)
                trigger1 = !trigger1
            } else if (!trigger1) {
                findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.drawable.voice_color2)
                trigger1 = !trigger1
            }

        }
        findViewById<ImageView>(R.id.toClipBoard).setOnClickListener {
            findViewById<ConstraintLayout>(R.id.clipBoardTextView).setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val text = parent!!.getItemAtPosition(position).toString();

        val result = when(text){
            "English" -> "en"
            "Spanish" -> "es"
            "Bulgarian" -> "bg"
            else -> "other"
        }

        Toast.makeText(this,result,Toast.LENGTH_LONG).show();
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}