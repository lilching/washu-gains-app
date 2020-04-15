package com.example.washugains.Adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.DataClass.Food
import com.example.washugains.ExpandableLayout
import com.example.washugains.R
import kotlinx.android.synthetic.main.expandable_exercise_row.view.*
import kotlinx.android.synthetic.main.expandable_food_row.view.*
import java.util.*
import kotlin.collections.ArrayList

class FoodViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.expandable_food_row, parent, false)) {

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

class FoodAdapter(private val list : ArrayList<Food>, private val listString : ArrayList<String>)
    : RecyclerView.Adapter<FoodViewHolder>(), Filterable {

    private var filteredFoodList = ArrayList<Food>()
    private val expandedPositionSet : HashSet<Int> = HashSet()

    init {
        filteredFoodList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(filteredFoodList[position])
        holder.itemView.childFoodView.text = "CHILD"

        holder.itemView.expandableFood.setOnExpandListener(object : ExpandableLayout.OnExpandListener {
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
        return filteredFoodList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredFoodList = list
                }
                else {
                    val filteredResults = ArrayList<Food>()
                    for (row in list) {
                        if (row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            filteredResults.add(row)
                        }
                    }
                    filteredFoodList = filteredResults
                }
                val finalFiltered = FilterResults()
                finalFiltered.values = filteredFoodList
                return finalFiltered

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredFoodList = results?.values as ArrayList<Food>
                notifyDataSetChanged()
            }
        }
    }
}