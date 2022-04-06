package com.ispacegh.babuckman.ispacerecipeapp.utils

import android.app.Application
import com.ispacegh.babuckman.ispacerecipeapp.model.database.RecipeDatabase
import com.ispacegh.babuckman.ispacerecipeapp.model.database.RecipeRepository

class FavDishApplication: Application() {
    private val database by lazy{RecipeDatabase.getDatabase(this@FavDishApplication)}
    val repository by lazy{RecipeRepository(database.recipeDao())}
}