package com.koose.ispacerecipeapp.view.activities

import android.Manifest
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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

        //set onClickon the Camera
        bindingPickImg.pictImgCamera.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */

                        if (report.areAllPermissionsGranted()) {
                            val take
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) { /* ... */
                    }
                }).check()

            bindingPickImg.pickImgGallery.setOnClickListener {

            }

            dialog.show()


        }
    }
    companion object{
        const val REQUEST_IMAGE_CAPTURE
    }
}