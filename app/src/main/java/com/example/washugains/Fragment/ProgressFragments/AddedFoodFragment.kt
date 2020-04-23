package com.example.washugains.Fragment.ProgressFragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.washugains.Adapter.FoodAddedAdapter
import com.example.washugains.DataClass.Food
import com.example.washugains.FoodAddedModel.FoodAddedViewModel
import com.example.washugains.R
import com.google.firebase.auth.FirebaseAuth
import com.sakebook.android.library.multilinedevider.MultiLineDivider

class AddedFoodFragment: Fragment() {
    val foodList : ArrayList<Food> = ArrayList()
    private lateinit var viewModel : FoodAddedViewModel

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
        viewModel=ViewModelProvider(this).get(FoodAddedViewModel::class.java)
        val adapter = FoodAddedAdapter(foodList,viewModel)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val multiLineDivider = MultiLineDivider(context!!)
        recyclerView?.addItemDecoration(multiLineDivider)


        val uid=FirebaseAuth.getInstance().uid
        if (uid!=null) {
            viewModel.updateFoodItems(uid)
        }
        viewModel!!.getFoodList().observe(this, Observer { foodItems ->
            foodList.clear()
            foodList.addAll(foodItems)
            adapter.notifyDataSetChanged()
        })
    }
}