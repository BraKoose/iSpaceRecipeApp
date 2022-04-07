package com.ispacegh.babuckman.ispacerecipeapp.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ispacegh.babuckman.ispacerecipeapp.MainActivity

import com.ispacegh.babuckman.ispacerecipeapp.adapter.FavDishAdapter
import com.ispacegh.babuckman.ispacerecipeapp.databinding.FragmentFavoriteBinding
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import com.ispacegh.babuckman.ispacerecipeapp.utils.FavDishApplication
import com.ispacegh.babuckman.ispacerecipeapp.viewmodel.FavDishViewModel
import com.ispacegh.babuckman.ispacerecipeapp.viewmodel.FavDishViewModelFactory


class FavoriteFragment : Fragment() {
    // TODO Step 3: Create an instance of ViewBinding.
    // START
    private var mBinding: FragmentFavoriteBinding? = null
    // END


    // TODO Step  5: Create an instance of ViewModel to access the methods that are necessary to populate the UI.
    // START
    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }
    // END


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        //val rootview = inflater.inflate(R.layout.fragment_favorite, container, false)
        mBinding = FragmentFavoriteBinding.inflate(inflater,container,false)


        return mBinding!!.root
    }
    // END

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Add an observer on the LiveData returned by getFavoriteDishesList.
         * The onChanged() method fires when the observed data changes and the activity is in the foreground.
         */
        mFavDishViewModel.favoriteDishes.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                // TODO Step 5: Remove the Logs and display the list of Favorite Dishes using RecyclerView. Here we will not create a separate adapter class we cas use the same that we have created for AllDishes.
                // START

                // Set the LayoutManager that this RecyclerView will use.
                mBinding!!.rvFavoriteDishesList.layoutManager =
                    GridLayoutManager(requireActivity(), 2)
                // Adapter class is initialized and list is passed in the param.
                val adapter = FavDishAdapter(this@FavoriteFragment)
                // adapter instance is set to the recyclerview to inflate the items.
                mBinding!!.rvFavoriteDishesList.adapter = adapter

                if (it.isNotEmpty()) {
                    mBinding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE

                    adapter.dishesList(it)
                } else {
                    mBinding!!.rvFavoriteDishesList.visibility = View.GONE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
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

    // TODO Step 5: Override the onResume function to show the BottomNavigationView when the fragment is completely loaded.
    // START
    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }
    // END


    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


}