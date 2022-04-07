package com.ispacegh.babuckman.ispacerecipeapp.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ispacegh.babuckman.ispacerecipeapp.MainActivity
import com.ispacegh.babuckman.ispacerecipeapp.R
import com.ispacegh.babuckman.ispacerecipeapp.adapter.FavDishAdapter
import com.ispacegh.babuckman.ispacerecipeapp.databinding.FragmentAllRecipeBinding
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import com.ispacegh.babuckman.ispacerecipeapp.utils.FavDishApplication
import com.ispacegh.babuckman.ispacerecipeapp.viewmodel.FavDishViewModel
import com.ispacegh.babuckman.ispacerecipeapp.viewmodel.FavDishViewModelFactory
import com.ispacegh.babuckman.ispacerecipeapp.views.activities.AddDishActivity

class AllRecipeFragment : Fragment() {

    private lateinit var mFragmentBinding: FragmentAllRecipeBinding
    private lateinit var mFavDishAdapter: FavDishAdapter
    private lateinit var recyclerView: RecyclerView

    private val mFavDishViewModel:FavDishViewModel by viewModels {
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
        mFragmentBinding = FragmentAllRecipeBinding.inflate(inflater, container, false)
        return mFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO Step 7: Initialize the RecyclerView and bind the adapter class
        // START
        // Set the LayoutManager that this RecyclerView will use.
        mFragmentBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        // Adapter class is initialized and list is passed in the param.
        // TODO Step 2: Make this variable as global
        mFavDishAdapter = FavDishAdapter(this@AllRecipeFragment)
        // adapter instance is set to the recyclerview to inflate the items.
        mFragmentBinding.rvDishesList.adapter = mFavDishAdapter

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

                    mFragmentBinding.rvDishesList.visibility = View.VISIBLE
                    mFragmentBinding.tvNoDishesAddedYet.visibility = View.GONE

                    mFavDishAdapter.dishesList(it)
                } else {

                    mFragmentBinding.rvDishesList.visibility = View.GONE
                    mFragmentBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
                // END
            }
        }

    }

    // TODO Step 2: Create a function to navigate to the Dish Details Fragment.
    // START
    /**
     * A function to navigate to the Dish Details Fragment.
     *
     * @param favDish
     */
    fun dishDetails(recipeData: RecipeData) {

        // TODO Step 4: Hide the BottomNavigationView while navigating to the DetailsFragment.
        // START
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
        // END

        findNavController()
            .navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDishDetailsFragment(recipeData))
    }
    // END


}