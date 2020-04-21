package com.example.washugains.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.R
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
class MLViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.ml_result_row, parent, false)) {
    private val name:TextView=itemView.findViewById(R.id.label)
    private val confidence: TextView = itemView.findViewById(R.id.confidence)



    fun bind(label : FirebaseVisionImageLabel){
        name.text="Label: "+label.text
        confidence.text="Confidence: "+label.confidence
    }
}

class MLAdapter(list : List<FirebaseVisionImageLabel>)
    : RecyclerView.Adapter<MLViewHolder>() {

    private var labelList : List<FirebaseVisionImageLabel>

    init {
        labelList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MLViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MLViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MLViewHolder, position: Int) {
        holder.bind(labelList[position])


    }

    override fun getItemCount(): Int {
        return labelList.size
    }

}