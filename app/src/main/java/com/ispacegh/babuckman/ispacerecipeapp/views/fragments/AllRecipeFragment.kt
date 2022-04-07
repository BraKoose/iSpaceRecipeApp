package com.ispacegh.babuckman.ispacerecipeapp.views.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ispacegh.babuckman.ispacerecipeapp.MainActivity
import com.ispacegh.babuckman.ispacerecipeapp.R
import com.ispacegh.babuckman.ispacerecipeapp.adapter.CustomListItemAdapter
import com.ispacegh.babuckman.ispacerecipeapp.adapter.FavDishAdapter
import com.ispacegh.babuckman.ispacerecipeapp.databinding.DialogCustomListBinding
import com.ispacegh.babuckman.ispacerecipeapp.databinding.FragmentAllRecipeBinding
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import com.ispacegh.babuckman.ispacerecipeapp.utils.Constants
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


    // TODO Step 3: Make the CustomItemsListDialog as global instead of local as below.
    // START
    private lateinit var mCustomListDialog: Dialog


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

    // TODO Step 8: Create a function to navigate to the Dish Details Fragment.
    // START
    /**
     * A function to navigate to the Dish Details Fragment.
     */
    fun dishDetails(recipeData: RecipeData){

        // TODO Step 9: Call the hideBottomNavigationView function when user wants to navigate to the DishDetailsFragment.
        // START
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
        // END

        findNavController().navigate(AllRecipeFragmentDirections
            .actionAllRecipeFragmentToDishDetailsFragment(recipeData)

        )
//        findNavController().navigate(
//            R.id.action_allRecipeFragment_to_dishDetailsFragment,null
//        )

    }
    // END

    // TODO Step 4: Create a function to show the filter items in the custom list dialog.
    // START
    /**
     * A function to launch the custom dialog.
     */
    private fun filterDishesListDialog() {
        mCustomListDialog = Dialog(requireActivity())

        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mCustomListDialog.setContentView(binding.root)

        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishTypes = Constants.dishTypes()
        // TODO Step 5: Add the 0 element to  get ALL items.
        dishTypes.add(0, Constants.ALL_ITEMS)

        // Set the LayoutManager that this RecyclerView will use.
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        // Adapter class is initialized and list is passed in the param.
        val adapter = CustomListItemAdapter(
            requireActivity(),
            this,
            dishTypes,
            Constants.FILTER_SELECTION
        )
        // adapter instance is set to the recyclerview to inflate the items.
        binding.rvList.adapter = adapter
        //Start the dialog and display it on screen.
        mCustomListDialog.show()
    }
    // END



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // TODO Step 6: Assign the item click and show the filter list dialog.
            // START
            R.id.action_filter_dishes -> {
                filterDishesListDialog()
                return true
            }
            // END
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}