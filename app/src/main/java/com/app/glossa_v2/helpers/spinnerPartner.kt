package com.app.glossa_v2.helpers

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.app.glossa_v2.R
import com.app.glossa_v2.TranslatorActivity

class spinnerPartner: serviceClass{



    override fun initiateSpinners(
        activity: TranslatorActivity,
        sourceSpinner: Spinner,
        targetSpinner: Spinner
    ) {

        val sourceAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            activity,
            R.array.languages,
            android.R.layout.simple_spinner_item
        )

        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceSpinner.adapter = sourceAdapter


        val targetAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            activity,
            R.array.languages,
            android.R.layout.simple_spinner_item
        )
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        targetSpinner.adapter = targetAdapter




    }



}



