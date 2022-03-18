package com.koose.ispacerecipeapp.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.koose.ispacerecipeapp.R
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

    private fun openCameraGalleryDialog(){
        dialog = Dialog(this)
        bindingPickImg = PickImgCameraGalleryBinding.inflate(layoutInflater)
        dialog.setContentView(bindingPickImg.root)

        //set onClickon the Camera
        bindingPickImg.pictImgCamera.setOnClickListener {

        }

        bindingPickImg.pickImgGallery.setOnClickListener {

        }

        dialog.show()
    }
}