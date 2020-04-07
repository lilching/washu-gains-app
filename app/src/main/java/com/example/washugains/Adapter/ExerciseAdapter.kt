package com.example.washugains.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.DataClass.Exercise
import com.example.washugains.R

class RecyclerViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.exercise_list_row, parent, false)) {

    private val exerciseInfo: TextView = itemView.findViewById(R.id.exerciseRow)

    fun bind(info : Exercise){
        exerciseInfo.text = info.activity
    }
}

class ExerciseAdapter(private val list : ArrayList<Exercise>)
    : RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecyclerViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}