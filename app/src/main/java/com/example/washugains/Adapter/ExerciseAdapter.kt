package com.example.washugains.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.R
import java.util.*
import kotlin.collections.ArrayList

class ExerciseHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.exercise_list_row, parent, false)) {

    private val info: TextView = itemView.findViewById(R.id.exerciseRow)

    fun bind(exercise: String){
        info.text=exercise
    }
}

class ExerciseAdapter(private val list : ArrayList<String>)
    : RecyclerView.Adapter<ExerciseHolder>(), Filterable {

    private var filteredExerciseList = ArrayList<String>()


    init {
        filteredExerciseList = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExerciseHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        holder.bind(filteredExerciseList[position])
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