package com.example.washugains.Adapter


import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.DataClass.Food
import com.example.washugains.FoodAddedModel.FoodAddedViewModel
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

import kotlin.collections.ArrayList

class FoodAddedViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.added_food_row, parent, false)) {

    private val name: TextView = itemView.findViewById(R.id.foodAddedNameText)
    private val calories: TextView=itemView.findViewById(R.id.caloriesAddedText)
    private val carbs: TextView=itemView.findViewById(R.id.carbsAddedText)
    private val protein: TextView=itemView.findViewById(R.id.proteinAddedText)
    private val sugar: TextView=itemView.findViewById(R.id.sugarAddedText)
    private val fat: TextView=itemView.findViewById(R.id.fatAddedText)
    private val deleteButton:Button=itemView.findViewById(R.id.deleteFoodButton)

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(food : Food, db: FirebaseFirestore, currentDate:String, uid:String?, list:ArrayList<Food>, viewModel: FoodAddedViewModel){
        name.text=food.name
        calories.text="Calories: "+food.calories
        carbs.text="Carbs: "+food.carb+"g"
        protein.text="Protein: "+food.protein+"g"
        sugar.text="Sugar: "+food.sugars+"g"
        fat.text="Fat: "+food.fat+"g"
        if(uid!=null) {
            deleteButton.setOnClickListener {
                db.collection("users").document(uid).collection("dates")
                    .document(currentDate).collection("foodAdded").document(food.id).delete()
                    .addOnSuccessListener {
                        viewModel.deleteFood(food.id,uid,currentDate)
                        viewModel.updateFoodItems(uid)
                        //add to today's data and update to firebase
                        db.collection("users").document(uid).collection("dates")
                            .document(currentDate).get().addOnSuccessListener {documentSnapshot ->
                                var dailyData= documentSnapshot.toObject(DailyInfo::class.java)
                                if (dailyData!=null) {
                                    val carb=roundTo2Helper((dailyData.carb - food.carb))
                                    val fat=roundTo2Helper((dailyData.fat - food.fat))
                                    val sugar=roundTo2Helper((dailyData.sugars - food.sugars))
                                    val protein=roundTo2Helper((dailyData.protein - food.protein))

                                    dailyData = DailyInfo(
                                        dailyData.date,
                                        dailyData.calories - food.calories,
                                        carb,
                                        fat,
                                       sugar,
                                    protein)
                                    db.collection("users").document(uid).collection("dates")
                                        .document(currentDate).set(dailyData)
                                }
                            }
                    }
            }
        }
    }
    fun roundTo2Helper(num:Double):Double{
        return Math.round(num * 100) /100.0
    }
}

class FoodAddedAdapter(private val list : ArrayList<Food>,private val viewModel: FoodAddedViewModel)
    : RecyclerView.Adapter<FoodAddedViewHolder>() {
    private val db=FirebaseFirestore.getInstance()
    private val uid= FirebaseAuth.getInstance().uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAddedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodAddedViewHolder(inflater, parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FoodAddedViewHolder, position: Int) {
        holder.bind(list[position],db,LocalDate.now().toString(),uid,list,viewModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}