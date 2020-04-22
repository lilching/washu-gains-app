package com.example.washugains.ExerciseAddedModel


import com.example.washugains.DataClass.Exercise;
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ExerciseAddedViewModel(application: Application): AndroidViewModel(application) {

    private var exerciseList: LiveData<List<Exercise>> = MutableLiveData()
    private val repository: ExerciseAddedRepository = ExerciseAddedRepository()
    init {
        exerciseList = repository.exerciseAddedList
    }
    fun getExerciseList() : LiveData<List<Exercise>> {
        return exerciseList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateExerciseItems(uid:String){
        repository.updateExerciseItems(uid)
    }
    fun deleteExercise(id:String,uid: String,currentDate:String){
        repository.deleteExercise(id,uid,currentDate)
    }

}