package com.ispacegh.babuckman.ispacerecipeapp.model.database

import androidx.annotation.WorkerThread
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {
    /**
     * Launching a new coroutine to insert the data in a non-blocking way.
     */
    suspend fun insertFavDishData(recipeData:RecipeData){
        recipeDao.insertFavDishDetails(recipeData)
    }


    // TODO Step 2: Create a suspend function on workerThread to Update the details that can be called from the ViewModel class.
    // START
    @WorkerThread
    suspend fun updateFavDishData(recipeData: RecipeData) {
        recipeDao.updateFavDishDetails(recipeData)
    }
    // END




    val allDishesList: Flow<List<RecipeData>> = recipeDao.getAllDishesList()
}