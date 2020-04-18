package com.example.washugains.FoodAddedModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.washugains.DataClass.Food;
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate


class FoodAddedRepository() {
    val foodAddedList: MutableLiveData<List<Food>> = MutableLiveData()
    val db= FirebaseFirestore.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateFoodItems(uid:String) {
        if(uid!=null) {
            db.collection("users").document(uid).collection("dates")
                .document(LocalDate.now().toString()).collection("foodAdded").get()
                .addOnSuccessListener { QuerySnapshot ->
                    val foodList=ArrayList<Food>()
                    for (document in QuerySnapshot) {
                        var food = document.toObject(Food::class.java)
                        food?.id = document.id
                        if (food != null) {
                            foodList.add(food)
                        }
                    }
                   foodAddedList.value=foodList
                }
        }

    }
    fun deleteFood(id:String,uid: String,currentDate:String){
        db.collection("users").document(uid).collection("dates")
            .document(currentDate).collection("foodAdded").document(id).delete()
    }



}