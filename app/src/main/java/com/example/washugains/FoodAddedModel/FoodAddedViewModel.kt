package com.example.washugains.FoodAddedModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.washugains.DataClass.Food

class FoodAddedViewModel(application: Application): AndroidViewModel(application) {
    private var _foodList: LiveData<List<Food>> = MutableLiveData()
    private val repository: FoodAddedRepository = FoodAddedRepository()

    init {
        _foodList = repository.foodAddedList
    }

    fun getFoodList() : LiveData<List<Food>> {
        return _foodList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateFoodItems(uid:String){
        repository.updateFoodItems(uid)
    }
    fun deleteFood(id:String,uid: String,currentDate:String){
        repository.deleteFood(id,uid,currentDate)
    }

}