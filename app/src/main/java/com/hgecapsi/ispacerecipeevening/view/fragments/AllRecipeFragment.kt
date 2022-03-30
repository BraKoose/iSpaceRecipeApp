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

    private lateinit var mFragmentbinding: FragmentAllRecipeBinding

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
        val mFragmentbinding = FragmentAllRecipeBinding.inflate(inflater, container, false)
        return mFragmentbinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO Step 7: Initialize the RecyclerView and bind the adapter class
        // START
        // Set the LayoutManager that this RecyclerView will use.
        mFragmentbinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        // Adapter class is initialized and list is passed in the param.
        // TODO Step 2: Make this variable as global
        mFavDishAdapter = FavDishAdapter(this@AllRecipeFragment)
        // adapter instance is set to the recyclerview to inflate the items.
        mFragmentbinding.rvDishesList.adapter = mFavDishAdapter
     mFavDishViewModel.allDishesList.observe(viewLifecycleOwner, Observer {
        if (it.isNotEmpty()){
            Log.i("ROOMDATA",it.toString())
        }
     })

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