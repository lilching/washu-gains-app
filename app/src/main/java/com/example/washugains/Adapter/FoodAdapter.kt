package com.example.washugains.Adapter


import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.DataClass.Food
import com.example.washugains.ExpandableLayout
import com.example.washugains.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.expandable_food_row.view.*
import java.time.LocalDate
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
    private val foodButton:FloatingActionButton=itemView.findViewById(R.id.addFoodButton)


    fun bind(food : Food,db:FirebaseFirestore,currentDate:String,uid:String?){
        name.text=food.name
        calories.text="Calories: "+food.calories
        carbs.text="Carbs: "+food.carb+"g"
        protein.text="Protein: "+food.protein+"g"
        sugar.text="Sugar: "+food.sugars+"g"
        fat.text="Fat: "+food.fat+"g"
        if (uid!=null) {
            foodButton.setOnClickListener {
                db.collection("users").document(uid).collection("dates")
                    .document(currentDate).collection("foodAdded").add(food)
                    .addOnSuccessListener {
                        Toast.makeText(itemView.context,"Food added!", Toast.LENGTH_SHORT).show() }
                //add to today's data and update to firebase
                db.collection("users").document(uid).collection("dates")
                    .document(currentDate).get().addOnSuccessListener { documentSnapshot ->
                        var dailyData= documentSnapshot.toObject(DailyInfo::class.java)
                        if (dailyData!=null)
                        dailyData=
                            DailyInfo(dailyData.date,dailyData.calories+food.calories,dailyData.carb+food.carb,
                        dailyData.fat+food.fat,dailyData.sugars+food.sugars,dailyData.protein+food.protein
                            , dailyData.caloriesBurned)
                        db.collection("users").document(uid).collection("dates")
                            .document(currentDate).set(dailyData!!)
                    }

            }
        }


    }
}

class FoodAdapter(private val list : ArrayList<Food>, private val listString : ArrayList<String>)
    : RecyclerView.Adapter<FoodViewHolder>(), Filterable {

    private var filteredFoodList = ArrayList<Food>()
    private val expandedPositionSet : HashSet<Int> = HashSet()
    private val db=FirebaseFirestore.getInstance()
    private val uid=FirebaseAuth.getInstance().uid

    init {
        filteredFoodList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodViewHolder(inflater, parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(filteredFoodList[position],db,LocalDate.now().toString(),uid)

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