package com.hgecapsi.ispacerecipeevening.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hgecapsi.ispacerecipeevening.R
import com.hgecapsi.ispacerecipeevening.adapter.FavDishAdapter
import com.hgecapsi.ispacerecipeevening.databinding.FragmentAllRecipeBinding
import com.hgecapsi.ispacerecipeevening.databinding.ItemDishLayoutBinding
import com.hgecapsi.ispacerecipeevening.utils.FavDishApplication
import com.hgecapsi.ispacerecipeevening.view.activities.AddDishActivity
import com.hgecapsi.ispacerecipeevening.viewmodel.FavDishViewModel
import com.hgecapsi.ispacerecipeevening.viewmodel.FavDishViewModelFactory

class AllRecipeFragment : Fragment() {
    private lateinit var allReceipebinding: FragmentAllRecipeBinding

    private lateinit var mFavDishAdapter: FavDishAdapter
    private lateinit var recyclerView: RecyclerView
    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        allReceipebinding  = FragmentAllRecipeBinding.inflate(inflater, container, false)
        return allReceipebinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO Step 7: Initialize the RecyclerView and bind the adapter class
        // START
        // Set the LayoutManager that this RecyclerView will use.
        allReceipebinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        // Adapter class is initialized and list is passed in the param.
        // TODO Step 2: Make this variable as global
        mFavDishAdapter = FavDishAdapter(this@AllRecipeFragment)
        // adapter instance is set to the recyclerview to inflate the items.
        allReceipebinding.rvDishesList.adapter = mFavDishAdapter

        /**
         * Add an observer on the LiveData returned by getAllDishesList.
         * The onChanged() method fires when the observed data changes and the activity is in the foreground.
         */
        /**
         * Add an observer on the LiveData returned by getAllDishesList.
         * The onChanged() method fires when the observed data changes and the activity is in the foreground.
         */
        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                // TODO Step 9: Pass the dishes list to the adapter class.
                // START
                if (it.isNotEmpty()) {

                    allReceipebinding.rvDishesList.visibility = View.VISIBLE
                    allReceipebinding.tvNoDishesAddedYet.visibility = View.GONE

                    mFavDishAdapter.dishesList(it)
                } else {

                    allReceipebinding.rvDishesList.visibility = View.GONE
                    allReceipebinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
                // END
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_recipe_option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(),AddDishActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)

    }

}