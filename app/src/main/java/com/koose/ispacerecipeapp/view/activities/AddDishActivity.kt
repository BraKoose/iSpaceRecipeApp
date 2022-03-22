package com.koose.ispacerecipeapp.view.activities

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.koose.ispacerecipeapp.R
import com.koose.ispacerecipeapp.databinding.ActivityAddDishBinding
import com.koose.ispacerecipeapp.databinding.PickImgCameraGalleryBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class AddDishActivity : AppCompatActivity() {

    private lateinit var bindingActivityDish: ActivityAddDishBinding
    lateinit var bindingPickImg: PickImgCameraGalleryBinding
    private lateinit var dialog:Dialog

    // A global variable for storing image path.
    private var imagePath:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityDish = ActivityAddDishBinding.inflate(layoutInflater
        )
        setContentView(bindingActivityDish.root)

        bindingActivityDish.ivAddDishImage.setOnClickListener {
            openCameraGalleryDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE){

            data?.extras.let {
                val thumb_nail:Bitmap = data!!.extras!!.get("data") as Bitmap

                //save path of the image captured from camera
                imagePath = saveImageToInternalStorage(thumb_nail)
                // Set Capture Image bitmap to the imageView using Glide
                Glide.with(this@AddDishActivity)
                    .load(thumb_nail)
                    .centerCrop()
                    .into(bindingActivityDish.ivDishImage)

                bindingActivityDish.ivAddDishImage.setImageDrawable(
                    ContextCompat.getDrawable(this@AddDishActivity, R.drawable.ic_edit)
                )
            }

        }else if (requestCode == PICK_IMAGE){
            data.let {
                val selectedImagePath = data?.data

                Glide.with(this@AddDishActivity)
                    .load(selectedImagePath)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // log exception
                            Log.e("TAG", "Error loading image", e)
                            return false // important to return false so the error placeholder can be placed
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            val bitmap: Bitmap = resource!!.toBitmap()

                            imagePath = saveImageToInternalStorage(bitmap)
                            Log.i("ImagePath", imagePath)
                            return false


                        }



                    })

                    .into( bindingActivityDish.ivDishImage)

                // Replace the add icon with edit icon once the image is selected.
                bindingActivityDish.ivAddDishImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddDishActivity,
                        R.drawable.ic_edit
                    )
                )
            }



        }



    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): String {

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        /**
         * The Mode Private here is
         * File creation mode: the default mode, where the created file can only
         * be accessed by the calling application (or all applications sharing the
         * same user ID).
         */
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        // Mention a file name to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }
        // Return the saved image absolute path
        return file.absolutePath
    }

    private fun openCameraGalleryDialog() {
        dialog = Dialog(this)
        bindingPickImg = PickImgCameraGalleryBinding.inflate(layoutInflater)
        dialog.setContentView(bindingPickImg.root)

        //set onClick listener on the Camera
        bindingPickImg.pictImgCamera.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */

                        if (report.areAllPermissionsGranted()) {
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        openRetryPermission()
                    }
                }).check()
            //set onClick listener on Gallery
            bindingPickImg.pickImgGallery.setOnClickListener {
                Dexter.withContext(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            //Pick Image from Gallery Intent
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(
                                Intent.createChooser(intent, "Select Picture"),
                                PICK_IMAGE
                            )
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) {

                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest,
                            token: PermissionToken
                        ) {

                        }
                    }).check()
            }

            dialog.show()


        }
    }

    private fun openRetryPermission(){
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    companion object{
        const val REQUEST_IMAGE_CAPTURE = 100
        const val PICK_IMAGE = 200
        private const val IMAGE_DIRECTORY = "FavDishImages"
    }
}