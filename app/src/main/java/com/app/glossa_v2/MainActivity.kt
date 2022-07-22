package com.app.glossa_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.toDocumentDetail).setOnClickListener {

            startActivity(Intent(this@MainActivity, TranslatorActivity::class.java))
        }
    }


}