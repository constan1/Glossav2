package com.app.glossa_v2.ui

import android.Manifest
import android.app.Dialog
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.snackbar.Snackbar

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper

import android.widget.AdapterView
import androidx.core.graphics.drawable.toBitmap
import com.airbnb.lottie.LottieAnimationView
import com.app.glossa_v2.Implementation.OcrServiceImpl
import com.app.glossa_v2.Implementation.ServiceImpl
import com.app.glossa_v2.R
import com.app.glossa_v2.Services.OcrServiceClass
import com.app.glossa_v2.Services.TranslationServiceClass
import com.app.glossa_v2.SpeechToText.textWatcher
import com.app.glossa_v2.TranslationModel.TranslationTask
import com.app.glossa_v2.helpers.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.lang.Exception



class TranslatorActivity : AppCompatActivity(){



    private var translationServiceImpl: TranslationServiceClass = ServiceImpl()
    private var ocrServiceImageButton : OcrServiceClass = OcrServiceImpl()

    private lateinit var copyBoard : ImageView
    private lateinit var clearText : ImageView
    private lateinit var animation: LottieAnimationView
    private lateinit var ocrImage: ImageView



    private lateinit var sourceLanguage : String
    private lateinit var targetLanguage : String
    private var loading : Dialog? = null
     var speakerDialog: Dialog? = null

    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>

    private var flag : Boolean = true

    private var image_uri: Uri? = null

    private lateinit var bitmap: Bitmap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator_main)

        cameraPermission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        loading = Dialog(this)
        speakerDialog = Dialog(this)


        val sourceSpinner: Spinner = findViewById(R.id.sourceLanguageSpinner)
        val targetSpinner: Spinner = findViewById(R.id.targetLanguageSpinner)


        val recordButton = findViewById<Button>(R.id.record_button)
        val speakBackButton = findViewById<Button>(R.id.speak_back_button)
        val galleryButton = findViewById<Button>(R.id.gallery_button)
        val cameraButton = findViewById<Button>(R.id.camera_button)



        val text_preTranslate = findViewById<EditText>(R.id.textPreTranslation)
        val text_postTranslate = findViewById<TextView>(R.id.translatedTextView)

        ocrImage = findViewById(R.id.ocrImage)

         copyBoard = findViewById(R.id.toClipBoard)
        clearText = findViewById(R.id.clearText)
        animation = findViewById(R.id.animation)

        val translateButton = findViewById<Button>(R.id.translateAndStopButton)


        translationServiceImpl.initiateSpinners(this,sourceSpinner,targetSpinner)

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

               if(!text_preTranslate.isEnabled && !text_preTranslate.text.isNullOrEmpty()){
                  translateButton.isEnabled = true
                   translateButton.setBackgroundResource(R.color.main)
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
                if(!translateButton.isEnabled && !findViewById<EditText>(
                        R.id.textPreTranslation
                    ).text.isNullOrEmpty()){
                    translateButton.isEnabled = true
                    translateButton.setBackgroundResource(R.color.main)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        recordButton.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                // Requesting the permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), MicrophoneHelper.REQUEST_PERMISSION)


            } else {
                if(flag) {

                    translationServiceImpl.record(this,sourceLanguage,targetLanguage,text_preTranslate)
                   clearText.visibility = View.GONE
                   copyBoard.visibility = View.GONE
                    animation.visibility = View.VISIBLE


                    flag = false
                    Snackbar.make(
                        findViewById(R.id.textToBeTranslatedView),
                        getString(R.string.recording_in_progress),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                      stopRecordingUI()
                }
            }


        }

        translateButton.setOnClickListener {

            if(!flag) {

                if(sourceLanguage != targetLanguage) {

                    translationServiceImpl.translateAndStop(this,text_preTranslate,sourceLanguage,targetLanguage,loading)

                    it.isEnabled = false
                    it.setBackgroundResource(R.color.grey)
                    copyBoard.visibility = View.VISIBLE
                    clearText.visibility = View.VISIBLE
                    animation.visibility = View.GONE
                    flag = true
                }
                else{
                    Snackbar.make(findViewById(R.id.textToBeTranslatedView),getString(R.string.sourceLanguage_targetLanguage),Snackbar.LENGTH_LONG).show()
                }
            }
            else {
                if (sourceLanguage != targetLanguage) {


                    TranslationTask(this, sourceLanguage, targetLanguage, loading!!).execute(
                        text_preTranslate.text.toString()
                    )
                    it.isEnabled = false
                    it.setBackgroundResource(R.color.grey)


                    animation.visibility = View.GONE
                    flag = true
                    copyBoard.visibility = View.VISIBLE
                    clearText.visibility = View.VISIBLE
                }
                else {
                    Snackbar.make(findViewById(R.id.textToBeTranslatedView),getString(R.string.sourceLanguage_targetLanguage),Snackbar.LENGTH_LONG).show()

                }
            }

        }



        copyBoard.setOnClickListener {

                val clip = ClipData.newPlainText(getString(R.string.copyLabel), text_postTranslate.text)
                clipboard.setPrimaryClip(clip)

                Snackbar.make(findViewById(R.id.textToBeTranslatedView),getString(R.string.copyBody),Snackbar.LENGTH_LONG).show()

        }


        speakBackButton.setOnClickListener {
            if(!flag){
                //stop recording
                stopRecordingUI()
            }
            else if(text_postTranslate.text.toString().isNotEmpty()) {

                translationServiceImpl.textToSpeech(this,text_postTranslate,sourceLanguage,targetLanguage, speakerDialog)
            }
            else {
                Snackbar.make(findViewById(R.id.textToBeTranslatedView),getString(R.string.no_text_for_playback),
                    Snackbar.LENGTH_LONG).show()
            }
        }

        galleryButton.setOnClickListener {

            if(!flag){
                //stop recording
                stopRecordingUI()
            }
            else {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickGallery()
                }
            }
        }

        cameraButton.setOnClickListener {

            if(!flag){
                //stop recording
                stopRecordingUI()
            }
            else {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
                    pickCamera()
                }
            }
        }

        clearText.setOnClickListener {

            text_preTranslate.text.clear()
            text_postTranslate.text = getString(R.string.cleared_text)
        }


        text_preTranslate.addTextChangedListener(object:
        textWatcher(this){

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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type =getString(R.string.imageType)
        startActivityForResult(intent,PermissionCodes.IMAGE_PICK_GALLERY_CODE)

    }

    private fun pickCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, getString(R.string.doc))
        values.put(MediaStore.Images.Media.DESCRIPTION, getString(R.string.ocr_text))
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
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
                       Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
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
                Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_SHORT).show()
            }
            val result : CropImage.ActivityResult = CropImage.getActivityResult(data)

            if(resultCode == RESULT_OK){
                val resultUri  : Uri = result.uri
                ocrImage.setImageURI(resultUri)

                val bitmapDrawable = ocrImage.drawable

                bitmap = bitmapDrawable.toBitmap()

                //detect text OCR function
                detectProcessText(bitmap)

            }

            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error: Exception = result.error
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show()
            }

            else {
                val intent = Intent(this, TranslatorActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun detectProcessText(bitmap: Bitmap){
        ocrServiceImageButton.detectProcessText(bitmap,this,findViewById(R.id.textPreTranslation))
        flag = true
    }

    private fun stopRecordingUI(){
        translationServiceImpl.stopRecording()
         copyBoard.visibility = View.VISIBLE
        clearText.visibility = View.VISIBLE
        animation.visibility = View.GONE
        flag = true
    }
}