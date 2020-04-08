package com.example.washugains.Adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.DataClass.Food
import com.example.washugains.R

class FoodViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.food_list_row, parent, false)) {

    private val name: TextView = itemView.findViewById(R.id.foodNameText)
    private val calories: TextView=itemView.findViewById(R.id.caloriesText)
    private val carbs: TextView=itemView.findViewById(R.id.carbsText)
    private val protein: TextView=itemView.findViewById(R.id.proteinText)
    private val sugar: TextView=itemView.findViewById(R.id.sugarText)
    private val fat: TextView=itemView.findViewById(R.id.fatText)

    fun bind(food : Food){
        name.text=food.name
        calories.text="Calories: "+food.calories
        carbs.text="Carbs: "+food.carb+"g"
        protein.text="Protein: "+food.protein+"g"
        sugar.text="Sugar: "+food.sugars+"g"
        fat.text="Fat: "+food.fat+"g"
    }
}

class FoodAdapter(private val list : ArrayList<Food>)
    : RecyclerView.Adapter<FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}