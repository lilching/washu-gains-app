package com.example.washugains.ExerciseAddedModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.washugains.DataClass.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class ExerciseAddedRepository {
    val exerciseAddedList: MutableLiveData<List<Exercise>> = MutableLiveData()
    val db= FirebaseFirestore.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateExerciseItems(uid:String) {
        if(uid!=null) {
            db.collection("users").document(uid).collection("dates")
                .document(LocalDate.now().toString()).collection("exerciseAdded").get()
                .addOnSuccessListener { QuerySnapshot ->
                    val exerciseList=ArrayList<Exercise>()
                    for (document in QuerySnapshot) {
                        var exercise = document.toObject(Exercise::class.java)
//                        exercise?.id = document.id
                        if (exercise != null) {
                            exerciseList.add(exercise)
                        }
                    }
                    exerciseAddedList.value=exerciseList
                }
        }

    }
    fun deleteExercise(id:String,uid: String,currentDate:String){
        db.collection("users").document(uid).collection("dates")
            .document(currentDate).collection("exerciseAdded").document(id).delete()
    }
}