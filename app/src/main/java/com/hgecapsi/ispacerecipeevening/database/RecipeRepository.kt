package com.hgecapsi.ispacerecipeevening.database

import androidx.annotation.WorkerThread
import androidx.room.Update
import com.hgecapsi.ispacerecipeevening.data.RecipeData
import kotlinx.coroutines.flow.Flow

/**
 * A Repository manages queries and allows you to use multiple backend.
 *
 * The DAO is passed into the repository constructor as opposed to the whole database.
 * This is because it only needs access to the DAO,
 * since the DAO contains all the read/write methods for the database.
 * There's no need to expose the entire database to the repository.
 *
 * @param favDishDao - Pass the FavDishDao as the parameter.
 */
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

    // TODO Step 2: Create a suspend function on workerThread to Update the details that can be called from the ViewModel class.
    // START
    @WorkerThread
    suspend fun updateFavDishData(recipeData: RecipeData) {
        recipeDao.updateFavDishDetails(recipeData)
    }
    // END
    // END

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allDishesList: Flow<List<RecipeData>> = recipeDao.getAllDishesList()
    // END
}