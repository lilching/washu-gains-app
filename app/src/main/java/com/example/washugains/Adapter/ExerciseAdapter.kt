package com.example.washugains.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.ExpandableLayout
import com.example.washugains.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.expandable_exercise_row.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class ExerciseHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.expandable_exercise_row, parent, false)) {

    private val info: TextView = itemView.findViewById(R.id.parentView)

    fun bind(exercise: String){
        info.text=exercise
    }
}

class ExerciseAdapter(private val list : ArrayList<String>, private val METlist: ArrayList<Int>, private val username: String)
    : RecyclerView.Adapter<ExerciseHolder>(), Filterable {

    private var filteredExerciseList = ArrayList<String>()
    private var filteredMETList = ArrayList<Int>()
    private val expandedPositionSet : HashSet<Int> = HashSet()
    private lateinit var db : FirebaseFirestore

    init {
        filteredExerciseList = list
        filteredMETList = METlist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExerciseHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("username", username).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        var weight = document.get("weight").toString().toInt()
                        var calories = document.get("calories").toString().toInt()
                        println(calories.toString() + "CALORIES HERE")
                        holder.itemView.enter.setOnClickListener{

                            val calsLost = caloriesBurned(filteredMETList[position], holder.itemView.minutesEntered.text.toString().toInt(), weight)
                            val updatedCalories = calories-calsLost
                            println("MET value " + filteredMETList[position])
                            println("calsLost " + calsLost)
                            println("current cals " + calories)
                            println("updated " + updatedCalories)

                            db.collection("users").whereEqualTo("username", username).get()
                                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result!!) {
                                            val reference = db.collection("users").document(document.id)
                                            reference.update("calories", updatedCalories).addOnSuccessListener {
                                                println("calories updated")
                                            }
                                        }
                                    }
                                })
                        }
                    }
                }
            })



        holder.bind(filteredExerciseList[position])
        holder.itemView.parentView.text = filteredExerciseList[position]
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

    fun caloriesBurned(MET: Int, time: Int, weight: Int): Int{
        val cals = ((MET*weight/2.2)*time/60).toInt()
        return cals
    }

    override fun getItemCount(): Int {
        return filteredExerciseList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredExerciseList = list
                }
                else {
                    val filteredResults = ArrayList<String>()
                    for (row in list) {
                        if (row.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            filteredResults.add(row)
                        }
                    }
                    filteredExerciseList = filteredResults
                }
                val finalFiltered = FilterResults()
                finalFiltered.values = filteredExerciseList
                return finalFiltered
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredExerciseList = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }
}