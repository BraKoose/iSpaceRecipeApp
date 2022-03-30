package com.hgecapsi.ispacerecipeevening.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hgecapsi.ispacerecipeevening.data.RecipeData
import com.hgecapsi.ispacerecipeevening.databinding.ItemDishLayoutBinding

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
        }

    override fun getItemCount(): Int {
        return dishes.size
    }



    class ViewHolder(view: ItemDishLayoutBinding):RecyclerView.ViewHolder(view.root){
        //holds The TextViews that will add each items to

        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        //
        val ibMore = view.ibMore
    }
}