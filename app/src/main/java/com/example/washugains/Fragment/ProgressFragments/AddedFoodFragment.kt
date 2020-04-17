package com.example.washugains.Fragment.ProgressFragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.Adapter.FoodAddedAdapter
import com.example.washugains.DataClass.DailyInfo
import com.example.washugains.DataClass.Food
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.sakebook.android.library.multilinedevider.MultiLineDivider
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.food_fragment.*
import java.time.LocalDate

class AddedFoodFragment: Fragment() {
    val foodList : ArrayList<Food> = ArrayList()
    private val db:FirebaseDatabase= FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.added_food_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        val recyclerView = view?.findViewById<RecyclerView>(R.id.foodAddedRecyclerView)
        val adapter = FoodAddedAdapter(foodList)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val multiLineDivider = MultiLineDivider(context!!)
        recyclerView?.addItemDecoration(multiLineDivider)

        val uid=FirebaseAuth.getInstance().uid
//        if(uid!=null) {
//            db.collection("users").document(uid).collection("dates")
//                .document(LocalDate.now().toString()).get()
//                .addOnSuccessListener { documentSnapshot ->
//                    var food = documentSnapshot.toObject(Food::class.java)
//                    food.id = documentSnapshot.id
//                }
//        }

    }
}