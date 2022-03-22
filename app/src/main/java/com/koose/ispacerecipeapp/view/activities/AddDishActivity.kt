package com.koose.ispacerecipeapp.view.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.koose.ispacerecipeapp.databinding.ActivityAddDishBinding
import com.koose.ispacerecipeapp.databinding.PickImgCameraGalleryBinding


class AddDishActivity : AppCompatActivity() {

    private lateinit var bindingActivityDish: ActivityAddDishBinding
    lateinit var bindingPickImg: PickImgCameraGalleryBinding
        private lateinit var dialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityDish = ActivityAddDishBinding.inflate(layoutInflater
        )
        setContentView(bindingActivityDish.root)

        bindingActivityDish.ivAddDishImage.setOnClickListener {
            openCameraGalleryDialog()
        }
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
                        override fun onPermissionGranted(response: PermissionGrantedResponse) { /* ... */
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest,
                            token: PermissionToken
                        ) { /* ... */
                        }
                    }).check()
            }

            dialog.show()


        }
    }

    private fun openRetryPermission(){

    }


    companion object{
        const val REQUEST_IMAGE_CAPTURE = 100
    }
}