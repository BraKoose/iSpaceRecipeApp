package com.hgecapsi.ispacerecipeevening.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
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
import com.hgecapsi.ispacerecipeevening.R
import com.hgecapsi.ispacerecipeevening.adapter.CustomListItemAdapter
import com.hgecapsi.ispacerecipeevening.data.RecipeData
import com.hgecapsi.ispacerecipeevening.databinding.ActivityAddDishBinding
import com.hgecapsi.ispacerecipeevening.databinding.DialogCustomListBinding
import com.hgecapsi.ispacerecipeevening.databinding.PickImgCameraGalleryBinding
import com.hgecapsi.ispacerecipeevening.utils.FavDishApplication
import com.hgecapsi.ispacerecipeevening.viewmodel.FavDishViewModel
import com.hgecapsi.ispacerecipeevening.viewmodel.FavDishViewModelFactory
import com.hgecapsi.ispacerecipeevening.utils.Constants
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
    private lateinit var bindingActivityAddDish: ActivityAddDishBinding
    private lateinit var bindingPickImg: PickImgCameraGalleryBinding
      private lateinit var dialog:Dialog

      private lateinit var mCustomListDialog:Dialog
      private lateinit var bindingDialogCustom: DialogCustomListBinding

    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }


    // A global variable for storing image path.
    private var imagePath:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityAddDish = ActivityAddDishBinding.inflate(layoutInflater)
        setContentView(bindingActivityAddDish.root)


      bindingActivityAddDish.ivAddDishImage.setOnClickListener {
          openCameraGalleryDioalog()
      }

        bindingActivityAddDish.etType.setOnClickListener(this@AddDishActivity)
        bindingActivityAddDish.etCategory.setOnClickListener(this@AddDishActivity)
        bindingActivityAddDish.etCookingTime.setOnClickListener(this@AddDishActivity)
        bindingActivityAddDish.btnAddDish.setOnClickListener(this@AddDishActivity)

    }


    override fun onClick(view: View) {
        when(view.id){

            R.id.iv_add_dish_image ->{
                openCameraGalleryDioalog()
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

            R.id.et_cooking_time ->{
                customItemsListDialog(
                   (getString(R.string.select_cooking_time)),
                    Constants.dishCookTime(),
                    Constants.DISH_COOKING_TIME
                )
            }

         R.id.btn_add_dish -> {

             // Define the local variables and get the EditText values.
             // For Dish Image we have the global variable defined already.

             val title = bindingActivityAddDish.etTitle.text.toString().trim()
             val type = bindingActivityAddDish.etType.text.toString().trim { it <= ' ' }
             val category = bindingActivityAddDish.etCategory.text.toString().trim { it <= ' ' }
             val ingredients = bindingActivityAddDish.etIngredients.text.toString().trim { it <= ' ' }
             val cookingTimeInMinutes = bindingActivityAddDish.etCookingTime.text.toString().trim { it <= ' ' }
             val cookingDirection = bindingActivityAddDish.etDirectionToCook.text.toString().trim { it <= ' ' }


             when {

                 TextUtils.isEmpty(imagePath) -> {
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



                     val favDishDetails: RecipeData = RecipeData(
                         imagePath,
                         "",
                         title,
                         type,
                         category,
                         ingredients,
                         cookingTimeInMinutes,
                         cookingDirection,
                     )

                     mFavDishViewModel.insert(favDishDetails)


                     Toast.makeText(
                            this@AddDishActivity,
                             "You successfully inserted favorite dish details.",
                             Toast.LENGTH_SHORT
                         ).show()

//                     if(dishID == 0) {
//                         mFavDishViewModel.insert(favDishDetails)
//
//                         Toast.makeText(
//                             this@AddRecipeActivity,
//                             "You successfully added your favorite dish details.",
//                             Toast.LENGTH_SHORT
//                         ).show()
//
//                         // You even print the log if Toast is not displayed on emulator
//                         Log.e("Insertion", "Success")
//                     }else{
//                         mFavDishViewModel.update(favDishDetails)
//
//                         Toast.makeText(
//                             this@AddRecipeActivity,
//                             "You successfully updated your favorite dish details.",
//                             Toast.LENGTH_SHORT
//                         ).show()
//
//                         // You even print the log if Toast is not displayed on emulator
//                         Log.e("Updating", "Success")
//                     }
                     // END


                     finish()
                 }
             }



         }

        }
    }

    private fun customItemsListDialog(
        DishTypeTitle: String,
        dishTypesList: ArrayList<String>,
        selectedDishType: String) {

        mCustomListDialog = Dialog(this@AddDishActivity)
        bindingDialogCustom = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(bindingDialogCustom.root)

        bindingDialogCustom.tvTitle.text = DishTypeTitle

        // Set the LayoutManager that this RecyclerView will use.
        bindingDialogCustom.rvList.layoutManager = LinearLayoutManager(this@AddDishActivity)
        // Adapter class is initialized and list is passed in the param.
        val adapter = CustomListItemAdapter(this@AddDishActivity, dishTypesList, selectedDishType)
        // adapter instance is set to the recyclerview to inflate the items.
        bindingDialogCustom.rvList.adapter = adapter
        //Start the dialog and display it on screen.
        mCustomListDialog.show()


    }

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode ==REQUEST_IMAGE_CAPTURE) {

                data?.extras?.let {
                    val thumbnail: Bitmap =
                        data.extras!!.get("data") as Bitmap // Bitmap from camera

                       Glide.with(this)
                           .load(thumbnail)
                           .into(bindingActivityAddDish.ivDishImage)

                      imagePath = saveImagePathToStorage(thumbnail)

                }
            } else if (requestCode == PICK_IMAGE) {

                data.let {
                    val selectedImagePath = data?.data

                    Glide.with(this@AddDishActivity)
                        .load(selectedImagePath)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
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

                                imagePath = saveImagePathToStorage(bitmap)
                                Log.i("ImagePath", imagePath)
                                return false
                            }

                        })

                        .into(bindingActivityAddDish.ivDishImage)

                    // Replace the add icon with edit icon once the image is selected.
                    bindingActivityAddDish.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@AddDishActivity,
                            R.drawable.ic_edit
                        )
                    )
                }

            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }


    private fun saveImagePathToStorage(thumbnail: Bitmap): String {

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
        // Return the saved image absolute path
        return file.absolutePath

    }



    private fun openCameraGalleryDioalog() {
        dialog = Dialog(this)
        bindingPickImg = PickImgCameraGalleryBinding.inflate(layoutInflater)

        dialog.setContentView(bindingPickImg.root)
        //set onclicklistener on the camera
        bindingPickImg.pictImgCamera.setOnClickListener {

            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,

                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()){
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {
                        openRetryPermission()
                    }
                }).check()

            dialog.dismiss()
        }

        bindingPickImg.pickImgGallery.setOnClickListener {

            Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        val intent = Intent()
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT)
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {


                    }
                }).check()
            dialog.dismiss()
        }

        dialog.show()


    }

    private fun openRetryPermission(){

    }

    fun selectedListItem(item: String, selection: String) {

        when (selection) {

            Constants.DISH_TYPE -> {
                mCustomListDialog.dismiss()
                bindingActivityAddDish.etType.setText(item)
            }

            Constants.DISH_CATEGORY -> {
                mCustomListDialog.dismiss()
                bindingActivityAddDish.etCategory.setText(item)
            }
            else -> {
                mCustomListDialog.dismiss()
                bindingActivityAddDish.etCookingTime.setText(item)
            }
        }

    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 100
        const val PICK_IMAGE = 200
        const val IMAGE_DIRECTORY = "favDishImage"
    }


}