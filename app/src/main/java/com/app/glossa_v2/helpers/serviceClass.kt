package com.app.glossa_v2.helpers

import android.content.Context
import android.widget.Spinner
import com.app.glossa_v2.TranslatorActivity

interface serviceClass {

    fun initiateSpinners(activity: TranslatorActivity, sourceSpinner: Spinner, targetSpinner:Spinner)
}