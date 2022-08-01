package com.app.glossa_v2.helpers

import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions
import com.ibm.watson.speech_to_text.v1.model.RecognizeWithWebsocketsOptions
import java.io.InputStream

class webSocketOptions {


    companion object {
        fun getRecognizeOptions(captureStream: InputStream,model:String): RecognizeWithWebsocketsOptions? {
            return RecognizeWithWebsocketsOptions.Builder()
                .audio(captureStream)
                .contentType(ContentType.OPUS.toString())
                .model(model)
                .interimResults(true)
                .inactivityTimeout(-1)
                .build()
        }
    }
}