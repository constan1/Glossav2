package com.app.glossa_v2

import android.Manifest
import android.app.Dialog
import android.content.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import android.widget.AdapterView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.util.isNotEmpty
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import com.airbnb.lottie.LottieAnimationView
import com.app.glossa_v2.helpers.*
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.Text
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer
import com.ibm.watson.speech_to_text.v1.SpeechToText
import com.ibm.watson.text_to_speech.v1.TextToSpeech
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.lang.Exception
import java.lang.StringBuilder


class TranslatorActivity : AppCompatActivity(){



    private lateinit var spinnerPartner: spinnerPartner

    private var clipboardCopy : Boolean = true

    var sourceLanguage : String = "English"
    var targetLanguage : String = "Spanish"
    private var loading : Dialog? = null
    private var speakerDialog: Dialog? = null
    private lateinit var  microphoneHelper: MicrophoneHelper
    private lateinit var capture: MicrophoneInputStream
    private lateinit var speechService: SpeechToText

    private lateinit var textService: TextToSpeech
    private lateinit var speechToText_: SpeechToText_

    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>

    private var fromOcrText : Boolean = false

    var image_uri: Uri? = null

    lateinit var bitmap: Bitmap

    private val player = StreamPlayer()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        cameraPermission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        loading = Dialog(this)
        speakerDialog = Dialog(this)

        spinnerPartner= spinnerPartner()
        microphoneHelper = MicrophoneHelper(this)
        val sourceSpinner: Spinner = findViewById(R.id.sourceLanguageSpinner)
        val targetSpinner: Spinner = findViewById(R.id.targetLanguageSpinner)

        spinnerPartner.initiateSpinners(this,sourceSpinner,targetSpinner)

        sourceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                    val selected: String? =
                        java.lang.String.valueOf(sourceSpinner.adapter.getItem(position))

