package com.example.washugains.Fragment.InputFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.Adapter.FoodAdapter
import com.example.washugains.DataClass.Food
import com.example.washugains.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sakebook.android.library.multilinedevider.MultiLineDivider

class FoodFragment : Fragment() {

    val foodList : ArrayList<Food> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.food_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()


        val recyclerView = view?.findViewById<RecyclerView>(R.id.foodRecyclerView)
        val adapter = FoodAdapter(foodList)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val multiLineDivider = MultiLineDivider(context!!)
        recyclerView?.addItemDecoration(multiLineDivider)

        val ref = FirebaseDatabase.getInstance().getReference("food")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodList.clear()
                for (productSnapshot in dataSnapshot.children) {
                    val food = productSnapshot.getValue(Food::class.java)
                    if(food!=null) {
                        foodList.add(food)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException()
            }
        })

    }
}