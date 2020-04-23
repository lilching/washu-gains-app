package com.example.washugains.Adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.DataClass.Exercise
import com.example.washugains.ExpandableLayout
import com.example.washugains.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.expandable_exercise_row.view.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class ExerciseHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.expandable_exercise_row, parent, false)) {

    private val info: TextView = itemView.findViewById(R.id.parentView)
    private val enter: FloatingActionButton = itemView.findViewById(R.id.enter)
    private val minutes: TextView = itemView.findViewById(R.id.minutesEntered)
    private val uid=FirebaseAuth.getInstance().uid
    private var weight : Int = 0

    private fun caloriesBurned(MET: Int, time: Int, weight: Int): Int{
        return ((MET*weight/2.2)*time/60).toInt()
    }

    fun bind(exercise: Exercise, met:Int, db:FirebaseFirestore,currentDate:String, username: String){
        info.text=exercise.activity

        //find weight of user
        db.collection("users").whereEqualTo("username", username).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        weight = document.get("weight").toString().toInt()
                    }
                }
            })

        if (uid!=null) {
            enter.setOnClickListener {
                if (minutes.text.toString() == "") {
                    Toast.makeText(itemView.context, "Please enter a value", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val calsLost =
                        caloriesBurned(met, minutes.text.toString().toInt(), weight)
                    //                                    val exerciseHold = exercise
                    exercise.calories = calsLost
                    db.collection("users").document(uid).collection("dates")
                        .document(currentDate).collection("exerciseAdded")
                        .add(exercise)
                        .addOnSuccessListener {
                            Toast.makeText(
                                itemView.context,
                                "Exercise added!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    db.collection("users").document(uid).collection("dates")
                        .document(currentDate).get()
                        .addOnSuccessListener { documentSnapshot ->
                            var dailyData =
                                documentSnapshot.toObject(DailyInfo::class.java)
                            if (dailyData != null)
                                dailyData =
                                    DailyInfo(
                                        dailyData.date,
                                        dailyData.calories,
                                        dailyData.carb,
                                        dailyData.fat,
                                        dailyData.sugars,
                                        dailyData.protein,
                                        dailyData.caloriesBurned + calsLost
                                    )
                            db.collection("users").document(uid).collection("dates")
                                .document(currentDate).set(dailyData!!)
                        }
                }
            }
        }
    }
}

class ExerciseAdapter(private val list : ArrayList<String>, private val elist : ArrayList<Exercise>, private val METlist: ArrayList<Int>, username: String)
    : RecyclerView.Adapter<ExerciseHolder>(), Filterable {

    private var filteredExerciseList = ArrayList<String>()
    private var exerciseList = ArrayList<Exercise>()
    private var filteredMETList = ArrayList<Int>()
    private val db=FirebaseFirestore.getInstance()
    private val expandedPositionSet : HashSet<Int> = HashSet()
    private val username = username

    init {
        filteredExerciseList = list
        filteredMETList = METlist
        exerciseList = elist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExerciseHolder(inflater,parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        holder.bind(exerciseList[position], filteredMETList[position], db, LocalDate.now().toString(), username)
//        holder.itemView.parentView.text = exerciseList[position]
//        holder.itemView.childView.text = filteredMETList[position].toString()

        holder.itemView.expandable.setOnExpandListener(object : ExpandableLayout.OnExpandListener {
            override fun onExpand(expanded: Boolean) {
                if (expandedPositionSet.contains(position)) {
                    expandedPositionSet.remove(position)
                }
                else {
                    expandedPositionSet.add(position)
                }
            }
        })


    }


    override fun getItemCount(): Int {
        return exerciseList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    exerciseList = elist
                }
                else {
                    val filteredResults = ArrayList<Exercise>()
                    for (row in elist) {
                        if (row.activity.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            filteredResults.add(row)
                        }
                    }
                    exerciseList = filteredResults
                }
                val finalFiltered = FilterResults()
                finalFiltered.values = exerciseList
                return finalFiltered
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                exerciseList = results?.values as ArrayList<Exercise>
                notifyDataSetChanged()
            }
        }
    }
}