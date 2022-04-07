package com.ispacegh.babuckman.ispacerecipeapp.model.database

import androidx.annotation.WorkerThread
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {
    /**
     * By default Room runs suspend queries off the main thread, therefore, we don't need to
     * implement anything else to ensure we're not doing long running database work
     * off the main thread.
     */
    @WorkerThread
    suspend fun insertFavDishData(recipeData: RecipeData) {
        recipeDao.insertFavDishDetails(recipeData)
    }

    // TODO Step 2: Create a variable for the dishes list to access it from ViewModel.
    // START
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allDishesList: Flow<List<RecipeData>> = recipeDao.getAllDishesList()
    // END



    // TODO Step 2: Create a suspend function on workerThread to Update the details that can be called from the ViewModel class.
    // START
    @WorkerThread
    suspend fun updateFavDishData(recipeData: RecipeData) {
        recipeDao.updateFavDishDetails(recipeData)
    }
    // END

    // TODO Step 2: Get the list of favorite dishes from the DAO and pass it to the ViewModel.
    // START
    val favoriteDishes: Flow<List<RecipeData>> = recipeDao.getFavoriteDishesList()
    // END

    // TODO Step 2: Create a suspend function using WorkerThread to delete the dish details.
    // START
    @WorkerThread
    suspend fun deleteFavDishData(recipeData: RecipeData) {
        recipeDao.deleteFavDishDetails(recipeData)
    }
    // END

    // TODO Step 2: Create a function get the filtered list of Dishes based on the selection.
    // START
    /**
     * A function to get the filtered list of Dishes.
     *
     * @param value - dish type selection
     */
    fun filteredListDishes(value: String): Flow<List<RecipeData>> =
        recipeDao.getFilteredDishesList(value)
    // END

}