package com.example.washugains.Adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.DataClass.Exercise
import com.example.washugains.ExerciseAddedModel.ExerciseAddedViewModel
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class ExerciseAddedViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.added_exercise_row, parent, false)) {
    private val name: TextView = itemView.findViewById(R.id.exerciseAddedNameText)
    private val deleteButton: Button =itemView.findViewById(R.id.deleteExerciseButton)


    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(exercise : Exercise, db: FirebaseFirestore, currentDate:String, uid:String?, list:ArrayList<Exercise>, viewModel: ExerciseAddedViewModel){
        name.text=exercise.activity

        if(uid!=null) {
            deleteButton.setOnClickListener {
                println(exercise.id + "delete id here!!!")
                db.collection("users").document(uid).collection("dates")
                    .document(currentDate).collection("exerciseAdded").document(exercise.id).delete()
                    .addOnSuccessListener {
                        viewModel.deleteExercise(exercise.id,uid,currentDate)
                        viewModel.updateExerciseItems(uid)
                        //add to today's data and update to firebase
                        db.collection("users").document(uid).collection("dates")
                            .document(currentDate).get().addOnSuccessListener {documentSnapshot ->
                                var dailyData= documentSnapshot.toObject(DailyInfo::class.java)
                                if (dailyData!=null) {

                                    dailyData = DailyInfo(
                                        dailyData.date,
                                        dailyData.calories,
                                        dailyData.carb,
                                        dailyData.fat,
                                        dailyData.sugars,
                                        dailyData.protein,
                                        dailyData.caloriesBurned - exercise.calories
                                    )
                                    db.collection("users").document(uid).collection("dates")
                                        .document(currentDate).set(dailyData)
                                }
                            }
                    }
            }
        }
    }
}

class ExerciseAddedAdapter(private val list : ArrayList<Exercise>, private val viewModel: ExerciseAddedViewModel)
    : RecyclerView.Adapter<ExerciseAddedViewHolder>() {
    private val db= FirebaseFirestore.getInstance()
    private val uid= FirebaseAuth.getInstance().uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAddedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExerciseAddedViewHolder(inflater, parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExerciseAddedViewHolder, position: Int) {
        holder.bind(list[position],db, LocalDate.now().toString(),uid,list,viewModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}