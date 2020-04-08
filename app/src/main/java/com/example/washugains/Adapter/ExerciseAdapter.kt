package com.example.washugains.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.DataClass.Exercise
import com.example.washugains.R
import kotlinx.android.synthetic.main.exercise_list_row.view.*
import java.util.*
import kotlin.collections.ArrayList


class ExerciseAdapter(private val list : ArrayList<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

//    private val names = list.map {it.activity}
    private var filteredExerciseList = ArrayList<String>()

    class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init {
        filteredExerciseList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.exercise_list_row, parent, false)
        return ExerciseHolder(inflater)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.exerciseRow.text = filteredExerciseList[position]
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

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredExerciseList = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }
}