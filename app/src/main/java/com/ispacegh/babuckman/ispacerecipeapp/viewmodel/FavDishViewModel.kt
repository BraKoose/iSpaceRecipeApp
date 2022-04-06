package com.ispacegh.babuckman.ispacerecipeapp.viewmodel

import androidx.lifecycle.*
import com.ispacegh.babuckman.ispacerecipeapp.model.database.RecipeRepository
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import kotlinx.coroutines.launch

class FavDishViewModel(private val recipeRepository: RecipeRepository): ViewModel() {
    /**
     * Launching a new coroutine to insert the data in a non-blocking way.
     */
    fun insert(recipeData: RecipeData) = viewModelScope.launch {
        // Call the repository function and pass the details.
        recipeRepository.insertFavDishData(recipeData)
    }


    // TODO Step 3: Create a function to Update and pass the required params.
// START
    /**
     * Launching a new coroutine to update the data in a non-blocking way
     */
    fun update(recipeData: RecipeData) = viewModelScope.launch {
        recipeRepository.updateFavDishData(recipeData)
    }
// END



    val allDishesList: LiveData<List<RecipeData>> = recipeRepository.allDishesList.asLiveData()
}
/**
 * To create the ViewModel we implement a ViewModelProvider.Factory that gets as a parameter the
 * dependencies needed to create FavDishViewModel: the FavDishRepository.
 * By using viewModels and ViewModelProvider.Factory then the framework will take care of the
 * lifecycle of the ViewModel.
 * It will survive configuration changes and even if the Activity is recreated,
 * you'll always get the right instance of the FavDishViewModel class.
 */
class FavDishViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavDishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}