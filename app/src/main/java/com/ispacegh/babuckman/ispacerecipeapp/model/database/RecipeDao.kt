package com.ispacegh.babuckman.ispacerecipeapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ispacegh.babuckman.ispacerecipeapp.model.entities.RecipeData
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    /**
     * All queries must be executed on a separate thread.
     * They cannot be executed from Main Thread or it will cause a crash.
     *
     * Room has Kotlin coroutines support.
     * This allows your queries to be annotated with the
     * suspend modifier and then called from a coroutine
     * or from another suspension function.
     */

    /**
     * A function to insert favorite dish details to the local database using Room.
     *
     * @param favDish - Here we will pass the entity class that we have created.
     */
    @Insert
    suspend fun insertFavDishDetails(recipeData: RecipeData)



    // TODO Step 1: Create a suspend function to update the dish details.
    // START
    /**
     * A function to update favorite dish details to the local database using Room.
     *
     * @param favDish - Here we will pass the entity class that we have created with updated details along with "id".
     */
    @Update
    suspend fun updateFavDishDetails(recipeData: RecipeData)
    // END





    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<RecipeData>>
}