                    if (selected != null) {
                        sourceLanguage = selected
                    }

            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        targetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                    val selected: String? =
                        java.lang.String.valueOf(targetSpinner.adapter.getItem(position))
                    if (selected != null) {
                        targetLanguage = selected
                    }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        findViewById<Button>(R.id.voiceButton).setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                // Requesting the permission
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), MicrophoneHelper.REQUEST_PERMISSION)


            } else {
                speechToText_ = SpeechToText_(sourceLanguage)

                findViewById<LottieAnimationView>(R.id.animation).isVisible = true
                findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.green)
                findViewById<Button>(R.id.translateAndStopButton).isEnabled = true
                findViewById<ImageView>(R.id.close).visibility = View.VISIBLE


                it.isEnabled = false
                it.setBackgroundResource(R.color.grey)
                findViewById<Button>(R.id.voiceButton).text = ""
                microphoneHelper = MicrophoneHelper(this)
                capture = microphoneHelper.getInputStream(true)
                speechService = speechToText_.setUp(this)
                speechToText_.startInputStream(this,findViewById(R.id.translatedTextbox_view),capture,speechService)
                Snackbar.make(findViewById(R.id.textToBeTranslated),"Recording In Progress!",Snackbar.LENGTH_LONG).show()
            }


        }

        findViewById<Button>(R.id.translateAndStopButton).setOnClickListener {
            if(fromOcrText){
                TranslationTask(this,sourceLanguage,targetLanguage,loading!!).execute(findViewById<TextView>(R.id.translatedTextbox_view).text.toString())
                findViewById<Button>(R.id.translateAndStopButton).isEnabled = false
                findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.grey)

                findViewById<Button>(R.id.voiceButton).isEnabled = true
                findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.color.white)
                findViewById<Button>(R.id.voiceButton).text = "Voice"

                findViewById<ImageView>(R.id.close).visibility = View.GONE

                fromOcrText = false
            }
            else {
                findViewById<Button>(R.id.voiceButton).isEnabled = true
                findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.color.white)
                findViewById<Button>(R.id.voiceButton).text = "Voice"

                speechToText_.stopInputStream(microphoneHelper)
                findViewById<LottieAnimationView>(R.id.animation).isVisible = false
                findViewById<Button>(R.id.translateAndStopButton).isEnabled = false
                findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.grey)
                findViewById<ImageView>(R.id.close).visibility = View.GONE
                //translate here.

                TranslationTask(this, sourceLanguage, targetLanguage, loading!!).execute(
                    findViewById<TextView>(R.id.translatedTextbox_view).text.toString()
                )
            }
        }

        findViewById<ImageView>(R.id.close).setOnClickListener {

            if(fromOcrText){
                findViewById<Button>(R.id.translateAndStopButton).isEnabled = false
                findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.grey)

                //voice button
                findViewById<Button>(R.id.voiceButton).isEnabled = true
                findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.color.white)
                findViewById<Button>(R.id.voiceButton).text = "Voice"

                it.visibility = View.GONE

                fromOcrText = false
            }

            else {

                findViewById<Button>(R.id.voiceButton).isEnabled = true
                findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.color.white)
                findViewById<Button>(R.id.voiceButton).text = "Voice"
                speechToText_.stopInputStream(microphoneHelper)
                findViewById<LottieAnimationView>(R.id.animation).isVisible = false
                findViewById<Button>(R.id.translateAndStopButton).isEnabled = false
                findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.grey)
                findViewById<ImageView>(R.id.close).visibility = View.GONE
            }
        }


        findViewById<ImageView>(R.id.toClipBoard).setOnClickListener {

            if(clipboardCopy) {


                findViewById<ConstraintLayout>(R.id.translatedTextLayour_view1).setBackgroundResource(R.drawable.background_color_main)
                clipboardCopy = !clipboardCopy
                val clip = ClipData.newPlainText("copied Text", findViewById<TextView>(R.id.translatedTextbox_view).text)
                clipboard.setPrimaryClip(clip)

                Snackbar.make(findViewById(R.id.textToBeTranslated),"Copied To Clipboard!",Snackbar.LENGTH_LONG).show()
            }
            else {
                findViewById<ConstraintLayout>(R.id.translatedTextLayour_view1).setBackgroundColor(Color.WHITE)
                clipboardCopy = !clipboardCopy
            }
        }

        findViewById<Button>(R.id.readItToMeButton).setOnClickListener {

            if(fromOcrText){
                Snackbar.make(findViewById(R.id.textToBeTranslated),"Can't read while translation in progress",Snackbar.LENGTH_LONG).show()
            }
            else {
                textService = initTextToSpeechService.initTextToSpeech(this)
                SynthesisTask(this, targetLanguage, player, textService, speakerDialog!!).execute(
                    findViewById<TextView>(R.id.translatedTextbox_view).text.toString()
                )

            }
        }

        findViewById<Button>(R.id.galleryButton).setOnClickListener {

            if(fromOcrText){
                Snackbar.make(findViewById(R.id.textToBeTranslated),"Can't Do this while translation in progress",Snackbar.LENGTH_LONG).show()
            }
            else {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickGallery()
                }
            }
        }

        findViewById<Button>(R.id.cameraButton).setOnClickListener {

            if(fromOcrText){
                Snackbar.make(findViewById(R.id.textToBeTranslated),"Can't do this while translation in progress",Snackbar.LENGTH_LONG).show()
            }
            else {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
                    pickCamera()
                }
            }
        }



        findViewById<TextView>(R.id.translatedTextbox_view).addTextChangedListener(object:
        textWatcher(){

        })
    }


    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission,PermissionCodes.STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission, PermissionCodes.CAMERA_REQUEST_CODE)
    }

    private fun checkCameraPermission():Boolean{
        val resultCamera: Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED)
        val resultStorage : Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED)

        return resultCamera && resultStorage
    }

    private fun pickGallery(){
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.type ="image/*"
        startActivityForResult(intent,PermissionCodes.IMAGE_PICK_GALLERY_CODE)
    }

    private fun pickCamera(){
        val values : ContentValues = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Doc")
        values.put(MediaStore.Images.Media.DESCRIPTION, "OcrText")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val camera : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camera.putExtra(MediaStore.EXTRA_OUTPUT,image_uri)
        startActivityForResult(camera,PermissionCodes.IMAGE_PICK_CAMERA_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PermissionCodes.CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted && writeStorageAccepted) {
                    pickCamera()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

            else if(requestCode == PermissionCodes.STORAGE_REQUEST_CODE) {
                if(grantResults.isNotEmpty()){
                   val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                   if(writeStorageAccepted){
                       pickGallery()
                   }
                   else {
                       Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                   }
               }
           }


        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            if(requestCode == PermissionCodes.IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data!!.data).setGuidelines(CropImageView.Guidelines.ON).start(this)
            }
            if(requestCode == PermissionCodes.IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this)
            }
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            if(data == null){
                Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show()
            }
            val result : CropImage.ActivityResult = CropImage.getActivityResult(data)

            if(resultCode == RESULT_OK){
                val resultUri  : Uri = result.uri
                findViewById<ImageView>(R.id.ocrImage).setImageURI(resultUri)

                val bitmapDrawable = findViewById<ImageView>(R.id.ocrImage).drawable

                bitmap = bitmapDrawable.toBitmap()

                //detect text OCR function
                detectProcessText(bitmap)

            }

            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error: Exception = result.error
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }

            else {
                val intent = Intent(this,TranslatorActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun detectProcessText(bitmap: Bitmap){
        val recognizer: TextRecognizer = TextRecognizer.Builder(applicationContext).build()
        if(!recognizer.isOperational){
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
        }
        else {
            val frame: Frame = Frame.Builder().setBitmap(bitmap).build()
            val sparseArray: SparseArray<TextBlock> = recognizer.detect(frame)
            val sb: StringBuilder = StringBuilder()

            if(sparseArray.isNotEmpty()){

                for(i in 0 until sparseArray.size()){
                    val myItem: TextBlock = sparseArray.valueAt(i)
                    sb.append(myItem.value)
                    sb.append("\n")
                }
                fromOcrText = true

                //translation button
                findViewById<Button>(R.id.translateAndStopButton).isEnabled = true
                findViewById<Button>(R.id.translateAndStopButton).setBackgroundResource(R.color.green)

                findViewById<Button>(R.id.voiceButton).isEnabled = false
                findViewById<Button>(R.id.voiceButton).setBackgroundResource(R.color.grey)
                findViewById<Button>(R.id.voiceButton).text = ""
                findViewById<ImageView>(R.id.close).visibility = View.VISIBLE


                findViewById<TextView>(R.id.translatedTextbox_view).text = sb.toString()

                findViewById<NestedScrollView>(R.id.nestedScrollView).fullScroll(View.FOCUS_UP)

            }
            else {
                Toast.makeText(this,"No Text Detected!", Toast.LENGTH_LONG).show()
            }
        }
    }





}