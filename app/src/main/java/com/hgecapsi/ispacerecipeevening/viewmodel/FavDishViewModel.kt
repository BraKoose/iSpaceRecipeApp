package com.hgecapsi.ispacerecipeevening.viewmodel

import androidx.lifecycle.*
import com.hgecapsi.ispacerecipeevening.data.RecipeData
import com.hgecapsi.ispacerecipeevening.database.RecipeRepository
import kotlinx.coroutines.launch

class  FavDishViewModel(private val recipeRepository: RecipeRepository):ViewModel() {
    /**
     * Launching a new coroutine to insert the data in a non-blocking way.
     */
    fun insert(recipeData: RecipeData) = viewModelScope.launch {
        // Call the repository function and pass the details.
        recipeRepository.insertFavDishData(recipeData)
    }

    // TODO Step 3: Get all the dishes list from the database in the ViewModel to pass it to the UI.
    // START
    /** Using LiveData and caching what allDishes returns has several benefits:
     * We can put an observer on the data (instead of polling for changes) and only
     * update the UI when the data actually changes.
     * Repository is completely separated from the UI through the ViewModel.
     */

    fun update(recipeData: RecipeData) = viewModelScope.launch {
        recipeRepository.updateFavDishData(recipeData)
    }

    val allDishesList: LiveData<List<RecipeData>> = recipeRepository.allDishesList.asLiveData()
    // END

}


/**
 * To create the ViewModel we implement a ViewModelProvider.Factory that gets as a parameter the dependencies
 * needed to create FavDishViewModel: the FavDishRepository.
 * By using viewModels and ViewModelProvider.Factory then the framework will take care of the lifecycle of the ViewModel.
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