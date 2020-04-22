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

    fun caloriesBurned(MET: Int, time: Int, weight: Int): Int{
        val cals = ((MET*weight/2.2)*time/60).toInt()
        return cals
    }

    fun bind(exercise: Exercise, met:Int, db:FirebaseFirestore,currentDate:String, username: String){
        info.text=exercise.activity
        db.collection("users").whereEqualTo("username", username).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        var weight = document.get("weight").toString().toInt()
                        enter.setOnClickListener{
                            val calsLost = caloriesBurned(met, minutes.text.toString().toInt(), weight)
                            if (uid!=null) {
                                exercise.calories = calsLost
                                db.collection("users").document(uid).collection("dates")
                                    .document(currentDate).collection("exerciseAdded").add(exercise)
                                    .addOnSuccessListener {
                                        Toast.makeText(itemView.context,"Exercise added!", Toast.LENGTH_SHORT).show()
                                    }

                                db.collection("users").document(uid).collection("dates")
                                    .document(currentDate).get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        var dailyData = documentSnapshot.toObject(DailyInfo::class.java)
                                        if (dailyData != null)
                                            dailyData =
                                                DailyInfo(
                                                    dailyData.date,
                                                    dailyData.calories,
                                                    dailyData.carb,
                                                    dailyData.fat,
                                                    dailyData.sugars,
                                                    dailyData.protein,
                                                    dailyData.caloriesBurned + calsLost)
                                        db.collection("users").document(uid).collection("dates")
                                            .document(currentDate).set(dailyData!!)
                                    }
                            }
                        }
                    }
                }
            })
    }
}

class ExerciseAdapter(private val list : ArrayList<String>, private val elist : ArrayList<Exercise>, private val METlist: ArrayList<Int>, username: String)
    : RecyclerView.Adapter<ExerciseHolder>(), Filterable {

    private var filteredExerciseList = ArrayList<String>()
    private var ExerciseList = ArrayList<Exercise>()
    private var filteredMETList = ArrayList<Int>()
    private val db=FirebaseFirestore.getInstance()
    private val expandedPositionSet : HashSet<Int> = HashSet()
    private val username = username

    init {
        filteredExerciseList = list
        filteredMETList = METlist
        ExerciseList = elist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExerciseHolder(inflater,parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        holder.bind(ExerciseList[position], filteredMETList[position], db, LocalDate.now().toString(), username)
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