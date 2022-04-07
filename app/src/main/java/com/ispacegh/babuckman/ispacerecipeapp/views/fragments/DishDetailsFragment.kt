package com.ispacegh.babuckman.ispacerecipeapp.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ispacegh.babuckman.ispacerecipeapp.R
import com.ispacegh.babuckman.ispacerecipeapp.databinding.FragmentDishDetailsBinding
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import com.ispacegh.babuckman.ispacerecipeapp.utils.FavDishApplication
import com.ispacegh.babuckman.ispacerecipeapp.viewmodel.FavDishViewModel
import com.ispacegh.babuckman.ispacerecipeapp.viewmodel.FavDishViewModelFactory


class DishDetailsFragment : Fragment() {
    // TODO Step 7: Create a ViewBinding variable.
    // START
    private var mBinding: FragmentDishDetailsBinding? = null
    // END

    // TODO Step 5: Create an ViewModel instance to access the methods.
    // START
    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }
    // END
    // TODO Step 6: Create a global variable  for Dish Details and assign the args to it.
    // START
    private var mFavDishDetails: RecipeData? = null
    // END


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO Step 8: Initialize the mBinding variable.
        // START
        mBinding = FragmentDishDetailsBinding.inflate(inflater, container, false)
        return mBinding!!.root
        // END
    }

}