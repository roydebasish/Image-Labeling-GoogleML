package com.drofficial.imagelabelinggoogleml

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.drofficial.imagelabelinggoogleml.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var imageLabeler: ImageLabeler

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        //init ,setup ImageLabelerOptions, to set the minimum confidence required e.g show only these results whose confidence is at least 95%
//        val imageLabelerOptions = ImageLabelerOptions.Builder()
//            .setConfidenceThreshold(0.95f)
//            .build()
//        imageLabeler = ImageLabeling.getClient(imageLabelerOptions)

        //Bitmap from drawable
//        val bitmapImage1 = BitmapFactory.decodeResource(resources,R.drawable.lili)

        //Bitmap from Uri,pick image from camera,gallery,Storage and store the Uri of picked image in this variable
//        val imageUri: Uri? = null
//        val bitmapImage2 = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)

        //Bitmap from ImageView
        val bitmapDrawable = binding.ivLabelImage.drawable as BitmapDrawable
        val bitmapImage3 = bitmapDrawable.bitmap

        //handle labelImage Button click, start labeling image
        binding.btnLabelImage.setOnClickListener {
            lableImage(bitmapImage3)
        }
    }


    private fun lableImage(bitmap: Bitmap){

        //change progress dialog message text and show
        progressDialog.setTitle("Preparing Image...")
        progressDialog.show()

        //prepare InputImage from bitmap
        val inputImage = InputImage.fromBitmap(bitmap,0)

        //change progress dialog message text
        progressDialog.setTitle("Labeling Image...")

        //start labeling image from inputImage
        imageLabeler.process(inputImage).addOnSuccessListener { imageLabels ->

            //we got labels fro image as List<ImageLabel>,now we will get and show detailed info
            for (imageLabel in imageLabels){
                val text = imageLabel.text
                val confidence = imageLabel.confidence
                val index = imageLabel.index

                //set result in result textview
                binding.tvResult.append("Text : $text \nConfidence : $confidence \nIndex : $index \n\n")
            }

            progressDialog.dismiss()

        }
            .addOnFailureListener{ e ->
                //task failed with an exception,hide progress dialog,show reason in toast
                Toast.makeText(this,"Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }

    }
}