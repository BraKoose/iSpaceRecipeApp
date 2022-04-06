package com.ispacegh.babuckman.ispacerecipeapp.views.activities

import android.Manifest
import android.app.Activity
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
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ispacegh.babuckman.ispacerecipeapp.R
import com.ispacegh.babuckman.ispacerecipeapp.adapter.CustomListItemAdapter
import com.ispacegh.babuckman.ispacerecipeapp.databinding.ActivityAddDishBinding
import com.ispacegh.babuckman.ispacerecipeapp.databinding.DialogCustomListBinding
import com.ispacegh.babuckman.ispacerecipeapp.databinding.PickImgFromCameraGallaryBinding
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import com.ispacegh.babuckman.ispacerecipeapp.utils.Constants
import com.ispacegh.babuckman.ispacerecipeapp.utils.FavDishApplication
import com.ispacegh.babuckman.ispacerecipeapp.viewmodel.FavDishViewModel
import com.ispacegh.babuckman.ispacerecipeapp.viewmodel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class AddDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var addDishBinding:ActivityAddDishBinding

    //global variable for storing image path
    private var mImagePath:String = ""

    //global variable for dialog
    private lateinit var mCustomListDialog:Dialog
    private lateinit var bindingDialogCustom: DialogCustomListBinding

    //global variable for image selection layout
    private lateinit var pickImgBinding: PickImgFromCameraGallaryBinding

    // TODO Step 3: Create a global variable for dish details that we will receive via intent.
    // START
    private var mRecipeDetails: RecipeData? = null
    // END
    /**Declare dialog box global so you can dismiss
     * that from everywhere in this class or activity **/
    private lateinit var dialog:Dialog

    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDishBinding = ActivityAddDishBinding.inflate(layoutInflater)
        setContentView(addDishBinding.root)

        // get edit intent from allRecipeFragment
        if (intent.hasExtra(Constants.EXTRA_DISH_DETAILS)) {
            mRecipeDetails = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }
        // END

        // TODO Step 7: Set the existing dish details to the view to edit.
        // START
        mRecipeDetails?.let {
            if (it.id != 0) {
                mImagePath = it.image

                // Load the dish image in the ImageView.
                Glide.with(this@AddDishActivity)
                    .load(mImagePath)
                    .centerCrop()
                    .into(addDishBinding.ivDishImage)

                addDishBinding.etTitle.setText(it.title)
                addDishBinding.etType.setText(it.type)
                addDishBinding.etCategory.setText(it.category)
                addDishBinding.etIngredients.setText(it.ingredients)
                addDishBinding.etCookingTime.setText(it.cookingTime)
                addDishBinding.etDirectionToCook.setText(it.directionToCook)

                addDishBinding.btnAddDish.text = resources.getString(R.string.lbl_update_dish)
            }
        }
        // END

        addDishBinding.ivAddDishImage.setOnClickListener {
            openCameraGalleryDialog()
        }
        addDishBinding.etType.setOnClickListener(this)
        addDishBinding.etCategory.setOnClickListener(this)
        addDishBinding.etCookingTime.setOnClickListener(this)
        addDishBinding.btnAddDish.setOnClickListener(this)
    }

    override fun onClick(view: View) {
            when(view.id){
                R.id.iv_add_dish_image -> {
                    openCameraGalleryDialog()
                    return
                }
                R.id.et_type -> {
                    customItemsListDialog(
                        resources.getString(R.string.title_select_dish_type),
                        Constants.dishTypes(),
                        Constants.DISH_TYPE
                    )
                    return
                }
                R.id.et_category -> {
                    customItemsListDialog(
                        resources.getString(R.string.title_select_dish_category),
                        Constants.dishCategories(),
                        Constants.DISH_CATEGORY
                    )
                    return
                }
                R.id.et_cooking_time -> {
                    customItemsListDialog(
                        resources.getString(R.string.title_select_cooking_time),
                        Constants.dishCookTime(),
                        Constants.DISH_COOKING_TIME
                    )
                    return
                }
                R.id.btn_add_dish ->{
                    // Define the local variables and get the EditText values.
                    // For Dish Image we have the global variable defined already.

                    val title = addDishBinding.etTitle.text.toString().trim { it <= ' ' }
                    val type = addDishBinding.etType.text.toString().trim { it <= ' ' }
                    val category = addDishBinding.etCategory.text.toString().trim { it <= ' ' }
                    val ingredients = addDishBinding.etIngredients.text.toString().trim { it <= ' ' }
                    val cookingTimeInMinutes = addDishBinding.etCookingTime.text.toString().trim { it <= ' ' }
                    val cookingDirection = addDishBinding.etDirectionToCook.text.toString().trim { it <= ' ' }

                    when {

                        TextUtils.isEmpty(mImagePath) -> {
                            Toast.makeText(
                                this@AddDishActivity,
                                resources.getString(R.string.err_msg_select_dish_image),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(title) -> {
                            Toast.makeText(
                                this@AddDishActivity,
                                resources.getString(R.string.err_msg_enter_dish_title),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(type) -> {
                            Toast.makeText(
                                this@AddDishActivity,
                                resources.getString(R.string.err_msg_select_dish_type),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(category) -> {
                            Toast.makeText(
                                this@AddDishActivity,
                                resources.getString(R.string.err_msg_select_dish_category),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(ingredients) -> {
                            Toast.makeText(
                                this@AddDishActivity,
                                resources.getString(R.string.err_msg_enter_dish_ingredients),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingTimeInMinutes) -> {
                            Toast.makeText(
                                this@AddDishActivity,
                                resources.getString(R.string.err_msg_select_dish_cooking_time),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingDirection) -> {
                            Toast.makeText(
                                this@AddDishActivity,
                                resources.getString(R.string.err_msg_enter_dish_cooking_instructions),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            // TODO Step 8: Update the data and pass the details to
                            //  ViewModel to Insert or Update
                            // START

                            var dishID = 0
                            var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                            var favoriteDish = false

                            mRecipeDetails?.let {
                                if (it.id != 0) {
                                    dishID = it.id
                                    imageSource = it.imageSource
                                    favoriteDish = it.favoriteDish
                                }
                            }


                            // START
                            val favDishDetails: RecipeData = RecipeData(
                                mImagePath,
                                imageSource,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTimeInMinutes,
                                cookingDirection,
                                favoriteDish,
                                dishID

                                )

                            if(dishID == 0) {
                                mFavDishViewModel.insert(favDishDetails)

                                Toast.makeText(
                                    this@AddDishActivity,
                                    "You successfully added your  dish to the database.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // You even print the log if Toast is not displayed on emulator
                                Log.e("Insertion", "Success")
                            }else{
                                mFavDishViewModel.update(favDishDetails)

                                Toast.makeText(
                                    this@AddDishActivity,
                                    "You successfully updated your  dish details.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // You even print the log if Toast is not displayed on emulator
                                Log.e("Updating", "Success")
                            }
                            // END


                            finish()
                        }
                    }
                }
            }
    }

    private fun customItemsListDialog(dishTypeTitle: String, dishTypes: ArrayList<String>, selectedDishType: String) {
        mCustomListDialog = Dialog(this@AddDishActivity)
        bindingDialogCustom = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(bindingDialogCustom.root)

        bindingDialogCustom.tvTitle.text = title

        // Set the LayoutManager that this RecyclerView will use.
        bindingDialogCustom.rvList.layoutManager = LinearLayoutManager(this@AddDishActivity)
        // Adapter class is initialized and list is passed in the param.
        val adapter = CustomListItemAdapter(this@AddDishActivity, dishTypes, selectedDishType)
        // adapter instance is set to the recyclerview to inflate the items.
        bindingDialogCustom.rvList.adapter = adapter
        //Start the dialog and display it on screen.
        mCustomListDialog.show()
    }

    private fun openCameraGalleryDialog() {
        dialog = Dialog(this)
        pickImgBinding = PickImgFromCameraGallaryBinding.inflate(layoutInflater)
        dialog.setContentView(pickImgBinding.root)

        pickImgBinding.actionPickCamera.setOnClickListener {
            Dexter.withContext(this@AddDishActivity)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        report.let {
                            if (report.areAllPermissionsGranted()) {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent, CAMERA)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?, token: PermissionToken?
                    ) {
                    /* when permission is denied by user */
                        showRationalDialogForPermissions()
                    }
                })
                .check()
        }

        pickImgBinding.actionPickImage.setOnClickListener {
            Dexter.withContext(this@AddDishActivity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object:PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        // TODO Step 2: Launch the gallery for Image selection using the constant.
                        // START
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(galleryIntent, GALLERY)
                        // END
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                            this@AddDishActivity,
                            "You have denied the storage permission to select image.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        /* when permission is denied by user */
                        showRationalDialogForPermissions()
                    }

                })
                .check();

            //dialog.dismiss()
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAMERA){
            data?.extras?.let {
                val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap //Bitmap from camera

                //Save path of the image captured from camera
                mImagePath = saveImageToInternalStorage(thumbnail)

                Glide.with(this@AddDishActivity)
                    .load(thumbnail)
                    .centerCrop()
                    .into(addDishBinding.ivDishImage)

                addDishBinding.ivAddDishImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddDishActivity,
                        R.drawable.ic_edit)
                )
            }
        }else if(requestCode == GALLERY){
            data.let {
                val selectedImagePath = data?.data

                //glide
                Glide.with(this@AddDishActivity)
                    .load(selectedImagePath)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object:RequestListener<Drawable>{
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
                            val bitmap:Bitmap = resource!!.toBitmap()

                            mImagePath = saveImageToInternalStorage(bitmap)
                            Log.i("ImagePath", mImagePath)
                            return false
                        }
                    })
                    .into(addDishBinding.ivDishImage)

                // Replace the add icon with edit icon once the image is selected.
                addDishBinding.ivAddDishImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddDishActivity,
                        R.drawable.ic_edit
                    )
                )
            }
        }
    }

    private fun saveImageToInternalStorage(thumbnail: Bitmap): String {

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
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        //return saved image absolute path
        return file.absolutePath
    }

    /**
     * A function used to show the alert dialog when the permissions are denied and need to allow it from settings app info.
     */
    private fun showRationalDialogForPermissions() {
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

    fun selectedListItem(item: String, selection: String) {
        when (selection) {

            Constants.DISH_TYPE -> {
                mCustomListDialog.dismiss()
                addDishBinding.etType.setText(item)
            }

            Constants.DISH_CATEGORY -> {
                mCustomListDialog.dismiss()
                addDishBinding.etCategory.setText(item)
            }
            else -> {
                mCustomListDialog.dismiss()
                addDishBinding.etCookingTime.setText(item)
            }
        }
    }

    companion object {
        private const val CAMERA = 100
        private const val GALLERY = 200
        private const val IMAGE_DIRECTORY = "FavDishImages"
    }
}