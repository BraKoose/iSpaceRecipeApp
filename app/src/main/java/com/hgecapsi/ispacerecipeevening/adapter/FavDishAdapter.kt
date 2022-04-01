package com.hgecapsi.ispacerecipeevening.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hgecapsi.ispacerecipeevening.R
import com.hgecapsi.ispacerecipeevening.data.RecipeData
import com.hgecapsi.ispacerecipeevening.databinding.ItemDishLayoutBinding
import com.hgecapsi.ispacerecipeevening.utils.Constants
import com.hgecapsi.ispacerecipeevening.view.fragments.AllRecipeFragment

class FavDishAdapter(
    private  val fragment:Fragment )
    : RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<RecipeData> = listOf()








    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding =
            ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val dish = dishes[position]
        // Load the dish image in the ImageView.
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)

        holder.tvTitle.text = dish.title

        holder.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.ibMore)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            // TODO Step 8: Assign the click event to the menu items as below and print the Log or You can display the Toast message for now.
            // START
            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish) {
                    //  Log.i("You have clicked on", "Edit Option of ${dish.title}")
                    // TODO Step 2: Replace the Log with below code to pass the dish details to AddUpdateDishActivity.
                    // START
                    val intent =
                        Intent(fragment.requireActivity(), AddRecipeActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                    fragment.requireActivity().startActivity(intent)
                    // END

                } else if (it.itemId == R.id.action_delete_dish) {
                    Log.i("You have clicked on", "Delete Option of ${dish.title}")
                    // TODO Step 6: Remove the log and call the function that we have created to delete.
                    // START
                    if (fragment is AllRecipeFragment) {
                        fragment.deleteStudent(dish)
                    }
                    // END

                }
                true
            }
            // END

            popup.show() //showing popup menu
        }


    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    // TODO Step 8: Create a function that will have the updated list of dishes that we will bind it to the adapter class.
    // START
    fun dishesList(list: List<RecipeData>) {
        dishes = list
        notifyDataSetChanged()
    }
    // END

    class ViewHolder(view: ItemDishLayoutBinding):RecyclerView.ViewHolder(view.root){
        //holds The TextViews that will add each items to

        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        //
        val ibMore = view.ibMore
    }
}